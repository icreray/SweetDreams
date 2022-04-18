package com.creray.sweetdreams.config;

import com.creray.sweetdreams.util.TimeUtil;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class Config {

    public static final String[] OPTION_NAMES;
    @Getter
    private final int maxSkipSpeedPerTick;
    @Getter
    private final String nightCantBePassedMessage;
    @Getter
    private final @NotNull String playersNeededToSkipMessage;
    @Getter
    private final @NotNull String sleepStatusMessage;
    @Getter
    private final @NotNull String timePatternMessage;
    @Getter
    private final @NotNull String goodMorningMessage;
    @Getter
    private final @NotNull String thisIsNotSleepWorldMessage;
    @Getter
    private final @NotNull String noPermissionsMessage;
    @Getter
    private final @NotNull String expectedIntegerMessage;
    @Getter
    private final @NotNull String gameruleIsNowSetToMessage;
    @Getter
    private final @NotNull String gameruleValueMessage;

    public Config(FileConfiguration configuration) {
        maxSkipSpeedPerTick = configuration.getInt(OPTION_NAMES[0], 40);
        nightCantBePassedMessage = configuration.getString(OPTION_NAMES[1], "Никакой отдых не поможет пропустить эту ночь");
        playersNeededToSkipMessage = configuration.getString(OPTION_NAMES[2], "Чтобы пропустить ночь, нужно чтобы легло еще %d игрок%s");
        sleepStatusMessage = configuration.getString(OPTION_NAMES[3], "§7Спит §r%d§7 из §r%d§7 игроков §8|§7 Время§r %s");
        timePatternMessage = configuration.getString(OPTION_NAMES[4], "%02d§8:§f%02d§7%s");
        goodMorningMessage = configuration.getString(OPTION_NAMES[5], "§7Доброе утро,§f %s");
        thisIsNotSleepWorldMessage = configuration.getString(OPTION_NAMES[6], "Невозможно выполнить данную комманду в этом мире.");
        noPermissionsMessage = configuration.getString(OPTION_NAMES[7], "&cУ вас нет прав на использование данной комманды.");
        expectedIntegerMessage = configuration.getString(OPTION_NAMES[8], "&cНеверное целое число.");
        gameruleIsNowSetToMessage = configuration.getString(OPTION_NAMES[9], "Установленно значение игрового правила playersSleepingPercentage: %d");
        gameruleValueMessage = configuration.getString(OPTION_NAMES[10], "Значение игрового правила playersSleepingPercentage: %d");
    }

    public String getPlayersNeedToSkipMessage(int playersRemain) {
        String postfix = "";
        if (playersRemain % 10 == 1) {
            postfix = "ов";
        }
        return String.format(playersNeededToSkipMessage, playersRemain, postfix);
    }

    public String getSleepStatusMessage(int playersSleeping, int playersRemainTotal, long worldTime) {
        String formattedTime = TimeUtil.getFormattedWorldTime(worldTime, this);
        return String.format(sleepStatusMessage, playersSleeping, playersRemainTotal, formattedTime);
    }

    public String getGoodMorningMessage(String nickname) {
        return String.format(goodMorningMessage, nickname);
    }

    static {
        OPTION_NAMES = new String[] {
                "maxSkipSpeedPerTick",
                "nightCantBePassedMessage",
                "playersNeededToSkipMessage",
                "sleepStatusMessage",
                "timePatternMessage",
                "goodMorningMessage",
                "thisIsNotSleepWorldMessage",
                "noPermissionsMessage",
                "expectedIntegerMessage",
                "gameruleIsNowSetToMessage",
                "gameruleValueMessage"
        };
    }
}
