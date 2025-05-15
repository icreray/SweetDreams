package com.creray.sweetdreams.sleep.world;

import com.creray.sweetdreams.hook.essentials.IEssentialsHook;
import com.creray.sweetdreams.sleep.task.NightSkipTasks;
import lombok.Getter;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static com.creray.sweetdreams.SweetDreams.LOGGER;

public class SleepWorldData {

    private final SleepWorld SLEEP_WORLD;
    private final NightSkipTasks NIGHT_TICK_TASK;

    private int defaultPlayersSleepingPercentage;
    @Getter
    private int defaultRandomTickSpeed;

    public SleepWorldData(World world, IEssentialsHook essentialsHook) {
        SLEEP_WORLD = new SleepWorld(world, essentialsHook);
        NIGHT_TICK_TASK = new NightSkipTasks(this, SLEEP_WORLD);
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
        LOGGER.info("Changed '{}' world gamerule playersSleepingPercentage from {} to 101.", world.getName(), defaultPlayersSleepingPercentage);
    }

    public void resetGameRules() {
        World world = SLEEP_WORLD.getWorld();
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, defaultPlayersSleepingPercentage);
        SLEEP_WORLD.setRandomTickSpeed(defaultRandomTickSpeed, true);
        LOGGER.info("Changed '{}' world gamerule playersSleepingPercentage from 101 to {}.", world.getName(), defaultPlayersSleepingPercentage);
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

    public String getWorldName() {
        return SLEEP_WORLD.getWorld().getName();
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
