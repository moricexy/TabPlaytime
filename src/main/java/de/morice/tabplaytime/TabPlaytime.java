package de.morice.tabplaytime;

import de.morice.tabplaytime.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TabPlaytime extends JavaPlugin implements Listener {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);

        this.getServer().getPluginManager().registerEvents(this, this);
        // just in case this is a reload
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.updateTab(player);
        }

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                this.updateTab(player);
            }
        }, 300L, 300L);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        this.updateTab(event.getPlayer());
    }

    private void updateTab(@NotNull Player player) {
        final String color = this.configManager.getTabColor();
        final String type = this.configManager.getWrapType();
        String mode = this.configManager.getTimeFormat();

        ChatColor c;

        try {
            c = ChatColor.valueOf(color.toUpperCase());
        } catch (Exception exception) {
            c = ChatColor.GREEN;
            Bukkit.getLogger().warning("Color %s was not found, defaulting *green*");
        }
        char first = type.charAt(0);
        char second = type.charAt(1);

        if (!List.of("default", "min").contains(mode)) {
            Bukkit.getLogger().warning("Mode %s was not found, defaulting *default*");
            mode = "default";
        }

        if (type.length() != 2) {
            first = '(';
            second = ')';
            Bukkit.getLogger().warning("Type was not valid! using default *()*");
        }

        String time;

        switch (mode) {
            case "default" -> {
                final int mins = (player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) / 60;
                final int hours = mins / 60;

                if (hours > 0) {
                    time = hours + "h";
                } else {
                    time = mins + "min";
                }
            }
            case "min" -> {
                time = (player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) / 60 + "min";
            }
            default -> throw new IllegalStateException();
        }

        player.setPlayerListName(this.colorize(this.configManager.getTabFormat()
                // player specials
                .replace("{playerName}", player.getName())
                .replace("{playerPing}", Integer.toString(player.getPing()))
                .replace("{playerWorld}", player.getWorld().getName().toLowerCase())
                .replace("{playerExperienceLevel}", Integer.toString(player.getLevel()))

                // server specials

                // config specials
                .replace("{firstWrap}", Character.toString(first))
                .replace("{colorAndTime}", c + time)
                .replace("{secondWrap}", Character.toString(second))
        ));
    }

    private String colorize(@NotNull String colorized) {
        return ChatColor.translateAlternateColorCodes('&', colorized);
    }
}
