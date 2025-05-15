package com.creray.sweetdreams.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@Getter
public class Config {

    public static final String[] OPTION_NAMES;

    private final int maxSkipSpeedPerTick;
    private final String nightCantBePassedMessage;
    private final @NotNull String playersNeededToSkipMessage;
    private final @NotNull String sleepStatusMessage;
    private final @NotNull String goodMorningMessage;

    public Config(FileConfiguration configuration) {
        maxSkipSpeedPerTick = configuration.getInt(OPTION_NAMES[0], 40);
        nightCantBePassedMessage = configuration.getString(OPTION_NAMES[1], "Никакой отдых не поможет пропустить эту ночь");
        playersNeededToSkipMessage = configuration.getString(OPTION_NAMES[2], "Чтобы пропустить ночь, нужно чтобы легло ещё <players_remaining> игрок(ов)");
        sleepStatusMessage = configuration.getString(OPTION_NAMES[3], "<gray>Спит</gray> <players_sleeping> <gray>из</gray> <players_remaining> <gray>игроков <dark_gray>| <gray>Время <reset><hour><gray>:<white><minute><gray><postfix>");
        goodMorningMessage = configuration.getString(OPTION_NAMES[4], "<gray>Доброе утро,<white> <player_name>");
    }

    static {
        OPTION_NAMES = new String[] {
                "maxSkipSpeedPerTick",
                "nightCantBePassedMessage",
                "playersNeededToSkipMessage",
                "sleepStatusMessage",
                "goodMorningMessage"
        };
    }
}
