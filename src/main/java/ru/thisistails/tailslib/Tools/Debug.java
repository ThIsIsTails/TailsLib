package ru.thisistails.tailslib.Tools;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class Debug {

    private static final boolean consoleLogging, enabled;
    private static final String prefix, info, warn, error;

    static {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        prefix = yaml.getString("debug.prefix");
        consoleLogging = yaml.getBoolean("debug.console");
        info = yaml.getString("debug.info");
        warn = yaml.getString("debug.warn");
        error = yaml.getString("debug.error");
        enabled = yaml.getBoolean("debug.enabled");
        if (enabled) Bukkit.getLogger().warning("Debug enabled.");
    }
    
    public static void info(@Nullable Player player, String message) {
        if (!enabled) return;

        if (player != null) player.sendMessage(Component.text(String.format(prefix, info) + ChatColor.translateAlternateColorCodes('&', "&r " + message)));
        if (consoleLogging) Bukkit.getLogger().info("[DEBUG] " + message);
    }

    public static void warn(Player player, String message) {
        if (!enabled) return;

        if (player != null) player.sendMessage(Component.text(String.format(prefix, warn) + ChatColor.translateAlternateColorCodes('&', "&r " + message)));
        if (consoleLogging) Bukkit.getLogger().warning("[DEBUG] " + message);
    }

    public static void error(Player player, String message) {
        if (!enabled) return;

        if (player != null) player.sendMessage(Component.text(String.format(prefix, error) + ChatColor.translateAlternateColorCodes('&', "&r " + message)));
        if (consoleLogging) Bukkit.getLogger().severe("[DEBUG] " + message);
    }

}
