package com.creray.sweetdreams.event.listener;

import com.creray.sweetdreams.Config;
import com.creray.sweetdreams.sleep.SleepWorlds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;

public class GameruleCommandListener implements Listener {

    private final Config CONFIG;
    private final SleepWorlds SLEEP_WORLDS;

    public GameruleCommandListener(@NotNull Config config, @NotNull SleepWorlds sleepWorlds) throws InvalidParameterException {
        CONFIG = config;
        SLEEP_WORLDS = sleepWorlds;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
         String[] command = splitCommand(event.getMessage());

         if (!command[0].equals("/gamerule")) {
             return;
         }
         if (command.length < 2) {
             return;
         }
         if (!command[1].equals("playersSleepingPercentage")) {
             return;
         }
         Player player = event.getPlayer();
         if (!player.isOp()) {
             player.sendMessage(CONFIG.getNoPermissionsMessage());
             event.setCancelled(true);
             return;
         }
         execute(command, player);
         event.setCancelled(true);
    }

    private void execute(String[] command, Player player) {
        if (command.length == 2) {
            player.sendMessage(
                    String.format(
                            CONFIG.getGameruleValueMessage(),
                            CONFIG.getPlayersSleepingPercentage(player.getWorld())
                    )
            );
            return;
        }
        int playersSleepingPercentage;

        try {
            playersSleepingPercentage = Integer.parseInt(command[2]);
        }
        catch (NumberFormatException e) {
            player.sendMessage(CONFIG.getExpectedIntegerMessage());
            return;
        }
        CONFIG.setPlayersSleepingPercentage(player.getWorld(), playersSleepingPercentage);
        player.sendMessage(
                String.format(CONFIG.getGameruleIsNowSetToMessage(), playersSleepingPercentage)
        );
    }

    private String[] splitCommand(String message) {
        return message.split(" ");
    }
}
