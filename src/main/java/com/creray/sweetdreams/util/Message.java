package com.creray.sweetdreams.util;

import com.creray.sweetdreams.util.minimessage.MiniMessageBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import static com.creray.sweetdreams.SweetDreams.CONFIG;
import static com.creray.sweetdreams.util.minimessage.Placeholders.*;

public class Message {
    public static Component getConfigReloadMessage() {
        return new MiniMessageBuilder(CONFIG.getConfigReloadMessage()).build();
    }

    public static Component playersNeededToSkip(Player player, int playersRemain) {
        return new MiniMessageBuilder(CONFIG.getPlayersNeededToSkipMessage())
                .setPlaceholder(PLAYERS_REMAINING.key(), String.valueOf(playersRemain))
                .setPapiPlaceholders(player)
                .build();
    }

    public static Component sleepStatus(Player player, int playersSleeping, int playersRemainTotal, long worldTime) {
        long hour = (int)(worldTime / 1000) + 6;
        if (hour >= 24) {
            hour -= 24;
        }
        String postfix = "AM";
        if (hour > 12) {
            hour -= 12;
            postfix = "PM";
        }

        int minuteRaw = (int) ((worldTime % 1000) / 100F * 6);
        String minute = String.format("%02d", minuteRaw);
        return new MiniMessageBuilder(CONFIG.getSleepStatusMessage())
                .setPlaceholder(PLAYERS_SLEEPING.key(), playersSleeping)
                .setPlaceholder(PLAYERS_REMAINING.key(), playersRemainTotal)
                .setPlaceholder(HOUR.key(), hour)
                .setPlaceholder(MINUTE.key(), minute)
                .setPlaceholder(POSTFIX.key(), postfix)
                .setPapiPlaceholders(player)
                .build();
    }

    public static Component goodMorning(Player player) {
        return new MiniMessageBuilder(CONFIG.getGoodMorningMessage())
                .setPlaceholder(PLAYER_NAME.key(), player.getName())
                .setPapiPlaceholders(player)
                .build();
    }

    public static Component nightCantBePassed(Player player) {
        return new MiniMessageBuilder(CONFIG.getNightCantBePassedMessage())
                .setPlaceholder(PLAYER_NAME.key(), player.getName())
                .setPapiPlaceholders(player)
                .build();
    }
}
