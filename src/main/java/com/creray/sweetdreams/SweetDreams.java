package com.creray.sweetdreams;

import com.creray.sweetdreams.config.Config;
import com.creray.sweetdreams.config.ConfigLoader;
import com.creray.sweetdreams.event.listener.GameruleCommandListener;
import com.creray.sweetdreams.event.listener.BedEventsListener;
import com.creray.sweetdreams.event.listener.NightSkippedListener;
import com.creray.sweetdreams.event.listener.PlayerUpdatesListeners;
import com.creray.sweetdreams.hook.essentials.IEssentialsHook;
import com.creray.sweetdreams.sleep.world.SleepWorldData;
import com.creray.sweetdreams.sleep.world.SleepWorlds;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class SweetDreams extends JavaPlugin {

    private final PluginManager PLUGIN_MANAGER;
    private final Logger LOGGER;

    private @NotNull IEssentialsHook essentialsHook;
    private @NotNull Config config;
    @Getter
    private @NotNull SleepWorlds sleepWorlds;

    @Override
    public void onEnable() {
        config = new ConfigLoader(this, LOGGER).tryLoadConfig();
        essentialsHook = IEssentialsHook.getHook(PLUGIN_MANAGER);
        sleepWorlds = new SleepWorlds(this, config, LOGGER, essentialsHook);
        registerEvents();
    }

    @Override
    public void onDisable() {
        sleepWorlds.forEach(SleepWorldData::resetGameRules);
        sleepWorlds.stopTasks();
        HandlerList.unregisterAll(this);
    }

    private void registerEvents() {
        PLUGIN_MANAGER.registerEvents(new BedEventsListener(sleepWorlds), this);
        PLUGIN_MANAGER.registerEvents(new GameruleCommandListener(config, sleepWorlds), this);
        PLUGIN_MANAGER.registerEvents(new PlayerUpdatesListeners(sleepWorlds), this);
        PLUGIN_MANAGER.registerEvents(new NightSkippedListener(config), this);
    }

    {
        PLUGIN_MANAGER = getServer().getPluginManager();
        LOGGER = getLogger();
    }
}
