package com.creray.sweetdreams.sleep.task;

import com.creray.sweetdreams.sleep.SleepWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class PlayerCheckTask extends BukkitRunnable {

    private final NightTickTask SLEEP_RUNNABLE;
    private final SleepWorld WORLD;
    private final Set<Player> SLEEPING_PLAYERS;


    public PlayerCheckTask(NightTickTask nightTickTask, SleepWorld world, Set<Player> sleepingPlayers) {
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
