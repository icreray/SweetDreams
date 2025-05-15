package com.creray.sweetdreams.sleep.task;

import com.creray.sweetdreams.SweetDreams;
import com.creray.sweetdreams.event.NightSkippedEvent;
import com.creray.sweetdreams.sleep.world.SleepWorld;
import com.creray.sweetdreams.sleep.task.delayedmessage.NightCantBePassedMessageTask;
import com.creray.sweetdreams.sleep.task.delayedmessage.PlayersNeededMessageTask;
import com.creray.sweetdreams.sleep.world.SleepWorldData;
import com.creray.sweetdreams.util.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

import static com.creray.sweetdreams.SweetDreams.CONFIG;

public class NightSkipTasks implements Runnable {

    private final JavaPlugin PLUGIN = SweetDreams.getPlugin();
    private final SleepWorldData SLEEP_WORLD_DATA;
    private final SleepWorld SLEEP_WORLD;
    private final Set<Player> SLEEPING_PLAYERS = new HashSet<>();

    private BukkitTask task;
    private PlayerCheckTask playerCheckTask;

    private boolean isSkipping = false;
    private int skipSpeed = 0;
    private int playersNeedToSleepTotal;
    private int playersRemainingToSkip;

    public NightSkipTasks(SleepWorldData sleepWorldData, SleepWorld world) {
        SLEEP_WORLD_DATA = sleepWorldData;
        SLEEP_WORLD = world;
    }

    public boolean isActive() {
        return isSkipping;
    }

    public void addSleepingPlayer(Player player) {
        SLEEPING_PLAYERS.add(player);
        recalculateValues();
        int playersNeeded = playersRemainingToSkip - SLEEPING_PLAYERS.size();
        if (SLEEP_WORLD_DATA.getPlayersSleepingPercentage() > 100) {
            NightCantBePassedMessageTask nightCantBePassedMessageTask = new NightCantBePassedMessageTask(SLEEPING_PLAYERS);
            nightCantBePassedMessageTask.runTaskLater(PLUGIN, 1L);
        }
        else if (playersNeeded <= 0) {
            runTasks();
        }
        else  {
            PlayersNeededMessageTask playersNeededMessageTask = new PlayersNeededMessageTask(SLEEPING_PLAYERS, playersNeeded);
            playersNeededMessageTask.runTaskLater(PLUGIN, 1L);
        }
    }

    public void removeSleepingPlayer(Player player) {
        SLEEPING_PLAYERS.remove(player);
        recalculateValues();
        if (SLEEPING_PLAYERS.isEmpty() || (SLEEPING_PLAYERS.size() < playersRemainingToSkip)) {
            stopTasks();
        }
    }

    public void resetSleepingPlayers() {
        SLEEPING_PLAYERS.clear();
        recalculateValues();
        stopTasks();
    }

    public void recalculateValues() {
        playersNeedToSleepTotal = SLEEP_WORLD.getSleepCountedPlayersNumber();
        final int sleepingPlayers = SLEEPING_PLAYERS.size();
        final int playersSleepingPercentage = Math.max(SLEEP_WORLD_DATA.getPlayersSleepingPercentage(), 0);
        playersRemainingToSkip = playersNeedToSleepTotal * playersSleepingPercentage / 100;
        double sleepPlayersPercentage = 1;
        if (playersNeedToSleepTotal > 0) {
            sleepPlayersPercentage = Math.min((float) sleepingPlayers / playersNeedToSleepTotal, 1);
        }
        skipSpeed = (int) (sleepPlayersPercentage * CONFIG.getMaxSkipSpeedPerTick());
        SLEEP_WORLD.setRandomTickSpeed(SLEEP_WORLD_DATA.getDefaultRandomTickSpeed() * (1 + skipSpeed));
    }

    @Override
    public void run() {
        boolean isNextDay = SLEEP_WORLD.addTime(skipSpeed);
        sendSleepStatusMessage();
        if (isNextDay) {
            Bukkit.getPluginManager().callEvent(new NightSkippedEvent(SLEEP_WORLD.getWorld(), SLEEPING_PLAYERS));
            SLEEP_WORLD.clearWeather();
            resetSleepingPlayers();
        }
    }

    private void sendSleepStatusMessage() {
        long worldTime = SLEEP_WORLD.getTime();

        SLEEPING_PLAYERS.forEach(player -> {
            Component message = Message.sleepStatus(player, SLEEPING_PLAYERS.size(), playersNeedToSleepTotal, worldTime);
            player.sendActionBar(message);
        });
    }

    private void runTasks() {
        if (!isSkipping) {
            task = Bukkit.getScheduler().runTaskTimer(PLUGIN, this, 1L, 1L);
            playerCheckTask = new PlayerCheckTask(this, SLEEP_WORLD, SLEEPING_PLAYERS);
            playerCheckTask.runTaskTimer(PLUGIN, 2L, 20L);
            isSkipping = true;
        }
    }

    public void stopTasks() {
        if (isSkipping) {
            task.cancel();
            playerCheckTask.cancel();
            SLEEP_WORLD.setRandomTickSpeed(SLEEP_WORLD_DATA.getDefaultRandomTickSpeed());
            isSkipping = false;
        }
    }
}
