package ru.thisistails.tailslib.Tools;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class Debug {

    private static final boolean consoleLogging, enabled;
    private static final String prefix, info, warn, error;

    private static Set<Player> ignore = new HashSet<>();

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

    public static boolean ignorePlayer(Player player) { return ignore.add(player); }
    public static boolean stopIgnoringPlayer(Player player) { return ignore.remove(player); }
    
    public static void info(@Nullable Player player, String message) {
        if (!enabled) return;

        if (player != null && !ignore.contains(player)) player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', String.format(prefix, info) + "&r " + message)));
        if (consoleLogging) Bukkit.getLogger().info("[DEBUG] " + message);
    }

    public static void warn(Player player, String message) {
        if (!enabled) return;

        if (player != null && !ignore.contains(player)) player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', String.format(prefix, warn) + "&r " + message)));
        if (consoleLogging) Bukkit.getLogger().warning("[DEBUG] " + message);
    }

    public static void error(Player player, String message) {
        if (player != null) player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', String.format(prefix, error) + "&r " + message)));
        if (consoleLogging) Bukkit.getLogger().severe("[DEBUG] " + message);
    }

}
