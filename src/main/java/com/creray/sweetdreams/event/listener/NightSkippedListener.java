package com.creray.sweetdreams.event.listener;

import com.creray.sweetdreams.event.NightSkippedEvent;
import com.creray.sweetdreams.util.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NightSkippedListener implements Listener {

    @EventHandler
    private void onNightSkipped(NightSkippedEvent event) {
        event.getSleptPlayers().forEach(this::sendGoodMorningMessage);
    }

    private void sendGoodMorningMessage(Player player) {
        Component goodMorningMessage = Message.goodMorning(player);
        player.sendActionBar(goodMorningMessage);
    }
}
