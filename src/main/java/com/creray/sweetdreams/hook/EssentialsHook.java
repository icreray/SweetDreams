package com.creray.sweetdreams.hook;

import com.earth2me.essentials.Essentials;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

public class EssentialsHook {

    private final PluginManager PLUGIN_MANAGER;
    private final @Nullable Essentials ESSENTIALS;
    private final boolean IS_HOOKED;

    public EssentialsHook(PluginManager pluginManager) {
        PLUGIN_MANAGER = pluginManager;
        ESSENTIALS = getEssentials();
        IS_HOOKED = ESSENTIALS != null;
    }

    public boolean isHooked() {
        return IS_HOOKED;
    }

    public boolean isAfk(Player player) {
        return isHooked() && ESSENTIALS.getUser(player).isAfk();
    }

    private @Nullable Essentials getEssentials() {
        return (Essentials) PLUGIN_MANAGER.getPlugin("Essentials");
    }
}
