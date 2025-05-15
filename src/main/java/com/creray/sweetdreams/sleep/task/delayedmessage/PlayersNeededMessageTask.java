package com.creray.sweetdreams.sleep.task.delayedmessage;

import com.creray.sweetdreams.util.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class PlayersNeededMessageTask extends BukkitRunnable {

    private final Set<Player> PLAYERS;
    private final int PLAYERS_NEEDED;

    public PlayersNeededMessageTask(Set<Player> players, int playersNeeded) {
        PLAYERS = players;
        PLAYERS_NEEDED = playersNeeded;
    }

    @Override
    public void run() {
        PLAYERS.forEach(this::sendPlayersNeedToSkipMessage);
    }

    private void sendPlayersNeedToSkipMessage(Player player) {
        Component message = Message.playersNeededToSkip(player, PLAYERS_NEEDED);
        player.sendActionBar(message);
    }
}
