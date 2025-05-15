package com.creray.sweetdreams.util.minimessage;

import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

public enum Placeholders {

    PLAYER_NAME("player_name"),
    PAPI("papi"),
    PLAYERS_SLEEPING("players_sleeping"),
    PLAYERS_REMAINING("players_remaining"),
    HOUR("hour"),
    MINUTE("minute"),
    POSTFIX("postfix");


    private final String key;

    Placeholders(String key) {
        this.key = key;
    }

    @Subst("")
    @NotNull
    public String key() {
        return key;
    }
}
