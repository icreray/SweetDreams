package com.creray.sweetdreams.util.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;


public class MiniMessageBuilder {
    private static boolean papiAvailable;
    private String format;
    private final List<TagResolver> tagResolvers = new ArrayList<>();
    private boolean shouldSerializeLegacy;

    private final MiniMessage miniMessage = MiniMessage.builder().preProcessor(string ->
            shouldSerializeLegacy ? LegacyStringFormatter.toMiniMessage(string) : string
    ).build();

    public MiniMessageBuilder(String format) {
        this.format = format;
    }

    public MiniMessageBuilder() {}

    public MiniMessageBuilder setPapiPlaceholders(@NotNull Player player) {
        if (papiAvailable) {
            tagResolvers.add(PapiParser.papiTag(player));
        }
        return this;
    }

    public MiniMessageBuilder setFormat(String format) {
        this.format = format;
        return this;
    }

    public MiniMessageBuilder setPapiRelationalPlaceholders(@NotNull Player firstPlayer, @NotNull Player secondPlayer) {
        if (papiAvailable) {
            tagResolvers.add(PapiParser.papiRelationalTag(firstPlayer, secondPlayer));
        }
        return this;
    }

    public MiniMessageBuilder setPlaceholder(@Subst("") @NotNull String key, @NotNull Component value) {
        tagResolvers.add(Placeholder.component(key, value));
        return this;
    }

    public MiniMessageBuilder setPlaceholder(@Subst("") @NotNull String key, @NotNull String value) {
        tagResolvers.add(Placeholder.parsed(key, value));
        return this;
    }

    public MiniMessageBuilder setPlaceholder(@Subst("") @NotNull String key, int value) {
        tagResolvers.add(Placeholder.component(key, Component.text(value)));
        return this;
    }

    public MiniMessageBuilder setPlaceholder(@Subst("") @NotNull String key, long value) {
        tagResolvers.add(Placeholder.component(key, Component.text(value)));
        return this;
    }

    public MiniMessageBuilder setUnparsedPlaceholder(@Subst("") @NotNull String key, @NotNull String value) {
        tagResolvers.add(Placeholder.unparsed(key, value));
        return this;
    }

    public MiniMessageBuilder serializeLegacy() {
        this.shouldSerializeLegacy = true;
        return this;
    }

    public boolean hasEmptyFormat() {
        return format.isEmpty();
    }

    @NotNull
    public Component build() {
        return miniMessage.deserialize(format, TagResolver.resolver(tagResolvers));
    }

    public static void checkPapiAvailability() {
        var plugin = getPluginManager().getPlugin("PlaceholderAPI");
        papiAvailable = plugin != null && plugin.isEnabled();
    }
}
