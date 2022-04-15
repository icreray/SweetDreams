package com.creray.sweetdreams.sleep;

import com.creray.sweetdreams.Config;
import com.creray.sweetdreams.hook.EssentialsHook;
import com.creray.sweetdreams.sleep.task.NightTickTask;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SleepWorldData {

    private final SleepWorld SLEEP_WORLD;
    private final NightTickTask NIGHT_TICK_TASK;

    public SleepWorldData(World world, JavaPlugin plugin, Config config, EssentialsHook essentialsHook) {
        SLEEP_WORLD = new SleepWorld(world, essentialsHook);
        NIGHT_TICK_TASK = new NightTickTask(plugin, SLEEP_WORLD, config);
    }

    public void update() {
        if (NIGHT_TICK_TASK.isActive()) {
            NIGHT_TICK_TASK.recalculateValues();
        }
    }

    public void addSleepingPlayer(Player player) {
        NIGHT_TICK_TASK.addSleepingPlayer(player);
    }

    public void removeSleepingPlayer(Player player) {
        NIGHT_TICK_TASK.removeSleepingPlayer(player);
    }

    public void stopTask() {
        if (NIGHT_TICK_TASK.isActive()) {
            NIGHT_TICK_TASK.stopTimer();
        }
    }
}
