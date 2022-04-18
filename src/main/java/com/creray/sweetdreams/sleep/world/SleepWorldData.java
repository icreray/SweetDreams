package com.creray.sweetdreams.sleep.world;

import com.creray.sweetdreams.config.Config;
import com.creray.sweetdreams.hook.essentials.IEssentialsHook;
import com.creray.sweetdreams.sleep.task.NightSkipTasks;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class SleepWorldData {

    private final SleepWorld SLEEP_WORLD;
    private final NightSkipTasks NIGHT_TICK_TASK;
    private final Logger LOGGER;

    private int defaultPlayersSleepingPercentage;
    private int defaultRandomTickSpeed;

    public SleepWorldData(World world, JavaPlugin plugin, Logger logger, Config config, IEssentialsHook essentialsHook) {
        SLEEP_WORLD = new SleepWorld(world, essentialsHook, logger);
        NIGHT_TICK_TASK = new NightSkipTasks(plugin, config, this, SLEEP_WORLD);
        LOGGER = logger;
    }

    public void update() {
        if (NIGHT_TICK_TASK.isActive()) {
            NIGHT_TICK_TASK.recalculateValues();
        }
    }

    public void setGameRules() {
        World world = SLEEP_WORLD.getWorld();
        defaultPlayersSleepingPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        defaultRandomTickSpeed = world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED);
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 101);
        LOGGER.info(
                String.format("Changed '%s' world gamerule playersSleepingPercentage from %d to 101.", world.getName(), defaultPlayersSleepingPercentage)
        );
    }

    public void resetGameRules() {
        World world = SLEEP_WORLD.getWorld();
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, defaultPlayersSleepingPercentage);
        SLEEP_WORLD.setRandomTickSpeed(defaultRandomTickSpeed, true);
        LOGGER.info(
                String.format("Changed '%s' world gamerule playersSleepingPercentage from 101 to %d.", world.getName(), defaultPlayersSleepingPercentage)
        );
    }

    public void addSleepingPlayer(Player player) {
        NIGHT_TICK_TASK.addSleepingPlayer(player);
    }

    public void removeSleepingPlayer(Player player) {
        NIGHT_TICK_TASK.removeSleepingPlayer(player);
    }

    public int getPlayersSleepingPercentage() {
        return defaultPlayersSleepingPercentage;
    }

    public int getDefaultRandomTickSpeed() {
        return defaultRandomTickSpeed;
    }

    public void setPlayersSleepingPercentage(int playersSleepingPercentage) {
        defaultPlayersSleepingPercentage = playersSleepingPercentage;
    }

    public void stopTask() {
        if (NIGHT_TICK_TASK.isActive()) {
            NIGHT_TICK_TASK.stopTasks();
        }
    }
}
