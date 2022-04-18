package com.creray.sweetdreams.util;

import com.creray.sweetdreams.config.Config;
import org.bukkit.World;

public class TimeUtil {

    public static final long DAY_TIME = 24000;

    public static boolean playersCanStillSleep(long time, World world) {
        return (time < 23460) || (time < 23992 && world.hasStorm());
    }

    public static String getFormattedWorldTime(long time, Config config) {
        long hours = (int)(time / 1000) + 6;
        if (hours >= 24) {
            hours -= 24;
        }
        String postfix = "AM";
        if (hours > 12) {
            hours -= 12;
            postfix = "PM";
        }
        int minutes = (int) ((time % 1000) / 100F * 6);
        return String.format(config.getTimePatternMessage(), hours, minutes, postfix);
    }

}
