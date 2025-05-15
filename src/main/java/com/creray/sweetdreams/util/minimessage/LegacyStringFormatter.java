package com.creray.sweetdreams.util.minimessage;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyStringFormatter {
    private static final Pattern pattern = Pattern.compile("([&§])([0-9a-fk-or])");

    // this used because LegacyComponentSerializer is piece of shit
    @NotNull
    public static String toMiniMessage(String legacyFormat) {
        Matcher matcher = pattern.matcher(legacyFormat);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String replacement = switch (matcher.group(2)) {
                case "0" -> "<black>";
                case "1" -> "<dark_blue>";
                case "2" -> "<dark_green>";
                case "3" -> "<dark_aqua>";
                case "4" -> "<dark_red>";
                case "5" -> "<dark_purple>";
                case "6" -> "<gold>";
                case "7" -> "<gray>";
                case "8" -> "<dark_gray>";
                case "9" -> "<blue>";
                case "a" -> "<green>";
                case "b" -> "<aqua>";
                case "c" -> "<red>";
                case "d" -> "<light_purple>";
                case "e" -> "<yellow>";
                case "f" -> "<white>";
                case "k" -> "<obfuscated>";
                case "l" -> "<bold>";
                case "m" -> "<strikethrough>";
                case "n" -> "<underlined>";
                case "o" -> "<italic>";
                case "r" -> "<reset>";
                default -> null;
            };
            if (replacement != null) {
            matcher.appendReplacement(result, replacement);
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }
}
