package com.creray.sweetdreams.hook.essentials;

import com.earth2me.essentials.Essentials;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class EssentialsHook implements IEssentialsHook {

    private final @Nullable Essentials ESSENTIALS;
    private final boolean IS_HOOKED;

    public EssentialsHook(Plugin essentialsPlugin) {
        ESSENTIALS = (Essentials) essentialsPlugin;
        IS_HOOKED = ESSENTIALS != null;
    }

    @Override
    public boolean isHooked() {
        return IS_HOOKED;
    }

    @Override
    public boolean isAfk(Player player) {
        return isHooked() && ESSENTIALS.getUser(player).isAfk();
    }
}
