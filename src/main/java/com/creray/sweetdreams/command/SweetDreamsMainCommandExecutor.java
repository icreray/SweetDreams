package com.creray.sweetdreams.command;

import com.creray.sweetdreams.config.ConfigLoadResult;
import com.creray.sweetdreams.config.ConfigLoader;
import com.creray.sweetdreams.sleep.world.SleepWorldData;
import com.creray.sweetdreams.util.Message;
import com.creray.sweetdreams.util.minimessage.MiniMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static com.creray.sweetdreams.SweetDreams.*;

public class SweetDreamsMainCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "reload" -> runReloadConfigCommand(sender);
                case "playersSleepingPercentage" -> runSleepingPercentageCommand(sender, args);
            }
        } else {
            sender.sendMessage(new MiniMessageBuilder(CONFIG.getWrongCommandMessage()).build());
        }
        return true;
    }

    private void runReloadConfigCommand(CommandSender sender) {
        ConfigLoadResult result = ConfigLoader.loadConfig();

        if (result.isSuccessLoaded()) {
            CONFIG = result.getConfig();
            sender.sendMessage(Message.getConfigReloadMessage());
        } else {
            var exception = result.getException();
            ConfigLoader.logConfigErrors(exception);

            if (sender instanceof ConsoleCommandSender) return;
            sender.sendMessage(new MiniMessageBuilder("<red>An error is occurred while loading config file, check console for details.\nError message: <gray><message>")
                    .setUnparsedPlaceholder("message", exception.getMessage())
                    .build()
            );
        }
    }

    private void runSleepingPercentageCommand(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            switch (args[1]) {
                case "get" -> getPlayersSleepingPercentage(sender, args);
                case "set" -> setPlayersSleepingPercentage(sender, args);
            }
        } else {
            sender.sendMessage(new MiniMessageBuilder(CONFIG.getPercentageUsageMessage()).build());
        }
    }

    private void getPlayersSleepingPercentage(CommandSender sender, String[] args) {
        if (args.length == 2) {
            getPlayersSleepingPercentage(sender);
        } else {
            getPlayersSleepingPercentage(sender, args[2]);
        }
    }

    private void setPlayersSleepingPercentage(CommandSender sender, String[] args) {
        if (args.length == 3) {
            setPlayersSleepingPercentage(sender, args[2]);
        } else if (args.length > 3) {
            setPlayersSleepingPercentage(sender, args[2], args[3]);
        } else {
            sender.sendMessage(new MiniMessageBuilder(CONFIG.getPercentageUsageMessage()).build());
        }
    }

    private void getPlayersSleepingPercentage(CommandSender sender) {
        if (sender instanceof Player player) {
            getPlayersSleepingPercentage(sender, player.getWorld().getName());
            return;
        }

        sender.sendMessage(new MiniMessageBuilder(CONFIG.getProvideWorldMessage()).build());
    }

    private void getPlayersSleepingPercentage(CommandSender sender, String worldName) {
        Consumer<SleepWorldData> consumer = (sleepWorldData) -> {
            int playersSleepingPercentage = sleepWorldData.getPlayersSleepingPercentage();
            sender.sendMessage(new MiniMessageBuilder(CONFIG.getPercentageGetMessage())
                    .setPlaceholder("world_name", worldName)
                    .setPlaceholder("percentage", playersSleepingPercentage)
                    .build()
            );
        };
        sleepWorldOperation(sender, worldName, consumer);
    }

    private void setPlayersSleepingPercentage(CommandSender sender, String value) {
        if (sender instanceof Player player) {
            setPlayersSleepingPercentage(sender, value, player.getWorld().getName());
            return;
        }

        sender.sendMessage(new MiniMessageBuilder(CONFIG.getProvideWorldMessage()).build());
    }

    private void setPlayersSleepingPercentage(CommandSender sender, String value, String worldName) {
        Consumer<SleepWorldData> consumer = sleepWorldData -> {
            int playersSleepingPercentage;
            try {
                playersSleepingPercentage = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                sender.sendMessage(new MiniMessageBuilder(CONFIG.getNonIntegerMessage()).build());
                return;
            }
            sleepWorldData.setPlayersSleepingPercentage(playersSleepingPercentage);
            sender.sendMessage(new MiniMessageBuilder(CONFIG.getPercentageSetMessage())
                    .setPlaceholder("world_name", worldName)
                    .setPlaceholder("percentage", playersSleepingPercentage)
                    .build()
            );
            LOGGER.info("Set new playersSleepingPercentage value for '{}' world: {}", worldName, playersSleepingPercentage);
        };
        sleepWorldOperation(sender, worldName, consumer);
    }

    private void sleepWorldOperation(CommandSender sender, String worldName, Consumer<SleepWorldData> consumer) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage(new MiniMessageBuilder(CONFIG.getWorldNotFoundMessage())
                    .setPlaceholder("world_name", worldName)
                    .build()
            );
            return;
        }
        SleepWorldData sleepWorldData;
        try {
            sleepWorldData = SLEEP_WORLDS.getSleepWorldData(world);
        }
        catch (NoSuchElementException e) {
            sender.sendMessage(new MiniMessageBuilder(CONFIG.getWrongWorldMessage())
                    .setPlaceholder("world_name", worldName)
                    .build()
            );
            return;
        }
        consumer.accept(sleepWorldData);
    }
}
