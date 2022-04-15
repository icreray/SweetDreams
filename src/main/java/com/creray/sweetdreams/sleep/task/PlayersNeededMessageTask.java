package com.creray.sweetdreams.sleep.task;

import com.creray.sweetdreams.Config;
import com.creray.sweetdreams.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class PlayersNeededMessageTask extends BukkitRunnable {

    private final Set<Player> PLAYERS;
    private final Config CONFIG;
    private final int PLAYERS_NEEDED;

    public PlayersNeededMessageTask(Set<Player> players, Config config, int playersNeeded) {
        PLAYERS = players;
        CONFIG = config;
        PLAYERS_NEEDED = playersNeeded;
    }

    @Override
    public void run() {
        for (Player player : PLAYERS) {
            MessageUtil.sendActionBar(player, CONFIG.getPlayersNeedToSkipMessage(PLAYERS_NEEDED));
        }
    }
}
