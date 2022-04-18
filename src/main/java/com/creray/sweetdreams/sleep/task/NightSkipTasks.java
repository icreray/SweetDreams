package com.creray.sweetdreams.sleep.task;

import com.creray.sweetdreams.config.Config;
import com.creray.sweetdreams.event.NightSkippedEvent;
import com.creray.sweetdreams.sleep.world.SleepWorld;
import com.creray.sweetdreams.sleep.task.delayedmessage.NightCantBePassedMessageTask;
import com.creray.sweetdreams.sleep.task.delayedmessage.PlayersNeededMessageTask;
import com.creray.sweetdreams.sleep.world.SleepWorldData;
import com.creray.sweetdreams.util.MessageUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class NightSkipTasks implements Runnable {

    private final JavaPlugin PLUGIN;
    private final PluginManager PLUGIN_MANAGER;
    private final Config CONFIG;
    private final SleepWorldData SLEEP_WORLD_DATA;
    private final SleepWorld SLEEP_WORLD;
    private final Set<Player> SLEEPING_PLAYERS;
    private final BukkitScheduler SCHEDULER;

    private BukkitTask task;
    private PlayerCheckTask playerCheckTask;

    private boolean isSkipping;
    private int skipSpeed;
    private int playersNeedToSleepTotal;
    private int playersRemainingToSkip;

    public NightSkipTasks(JavaPlugin plugin, Config config, SleepWorldData sleepWorldData, SleepWorld world) {
        CONFIG = config;
        PLUGIN = plugin;
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
            NightCantBePassedMessageTask nightCantBePassedMessageTask = new NightCantBePassedMessageTask(SLEEPING_PLAYERS, CONFIG);
            nightCantBePassedMessageTask.runTaskLater(PLUGIN, 1L);
        }
        else if (playersNeeded <= 0) {
            runTasks();
        }
        else  {
            PlayersNeededMessageTask playersNeededMessageTask = new PlayersNeededMessageTask(SLEEPING_PLAYERS, CONFIG, playersNeeded);
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
            PLUGIN_MANAGER.callEvent(new NightSkippedEvent(SLEEP_WORLD.getWorld(), SLEEPING_PLAYERS));
            SLEEP_WORLD.clearWeather();
            resetSleepingPlayers();
        }
    }

    private void sendSleepStatusMessage() {
        long worldTime = SLEEP_WORLD.getTime();
        BaseComponent[] component = TextComponent.fromLegacyText(
                CONFIG.getSleepStatusMessage(
                        SLEEPING_PLAYERS.size(),
                        playersNeedToSleepTotal,
                        worldTime
                )
        );
        SLEEPING_PLAYERS.forEach(player ->
            MessageUtil.sendActionBar(player, component)
        );
    }

    private void runTasks() {
        if (!isSkipping) {
            task = SCHEDULER.runTaskTimer(PLUGIN, this, 1L, 1L);
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

    {
        PLUGIN_MANAGER = Bukkit.getPluginManager();
        SLEEPING_PLAYERS = new HashSet<>();
        SCHEDULER = Bukkit.getScheduler();

        isSkipping = false;
        skipSpeed = 0;
    }
}
