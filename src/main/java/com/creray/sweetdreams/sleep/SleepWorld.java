package com.creray.sweetdreams.sleep;

import com.creray.sweetdreams.hook.EssentialsHook;
import com.creray.sweetdreams.util.TimeUtil;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.security.InvalidParameterException;
import java.util.List;

public class SleepWorld {

    private final World WORLD;
    private final EssentialsHook ESSENTIALS_HOOK;

    public SleepWorld(World world, EssentialsHook essentialsHook) {
        WORLD = world;
        ESSENTIALS_HOOK = essentialsHook;
    }

    public World getWorld() {
        return WORLD;
    }

    public long getTime() {
        return WORLD.getTime();
    }

    public List<Player> getAllPlayers() {
        return WORLD.getPlayers();
    }

    public int getSleepCountedPlayersNumber() {
        int players = 0;
        for (Player player : WORLD.getPlayers()) {
            if (ESSENTIALS_HOOK.isAfk(player)) {
                continue;
            }
            players += 1;
        }
        return players;
    }

    public boolean isAfk(Player player) {
        return ESSENTIALS_HOOK.isAfk(player);
    }

    public boolean isInWorld(Player player) {
        return player.getWorld() == WORLD;
    }

    public boolean addTime(long deltaTicks) throws InvalidParameterException {
        if (deltaTicks < 0) {
            throw new InvalidParameterException();
        }
        long time = WORLD.getTime();
        time += deltaTicks;
        boolean isNextDay = false;
        if (!TimeUtil.playersCanStillSleep(time, WORLD)) {
            isNextDay = true;
            time %= TimeUtil.DAY_TIME;
        }
        WORLD.setTime(time);
        return isNextDay;
    }

    public void setRandomTickSpeed(int randomTickSpeed) {
        WORLD.setGameRule(GameRule.RANDOM_TICK_SPEED, randomTickSpeed);
    }

    public void clearWeather() {
        WORLD.setStorm(false);
        WORLD.setThundering(false);
    }
}
