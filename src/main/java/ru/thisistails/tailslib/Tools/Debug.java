package ru.thisistails.tailslib.Tools;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

/**
 * Utility class for logging debug messages to players and the console.
 * The class supports different logging levels: info, warn, and error.
 * It also allows ignoring specific players from receiving debug messages.
 */
public class Debug {

    // Configuration settings
    private static final boolean consoleLogging;
    private static final boolean enabled;
    private static final boolean suppressErrors;
    private static final String prefix;
    private static final String info;
    private static final String warn;
    private static final String error;

    // Set of players to be ignored for debug messages
    private static Set<Player> ignore = new HashSet<>();

    // Static block to initialize configuration settings
    static {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        prefix = yaml.getString("debug.prefix", "[DEBUG]");
        consoleLogging = yaml.getBoolean("debug.console", true);
        info = yaml.getString("debug.info", "[INFO]");
        warn = yaml.getString("debug.warn", "[WARN]");
        error = yaml.getString("debug.error", "[ERROR]");
        enabled = yaml.getBoolean("debug.enabled", true);
        suppressErrors = yaml.getBoolean("debug.suppressErrors", false);
    }

    /**
     * Adds a player to the ignore list, preventing them from receiving debug messages.
     * @param player The player to ignore.
     * @return true if the player was successfully added to the ignore list, false if they were already ignored.
     */
    public static boolean ignorePlayer(Player player) {
        return ignore.add(player);
    }

    /**
     * Removes a player from the ignore list, allowing them to receive debug messages again.
     * @param player The player to stop ignoring.
     * @return true if the player was successfully removed from the ignore list, false if they were not being ignored.
     */
    public static boolean stopIgnoringPlayer(Player player) {
        return ignore.remove(player);
    }

    /**
     * Logs an info-level debug message to the specified player and/or console.
     * @param player The player to send the message to, or null to only log to the console.
     * @param message The message to log.
     */
    public static void info(@Nullable Player player, String message) {
        if (!enabled) return;

        String formattedMessage = ChatColor.translateAlternateColorCodes('&', String.format(prefix, info) + "&r " + message);
        if (player != null && !ignore.contains(player)) {
            player.sendMessage(Component.text(formattedMessage));
        }
        if (consoleLogging) {
            Bukkit.getLogger().info("[DEBUG] " + message);
        }
    }

    /**
     * Logs an info-level debug message to the console.
     * @param message The message to log.
     */
    public static void info(String message) {
        info(null, message);
    }

    /**
     * Logs a warning-level debug message to the specified player and/or console.
     * @param player The player to send the message to, or null to only log to the console.
     * @param message The message to log.
     */
    public static void warn(@Nullable Player player, String message) {
        if (!enabled) return;

        String formattedMessage = ChatColor.translateAlternateColorCodes('&', String.format(prefix, warn) + "&r " + message);
        if (player != null && !ignore.contains(player)) {
            player.sendMessage(Component.text(formattedMessage));
        }
        if (consoleLogging) {
            Bukkit.getLogger().warning("[DEBUG] " + message);
        }
    }

    /**
     * Logs a warning-level debug message to the console.
     * @param message The message to log.
     */
    public static void warn(String message) {
        warn(null, message);
    }

    /**
     * Logs an error-level debug message to the specified player and/or console.
     * @param player The player to send the message to, or null to only log to the console.
     * @param message The message to log.
     */
    public static void error(@Nullable Player player, String message) {
        if (suppressErrors) return;

        String formattedMessage = ChatColor.translateAlternateColorCodes('&', String.format(prefix, error) + "&r " + message);
        if (player != null) {
            player.sendMessage(Component.text(formattedMessage));
        }
        if (consoleLogging) {
            Bukkit.getLogger().severe("[DEBUG] " + message);
        }
    }

    /**
     * Logs an error-level debug message to the console.
     * @param message The message to log.
     */
    public static void error(String message) {
        error(null, message);
    }
}