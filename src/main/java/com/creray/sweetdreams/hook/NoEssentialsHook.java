package com.creray.sweetdreams.hook;

import org.bukkit.plugin.PluginManager;

public class NoEssentialsHook extends EssentialsHook {

    public NoEssentialsHook(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public boolean isHooked() {
        return false;
    }
}
