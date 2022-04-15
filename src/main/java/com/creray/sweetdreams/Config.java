package com.creray.sweetdreams;

import com.creray.sweetdreams.util.TimeUtil;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Config {

    private final JavaPlugin PLUGIN;
    private final FileConfiguration CONFIGURATION;

    private final Map<World, Integer> PLAYERS_SLEEPING_PERCENTAGES;
    @Getter
    private int maxSkipSpeedPerTick;
    @Getter
    private double playersToSpeedPercentPow;
    @Getter
    private String nightCantBePassedMessage;
    @Getter
    private @NotNull String playersNeededToSkipMessage;
    @Getter
    private @NotNull String sleepStatusMessage;
    @Getter
    private @NotNull String goodMorningMessage;
    @Getter
    private @NotNull String noPermissionsMessage;
    @Getter
    private @NotNull String expectedIntegerMessage;
    @Getter
    private @NotNull String gameruleIsNowSetToMessage;
    @Getter
    private @NotNull String gameruleValueMessage;

    public Config(JavaPlugin plugin) {
        PLUGIN = plugin;
        PLUGIN.saveDefaultConfig();
        CONFIGURATION = PLUGIN.getConfig();
        loadValues();
    }

    public int getPlayersSleepingPercentage(World world) throws NoSuchElementException {
        Integer percentage = PLAYERS_SLEEPING_PERCENTAGES.get(world);
        if (percentage == null) {
            throw new NoSuchElementException();
        }
        return percentage;
    }

    public String getPlayersNeedToSkipMessage(int playersRemain) {
        String postfix = "";
        if (playersRemain % 10 == 1) {
            postfix = "ов";
        }
        return String.format(playersNeededToSkipMessage, playersRemain, postfix);
    }

    public String getSleepStatusMessage(int playersSleeping, int playersRemainTotal, long worldTime) {
        String formattedTime = TimeUtil.getFormattedWorldTime(worldTime);
        return String.format(sleepStatusMessage, playersSleeping, playersRemainTotal, formattedTime);
    }

    public String getGoodMorningMessage(String nickname) {
        return String.format(goodMorningMessage, nickname);
    }

    public void setPlayersSleepingPercentage(World world, int playersSleepingPercentage) {
        PLAYERS_SLEEPING_PERCENTAGES.put(world, playersSleepingPercentage);
    }

    private void loadValues() {
        maxSkipSpeedPerTick = CONFIGURATION.getInt("maxSkipSpeedPerTick", 30);
        nightCantBePassedMessage = CONFIGURATION.getString("nightCantBePassedMessage", "Никакой отдых не поможет пропустить эту ночь");
        playersNeededToSkipMessage = CONFIGURATION.getString("playersNeededToSkipMessage", "Чтобы пропустить ночь, нужно чтобы легло еще %d игрок%s");
        sleepStatusMessage = CONFIGURATION.getString("sleepStatusMessage", "Спит %d из %d игроков | Время %s");
        goodMorningMessage = CONFIGURATION.getString("goodMorningMessage", "Доброе утро, %s");
        noPermissionsMessage = CONFIGURATION.getString("noPermissionsMessage", "&cУ вас нет прав на использование данной комманды.");
        expectedIntegerMessage = CONFIGURATION.getString("expectedIntegerMessage", "&cНеверное целое число.");
        gameruleIsNowSetToMessage = CONFIGURATION.getString("gameruleIsNowSetToMessage", "Установленно значение игрового правила playersSleepingPercentage: %d");
        gameruleValueMessage = CONFIGURATION.getString("gameruleValueMessage", "Значение игрового правила playersSleepingPercentage: %d");
    }

    {
        PLAYERS_SLEEPING_PERCENTAGES = new HashMap<>();
    }
}
