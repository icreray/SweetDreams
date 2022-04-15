package com.creray.sweetdreams.event.listener;

import com.creray.sweetdreams.sleep.SleepWorlds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;

import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

public class BedEventsListener implements Listener {

    private final SleepWorlds SLEEP_WORLDS;

    public BedEventsListener(@NotNull SleepWorlds sleepWorlds) throws InvalidParameterException {
        SLEEP_WORLDS = sleepWorlds;
    }

    @EventHandler
    private void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() != BedEnterResult.OK) {
            return;
        }
        Player player = event.getPlayer();
        SLEEP_WORLDS.addSleepingPlayer(player.getWorld(), player);
    }

    @EventHandler
    private void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        SLEEP_WORLDS.removeSleepingPlayer(player.getWorld(), player);
    }
}
