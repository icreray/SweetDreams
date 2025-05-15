package com.creray.sweetdreams;

import com.creray.sweetdreams.command.SweetDreamsMainCommandExecutor;
import com.creray.sweetdreams.command.SweetDreamsMainTabCompleter;
import com.creray.sweetdreams.config.Config;
import com.creray.sweetdreams.config.ConfigLoader;
import com.creray.sweetdreams.event.listener.BedEventsListener;
import com.creray.sweetdreams.event.listener.NightSkippedListener;
import com.creray.sweetdreams.event.listener.PlayerUpdatesListeners;
import com.creray.sweetdreams.hook.essentials.IEssentialsHook;
import com.creray.sweetdreams.sleep.world.SleepWorldData;
import com.creray.sweetdreams.sleep.world.SleepWorlds;
import com.creray.sweetdreams.util.minimessage.MiniMessageBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class SweetDreams extends JavaPlugin {

    private final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();
    @Getter
    private static JavaPlugin plugin;
    public static Logger LOGGER;
    public static Config CONFIG;
    public static SleepWorlds SLEEP_WORLDS;

    @Override
    public void onEnable() {
        plugin = this;
        LOGGER = getSLF4JLogger();
        MiniMessageBuilder.checkPapiAvailability();
        CONFIG = new ConfigLoader().tryLoadConfig();
        IEssentialsHook essentialsHook = IEssentialsHook.getHook(PLUGIN_MANAGER);
        SLEEP_WORLDS = new SleepWorlds(essentialsHook);
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        SLEEP_WORLDS.forEach(SleepWorldData::resetGameRules);
        SLEEP_WORLDS.stopTasks();
        HandlerList.unregisterAll(this);
    }

    private void registerEvents() {
        PLUGIN_MANAGER.registerEvents(new BedEventsListener(), this);
        PLUGIN_MANAGER.registerEvents(new PlayerUpdatesListeners(), this);
        PLUGIN_MANAGER.registerEvents(new NightSkippedListener(), this);
    }

    private void registerCommands() {
        registerCommand("sweetdreams", new SweetDreamsMainCommandExecutor(), new SweetDreamsMainTabCompleter());
    }

    private void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            LOGGER.warn("Could not find command '/{}', please reinstall the plugin.yml file.", name);
            return;
        }
        command.setExecutor(executor);
        command.setTabCompleter(tabCompleter);
    }
}
