package com.creray.sweetdreams.event;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Called when players skip night.
 */
public class NightSkippedEvent extends Event {

    private static final HandlerList HANDLERS;

    private final World WORLD;
    private final Set<Player> SLEPT_PLAYERS;

    public NightSkippedEvent(World world, Set<Player> sleptPlayers) {
        WORLD = world;
        SLEPT_PLAYERS = sleptPlayers;
    }

    public World getWorld() {
        return WORLD;
    }

    public Set<Player> getSleptPlayers() {
        return SLEPT_PLAYERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    static {
        HANDLERS = new HandlerList();
    }
}
