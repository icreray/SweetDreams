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
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public final class SweetDreams extends JavaPlugin {

    private final PluginManager PLUGIN_MANAGER;
    private final Logger LOGGER;

    private @NotNull Config config;
    @Getter
    private @NotNull SleepWorlds sleepWorlds;

    @Override
    public void onEnable() {
        config = new ConfigLoader(this, LOGGER).tryLoadConfig();
        IEssentialsHook essentialsHook = IEssentialsHook.getHook(PLUGIN_MANAGER);
        sleepWorlds = new SleepWorlds(this, config, LOGGER, essentialsHook);
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        sleepWorlds.forEach(SleepWorldData::resetGameRules);
        sleepWorlds.stopTasks();
        HandlerList.unregisterAll(this);
    }

    private void registerEvents() {
        PLUGIN_MANAGER.registerEvents(new BedEventsListener(sleepWorlds), this);
        PLUGIN_MANAGER.registerEvents(new PlayerUpdatesListeners(sleepWorlds), this);
        PLUGIN_MANAGER.registerEvents(new NightSkippedListener(config), this);
    }

    private void registerCommands() {
        registerCommand("sweetdreams", new SweetDreamsMainCommandExecutor(sleepWorlds, LOGGER), new SweetDreamsMainTabCompleter(sleepWorlds));
    }

    private void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            LOGGER.warning("Could not find command '/" + name + "', please reinstall the plugin.yml file.");
            return;
        }
        command.setExecutor(executor);
        command.setTabCompleter(tabCompleter);
    }

    {
        PLUGIN_MANAGER = getServer().getPluginManager();
        LOGGER = getLogger();
    }
}
