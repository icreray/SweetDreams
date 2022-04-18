package com.creray.sweetdreams.hook.essentials;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public interface IEssentialsHook {

    static IEssentialsHook getHook(PluginManager pluginManager) {
        Plugin essentialsPlugin = pluginManager.getPlugin("Essentials");
        if (essentialsPlugin == null) {
            return new NoEssentialsHook();
        }
        return new EssentialsHook(essentialsPlugin);
    }

    boolean isHooked();
    boolean isAfk(Player player);
}
