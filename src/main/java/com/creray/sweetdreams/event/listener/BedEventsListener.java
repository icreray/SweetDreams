package com.creray.sweetdreams.event.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import static com.creray.sweetdreams.SweetDreams.SLEEP_WORLDS;
import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

public class BedEventsListener implements Listener {

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
