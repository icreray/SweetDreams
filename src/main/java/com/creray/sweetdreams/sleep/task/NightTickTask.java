package com.creray.sweetdreams.sleep.task;

import com.creray.sweetdreams.Config;
import com.creray.sweetdreams.sleep.SleepWorld;
import com.creray.sweetdreams.util.MessageUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class NightTickTask implements Runnable {

    private final JavaPlugin PLUGIN;
    private final Config CONFIG;
    private final SleepWorld SLEEP_WORLD;
    private final Set<Player> SLEEPING_PLAYERS;
    private final BukkitScheduler SCHEDULER;

    private BukkitTask task;
    private PlayerCheckTask playerCheckTask;

    private boolean isSkipping;
    private int skipSpeed;
    private int playersNeedToSleepTotal;
    private int playersRemainingToSkip;

    public NightTickTask(JavaPlugin plugin, SleepWorld world, Config config) {
        SLEEP_WORLD = world;
        CONFIG = config;
        PLUGIN = plugin;
    }

    public boolean isActive() {
        return isSkipping;
    }

    public void addSleepingPlayer(Player player) {
        SLEEPING_PLAYERS.add(player);
        recalculateValues();
        int playersNeeded = playersRemainingToSkip - SLEEPING_PLAYERS.size();
        if (CONFIG.getPlayersSleepingPercentage(SLEEP_WORLD.getWorld()) > 100) {
            NightCantBePassedMessageTask nightCantBePassedMessageTask = new NightCantBePassedMessageTask(SLEEPING_PLAYERS, CONFIG);
            nightCantBePassedMessageTask.runTaskLater(PLUGIN, 1L);
        }
        else if (playersNeeded <= 0) {
            runTimer();
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
            stopTimer();
        }
    }

    public void resetSleepingPlayers() {
        SLEEPING_PLAYERS.clear();
        recalculateValues();
        stopTimer();
    }

    public void recalculateValues() {
        playersNeedToSleepTotal = SLEEP_WORLD.getSleepCountedPlayersNumber();
        int sleepingPlayers = SLEEPING_PLAYERS.size();
        int playersSleepingPercentage = Math.max(CONFIG.getPlayersSleepingPercentage(SLEEP_WORLD.getWorld()), 0);
        if (playersSleepingPercentage > 100) {

        }
        playersRemainingToSkip = playersNeedToSleepTotal * CONFIG.getPlayersSleepingPercentage(SLEEP_WORLD.getWorld()) / 100;
        double sleepPlayersPercentage = 1;
        if (playersNeedToSleepTotal > 0) {
            sleepPlayersPercentage = Math.min((float) sleepingPlayers / playersNeedToSleepTotal, 1);
        }
        skipSpeed = (int) (sleepPlayersPercentage * CONFIG.getMaxSkipSpeedPerTick());
        SLEEP_WORLD.setRandomTickSpeed(3 * (1 + skipSpeed));
    }

    @Override
    public void run() {
        boolean isNextDay = SLEEP_WORLD.addTime(skipSpeed);
        sendSleepStatusMessage();
        if (isNextDay) {
            SLEEPING_PLAYERS.forEach(this::sendGoodMorningMessage);
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
        SLEEPING_PLAYERS.forEach(player -> {
            MessageUtil.sendActionBar(player, component);
        });
    }

    private void sendGoodMorningMessage(Player player) {
        String goodMorningMessage = CONFIG.getGoodMorningMessage(player.getDisplayName());
        MessageUtil.sendActionBar(player, goodMorningMessage);
    }

    private void runTimer() {
        if (!isSkipping) {
            task = SCHEDULER.runTaskTimer(PLUGIN, this, 1L, 1L);
            playerCheckTask = new PlayerCheckTask(this, SLEEP_WORLD, SLEEPING_PLAYERS);
            playerCheckTask.runTaskTimer(PLUGIN, 2L, 20L);
            isSkipping = true;
        }
    }

    public void stopTimer() {
        if (isSkipping) {
            task.cancel();
            playerCheckTask.cancel();
            SLEEP_WORLD.setRandomTickSpeed(3);
            isSkipping = false;
        }
    }

    {
        SLEEPING_PLAYERS = new HashSet<>();
        SCHEDULER = Bukkit.getScheduler();

        isSkipping = false;
        skipSpeed = 0;
    }
}
