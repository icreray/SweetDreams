package com.creray.sweetdreams.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void sendActionBar(Player player, String legacyText) {
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(legacyText)
        );
    }

    public static void sendActionBar(Player player, BaseComponent ... components) {
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                components
        );
    }
}
