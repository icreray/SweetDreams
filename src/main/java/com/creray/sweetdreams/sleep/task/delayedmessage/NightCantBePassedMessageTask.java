package com.creray.sweetdreams.sleep.task.delayedmessage;

import com.creray.sweetdreams.util.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class NightCantBePassedMessageTask extends BukkitRunnable {

    private final Set<Player> PLAYERS;

    public NightCantBePassedMessageTask(Set<Player> players) {
        PLAYERS = players;
    }

    @Override
    public void run() {
        PLAYERS.forEach(this::sendNightCantBePassedMessage);
    }

    private void sendNightCantBePassedMessage(Player player) {
        Component message = Message.nightCantBePassed(player);
        player.sendActionBar(message);
    }
}
