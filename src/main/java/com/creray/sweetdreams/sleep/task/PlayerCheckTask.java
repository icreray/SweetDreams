package com.creray.sweetdreams.sleep.task;

import com.creray.sweetdreams.sleep.world.SleepWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class PlayerCheckTask extends BukkitRunnable {

    private final NightSkipTasks SLEEP_RUNNABLE;
    private final SleepWorld WORLD;
    private final Set<Player> SLEEPING_PLAYERS;


    public PlayerCheckTask(NightSkipTasks nightTickTask, SleepWorld world, Set<Player> sleepingPlayers) {
        SLEEP_RUNNABLE = nightTickTask;
        WORLD = world;
        SLEEPING_PLAYERS = sleepingPlayers;
    }

    @Override
    public void run() {
        SLEEPING_PLAYERS.forEach(this::removeInvalidSleepingPlayer);
    }

    private void removeInvalidSleepingPlayer(Player player) {
        if (!player.isOnline() || !WORLD.isInWorld(player) || WORLD.isAfk(player)) {
            SLEEP_RUNNABLE.removeSleepingPlayer(player);
        }
    }
}
