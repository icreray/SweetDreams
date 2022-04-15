package com.creray.sweetdreams;

import com.creray.sweetdreams.event.listener.GameruleCommandListener;
import com.creray.sweetdreams.event.listener.BedEventsListener;
import com.creray.sweetdreams.event.listener.PlayerUpdatesListeners;
import com.creray.sweetdreams.hook.EssentialsHook;
import com.creray.sweetdreams.hook.NoEssentialsHook;
import com.creray.sweetdreams.sleep.SleepWorlds;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class SweetDreams extends JavaPlugin {

    private final PluginManager PLUGIN_MANAGER;
    private final Logger LOGGER;

    private @NotNull EssentialsHook essentialsHook;
    private @NotNull Config config;
    @Getter
    private @NotNull SleepWorlds sleepWorlds;

    @Override
    public void onEnable() {
        config = new Config(this);
        essentialsHook = getEssentialsHook();
        sleepWorlds = new SleepWorlds(this, config, LOGGER, essentialsHook);
        registerEvents();
        sleepWorlds.forEach(sleepWorlds::setGameRules);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        sleepWorlds.stopTasks();
        sleepWorlds.forEach(sleepWorlds::resetGameRules);
    }

    private void registerEvents() {
        PLUGIN_MANAGER.registerEvents(new BedEventsListener(sleepWorlds), this);
        PLUGIN_MANAGER.registerEvents(new GameruleCommandListener(config, sleepWorlds), this);
        PLUGIN_MANAGER.registerEvents(new PlayerUpdatesListeners(sleepWorlds), this);
    }

    private EssentialsHook getEssentialsHook() {
        Plugin plugin = PLUGIN_MANAGER.getPlugin("Essentials");
        if (plugin != null) {
            return new EssentialsHook(PLUGIN_MANAGER);
        } else {
            return new NoEssentialsHook(PLUGIN_MANAGER);
        }
    }

    {
        PLUGIN_MANAGER = getServer().getPluginManager();
        LOGGER = getLogger();
    }
}
