package com.creray.sweetdreams.util;

import org.bukkit.World;

public class TimeUtil {

    public static final long DAY_TIME = 24000;

    public static boolean playersCanStillSleep(long time, World world) {
        return (time < 23460) || (time < 23992 && world.hasStorm());
    }
}
