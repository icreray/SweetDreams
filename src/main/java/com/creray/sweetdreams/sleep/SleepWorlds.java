package com.creray.sweetdreams.sleep;

import com.creray.sweetdreams.Config;
import com.creray.sweetdreams.hook.EssentialsHook;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class SleepWorlds {

    private final Logger LOGGER;
    private final JavaPlugin PLUGIN;
    private final Config CONFIG;

    private final EssentialsHook ESSENTIALS_HOOK;

    private final Map<World, SleepWorldData> SLEEP_WORLDS;

    public SleepWorlds(JavaPlugin plugin, Config config, Logger logger, EssentialsHook essentialsHook) {
        PLUGIN = plugin;
        CONFIG = config;
        LOGGER = logger;
        ESSENTIALS_HOOK = essentialsHook;
        loadWorlds();
    }

    public void forEach(Consumer<World> consumer) {
        SLEEP_WORLDS.keySet().forEach(consumer);
    }

    public void setGameRules(World world) {
        int playersSleepingPercentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        CONFIG.setPlayersSleepingPercentage(world, playersSleepingPercentage);
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 101);
    }

    public void resetGameRules(World world) {
        int playersSleepingPercentage = CONFIG.getPlayersSleepingPercentage(world);
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, playersSleepingPercentage);
    }

    public boolean contains(@NotNull World world) {
        return SLEEP_WORLDS.containsKey(world);
    }

    public void tryAddWorld(World world) {
        if (world.getEnvironment() != World.Environment.NORMAL) {
            LOGGER.info(String.format("Failed to add '%s' world to sleep worlds, because world environment isn't NORMAL.", world.getName()));
            return;
        }
        if (world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE) == false) {
            return;
        }
        SleepWorldData sleepWorldData = new SleepWorldData(world, PLUGIN, CONFIG, ESSENTIALS_HOOK);
        SLEEP_WORLDS.put(world, sleepWorldData);
        setGameRules(world);
    }

    public void addSleepingPlayer(World world, Player player) {
        if (!this.contains(world)) {
            tryAddWorld(world);
        }
        SleepWorldData sleepWorldData;
        try {
            sleepWorldData = getSleepWorldData(world);
        }
        catch (NoSuchElementException e) {
            return;
        }
        sleepWorldData.addSleepingPlayer(player);
    }

    public void removeSleepingPlayer(World world, Player player) {
        SleepWorldData sleepWorldData;
        try {
            sleepWorldData = getSleepWorldData(world);
        }
        catch (NoSuchElementException e) {
            return;
        }
        sleepWorldData.removeSleepingPlayer(player);
    }

    public void update(World world) {
        SleepWorldData sleepWorldData;
        try {
            sleepWorldData = getSleepWorldData(world);
        }
        catch (NoSuchElementException e) {
            return;
        }
        sleepWorldData.update();
    }

    public void stopTasks() {
        for (SleepWorldData sleepWorldData : SLEEP_WORLDS.values()) {
            sleepWorldData.stopTask();
        }
        SLEEP_WORLDS.clear();
    }

    public @NotNull SleepWorldData getSleepWorldData(World world) throws NoSuchElementException {
        SleepWorldData sleepWorldData = SLEEP_WORLDS.get(world);
        if (sleepWorldData == null) {
            throw new NoSuchElementException();
        }
        return sleepWorldData;
    }

    private void loadWorlds() {
        Bukkit.getWorlds().forEach(this::tryAddWorld);
    }

    {
        SLEEP_WORLDS = new HashMap<>();
    }
}
