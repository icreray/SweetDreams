package com.creray.sweetdreams.config;

import com.creray.sweetdreams.SweetDreams;
import com.google.common.base.Charsets;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Set;

import static com.creray.sweetdreams.SweetDreams.LOGGER;

public class ConfigLoader {

    private static final String FILENAME = "config.yml";

    private static final JavaPlugin PLUGIN = SweetDreams.getPlugin();

    public static @NotNull ConfigLoadResult loadConfig() {
        File configFile = new File(PLUGIN.getDataFolder(), FILENAME);
        PLUGIN.saveDefaultConfig();
        YamlConfiguration defaultConfig = loadResourceConfig();
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(configFile);

            if (!hasKeys(config, defaultConfig)) {
                throw new InvalidConfigurationException("Config keys don't match default keys");
            }

            return ConfigLoadResult.success(new Config(config));
        }
        catch (IOException | InvalidConfigurationException e) {
            return ConfigLoadResult.exceptionally(new Config(defaultConfig), e);
        }
    }

    private static boolean hasKeys(YamlConfiguration config, YamlConfiguration defaultConfig) throws IOException {
        Set<String> keys = config.getKeys(true);
        Set<String> defaultKeys = defaultConfig.getKeys(true);

        for (var key : keys) {
            if (!defaultKeys.contains(key)) {
                return false;
            }
        }
        return true;
    }

    private static YamlConfiguration loadResourceConfig() {
        InputStream configInputStream = PLUGIN.getResource(FILENAME);
        if (configInputStream == null) {
            throw new NoStandardConfigException();
        }
        InputStreamReader configReader = new InputStreamReader(configInputStream, Charsets.UTF_8);
        return YamlConfiguration.loadConfiguration(configReader);
    }

    public static void logConfigErrors(Exception exception) {
        if (exception instanceof InvalidConfigurationException e) {
            LOGGER.error("Invalid configuration file, loaded default config", e);
        } else if (exception instanceof IOException e) {
            LOGGER.error("I/O error occurred while loading configuration file, loaded default config", e);
        }
    }

    public static class NoStandardConfigException extends RuntimeException {}
}
