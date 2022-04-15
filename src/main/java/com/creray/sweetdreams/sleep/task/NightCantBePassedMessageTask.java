package com.creray.sweetdreams.sleep.task;

import com.creray.sweetdreams.Config;
import com.creray.sweetdreams.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class NightCantBePassedMessageTask extends BukkitRunnable {

    private final Set<Player> PLAYERS;
    private final Config CONFIG;

    public NightCantBePassedMessageTask(Set<Player> players, Config config) {
        PLAYERS = players;
        CONFIG = config;
    }

    @Override
    public void run() {
        for (Player player : PLAYERS) {
            MessageUtil.sendActionBar(player, CONFIG.getNightCantBePassedMessage());
        }
    }
}
