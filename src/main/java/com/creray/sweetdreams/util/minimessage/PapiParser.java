package com.creray.sweetdreams.util.minimessage;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.creray.sweetdreams.util.minimessage.Placeholders.PAPI;

public class PapiParser {
    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().extractUrls().hexColors().build();

    @NotNull
    public static TagResolver papiTag(@NotNull Player player) {
        return TagResolver.resolver(PAPI.key(), (argumentQueue, context) -> {
            String papiPlaceholder = argumentQueue.popOr("papi tag requires an argument").value();
            String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            Component componentPlaceholder = legacyComponentSerializer.deserialize(parsedPlaceholder);

            return Tag.selfClosingInserting(componentPlaceholder);
        });
    }

    @NotNull
    public static TagResolver papiRelationalTag(@NotNull Player firstPlayer, @NotNull Player secondPlayer) {
        return TagResolver.resolver(PAPI.key(), (argumentQueue, context) -> {
            String papiPlaceholder = argumentQueue.popOr("papi tag requires an argument").value();
            String parsedPlaceholder = PlaceholderAPI.setRelationalPlaceholders(firstPlayer, secondPlayer, '%' + papiPlaceholder + '%');
            Component componentPlaceholder = legacyComponentSerializer.deserialize(parsedPlaceholder);

            return Tag.selfClosingInserting(componentPlaceholder);
        });
    }
}