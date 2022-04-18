package com.creray.sweetdreams.event.listener;

import com.creray.sweetdreams.config.Config;
import com.creray.sweetdreams.event.NightSkippedEvent;
import com.creray.sweetdreams.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NightSkippedListener implements Listener {

    private final Config CONFIG;

    public NightSkippedListener(Config config) {
        CONFIG = config;
    }

    @EventHandler
    private void onNightSkipped(NightSkippedEvent event) {
        event.getSleptPlayers().forEach(this::sendGoodMorningMessage);
    }

    private void sendGoodMorningMessage(Player player) {
        String goodMorningMessage = CONFIG.getGoodMorningMessage(player.getDisplayName());
        MessageUtil.sendActionBar(player, goodMorningMessage);
    }
}
