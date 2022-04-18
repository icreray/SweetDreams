package com.creray.sweetdreams.hook.essentials;

import org.bukkit.entity.Player;
public class NoEssentialsHook implements IEssentialsHook {

    @Override
    public boolean isHooked() {
        return false;
    }

    @Override
    public boolean isAfk(Player player) {
        return false;
    }
}
