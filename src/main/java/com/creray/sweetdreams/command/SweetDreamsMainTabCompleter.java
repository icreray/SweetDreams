package com.creray.sweetdreams.command;

import com.creray.sweetdreams.sleep.world.SleepWorldData;
import com.creray.sweetdreams.sleep.world.SleepWorlds;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SweetDreamsMainTabCompleter implements TabCompleter {

    private final SleepWorlds SLEEP_WORLDS;

    public SweetDreamsMainTabCompleter(SleepWorlds sleepWorlds) {
        SLEEP_WORLDS = sleepWorlds;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("playersSleepingPercentage");
            return completions;
        }
        if (args.length == 2) {
            if (args[0].equals("playersSleepingPercentage")) {
                completions.add("get");
                completions.add("set");
                return completions;
            }
            return completions;
        }
        if (args.length == 3) {
            if (args[0].equals("playersSleepingPercentage")) {
                if (args[1].equals("set")) {
                    completions.add("0");
                    completions.add("50");
                    completions.add("100");
                    return completions;
                }
                if (args[1].equals("get")) {
                    for (SleepWorldData sleepWorldData : SLEEP_WORLDS.getSleepWorldsData()) {
                        String worldName = sleepWorldData.getWorldName();
                        completions.add(worldName);
                    }
                    return completions;
                }
            }
            return completions;
        }
        if (args.length == 4) {
            if (args[0].equals("playersSleepingPercentage") && args[1].equals("set")) {
                for (SleepWorldData sleepWorldData : SLEEP_WORLDS.getSleepWorldsData()) {
                    String worldName = sleepWorldData.getWorldName();
                    completions.add(worldName);
                }
                return completions;
            }
            return completions;
        }
        return completions;
    }
}
