package com.creray.sweetdreams.event.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.creray.sweetdreams.SweetDreams.SLEEP_WORLDS;

public class PlayerUpdatesListeners implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SLEEP_WORLDS.update(player.getWorld());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SLEEP_WORLDS.removeSleepingPlayer(player.getWorld(), player);

    }

    @EventHandler
    private void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        SLEEP_WORLDS.update(event.getFrom());
        Player player  = event.getPlayer();
        SLEEP_WORLDS.update(player.getWorld());
    }
}
