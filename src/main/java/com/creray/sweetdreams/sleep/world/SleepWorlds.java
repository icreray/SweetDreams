package com.creray.sweetdreams.sleep.world;

import com.creray.sweetdreams.hook.essentials.IEssentialsHook;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

import static com.creray.sweetdreams.SweetDreams.LOGGER;

public class SleepWorlds {

    private final IEssentialsHook ESSENTIALS_HOOK;

    private final Map<World, SleepWorldData> SLEEP_WORLDS;

    public SleepWorlds(IEssentialsHook essentialsHook) {
        ESSENTIALS_HOOK = essentialsHook;
        loadWorlds();
    }

    public void forEach(Consumer<SleepWorldData> consumer) {
        SLEEP_WORLDS.values().forEach(consumer);
    }

    public boolean contains(@NotNull World world) {
        return SLEEP_WORLDS.containsKey(world);
    }

    public void tryAddWorld(World world) {
        if (world.getEnvironment() != World.Environment.NORMAL) {
            LOGGER.info("Failed to add '{}' world to sleep worlds, because world environment isn't NORMAL.", world.getName());
            return;
        }
        if (world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE) == false) {
            LOGGER.info("Failed to add '{}' world to sleep worlds, because world gamerule doDaylightCycle is false.", world.getName());
            return;
        }
        SleepWorldData sleepWorldData = new SleepWorldData(world, ESSENTIALS_HOOK);
        sleepWorldData.setGameRules();
        SLEEP_WORLDS.put(world, sleepWorldData);
    }

    public void addSleepingPlayer(World world, Player player) {
        if (!this.contains(world)) {
            tryAddWorld(world);
        }
        tryExecuteSleepWorldOperation(world, sleepWorldData -> sleepWorldData.addSleepingPlayer(player));
    }

    public void removeSleepingPlayer(World world, Player player) {
        tryExecuteSleepWorldOperation(world, sleepWorldData -> sleepWorldData.removeSleepingPlayer(player));
    }

    public void update(World world) {
        tryExecuteSleepWorldOperation(world, SleepWorldData::update);
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

    public @NotNull Collection<SleepWorldData> getSleepWorldsData() {
        return SLEEP_WORLDS.values();
    }

    private void tryExecuteSleepWorldOperation(World world, Consumer<SleepWorldData> consumer) {
        SleepWorldData sleepWorldData;
        try {
            sleepWorldData = getSleepWorldData(world);
        }
        catch (NoSuchElementException e) {
            return;
        }
        consumer.accept(sleepWorldData);
    }

    private void loadWorlds() {
        Bukkit.getWorlds().forEach(this::tryAddWorld);
    }

    {
        SLEEP_WORLDS = new HashMap<>();
    }
}
