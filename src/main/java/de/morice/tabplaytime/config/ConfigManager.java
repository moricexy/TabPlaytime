package de.morice.tabplaytime.config;

import de.morice.tabplaytime.TabPlaytime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    private final TabPlaytime plugin;

    public ConfigManager(@NotNull TabPlaytime plugin) {
        this.plugin = plugin;
        this.loadConfig();
    }

    private void loadConfig() {
        final FileConfiguration config = this.plugin.getConfig();

        if (!config.isSet("tabColor")) {
            config.set("tabColor", "GREEN");
            config.setComments("tabColor", List.of(
                    "which color will the playtime display have in tab?",
                    "defaults to GREEN, even when an invalid color is provided")
            );
        }

        if (!config.isSet("timeFormat")) {
            config.set("timeFormat", "default");
            config.setComments("timeFormat", List.of(
                    "Represents what format the text is displayed by",
                    "default: 0min-60min, 5h",
                    "min: 5h -> 300min"
            ));
        }

        if (!config.isSet("wrapType")) {
            config.set("wrapType", "()");
            config.setComments("wrapType", List.of(
                    "What char should be used on end and start of of the format?",
                    "Must be 2 chars",
                    "default: ()"
            ));
        }

        if (!config.isSet("displayType")) {
            config.set("displayType", "END");
            config.setComments("displayType", List.of(
                    "Where should the playtime stay?",
                    "Must be END or START (lowercase works too)",
                    "default: END"
            ));
        }

        this.plugin.saveConfig();
    }

    public String getTabColor() {
        return this.plugin.getConfig().getString("tabColor", "GREEN");
    }

    public String getWrapType() {
        return this.plugin.getConfig().getString("wrapType", "()");
    }

    public String getTimeFormat() {
        return this.plugin.getConfig().getString("timeFormat", "default");
    }
}
