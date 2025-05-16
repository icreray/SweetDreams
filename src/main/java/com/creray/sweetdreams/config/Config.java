package com.creray.sweetdreams.config;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

@Getter
public class Config {

    private final int maxSkipSpeedPerTick;
    private final boolean shouldSkipThunder;
    private final int minimumWeatherClearTime;
    private final int maximumWeatherClearTime;

    private final String configReloadMessage;
    private final String nightCantBePassedMessage;
    private final String playersNeededToSkipMessage;
    private final String sleepStatusMessage;
    private final String goodMorningMessage;

    public Config(YamlConfiguration configuration) {
        maxSkipSpeedPerTick = configuration.getInt("max-skip-speed", 40);
        shouldSkipThunder = configuration.getBoolean("should-skip-thunder", true);
        minimumWeatherClearTime = configuration.getInt("minimum-clear-time", 12000);
        maximumWeatherClearTime = configuration.getInt("maximum-clear-time", 12000);

        configReloadMessage = configuration.getString("message.config-reload");
        nightCantBePassedMessage = configuration.getString("message.night-skipping.night-cant-be-passed");
        playersNeededToSkipMessage = configuration.getString("message.night-skipping.players-needed-to-skip");
        sleepStatusMessage = configuration.getString("message.night-skipping.sleep-status");
        goodMorningMessage = configuration.getString("message.night-skipping.good-morning");

    }
}