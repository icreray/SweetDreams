package com.creray.sweetdreams.config;

import com.creray.sweetdreams.SweetDreams;
import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.creray.sweetdreams.SweetDreams.LOGGER;

public class ConfigLoader {

    private static final String FILENAME = "config.yml";

    private final JavaPlugin PLUGIN = SweetDreams.getPlugin();

    public @NotNull Config tryLoadConfig() {
        File configFile = new File(PLUGIN.getDataFolder(), FILENAME);
        PLUGIN.saveDefaultConfig();
        try {
            return loadConfig(configFile);
        }
        catch (IOException e) {
            FileConfiguration emptyConfiguration = new YamlConfiguration();
            LOGGER.warn("Unable to load config.");
            return new Config(emptyConfiguration);
        }
    }

    private @NotNull Config loadConfig(File configFile) throws IOException {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
        FileConfiguration defaultConfiguration = loadResourceConfig();
        for (String optionName : Config.OPTION_NAMES) {
            if (!configuration.contains(optionName)) {
                replaceConfig(configuration, defaultConfiguration, configFile);
                break;
            }
        }
        return new Config(configuration);
    }

    private void replaceConfig(FileConfiguration configuration, FileConfiguration defaultConfiguration, File configFile) throws IOException {
        LOGGER.info("Invalid config, replaced it with default.");
        configuration.setDefaults(defaultConfiguration);
        defaultConfiguration.save(configFile);
    }

    private FileConfiguration loadResourceConfig() {
        InputStream configInputStream = PLUGIN.getResource(FILENAME);
        if (configInputStream == null) {
            throw new NoStandardConfigException();
        }
        InputStreamReader configReader = new InputStreamReader(configInputStream, Charsets.UTF_8);
        return YamlConfiguration.loadConfiguration(configReader);
    }

    public static class NoStandardConfigException extends RuntimeException {}
}
