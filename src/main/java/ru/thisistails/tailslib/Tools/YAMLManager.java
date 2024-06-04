package ru.thisistails.tailslib.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class YAMLManager {

    private static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    /**
     * Requests a specific YAML file from a specified path.
     * If the file does not exist, it will create the file and then return the requested configuration.
     *
     * @param   pluginName  the name of the plugin.
     * @param   filePath    the path to the file without a leading slash.
     * @return              the requested configuration file.
     */
    public static FileConfiguration require(String pluginName, String filePath) {

        // Create a new File object with the specified path
        File file = new File(Bukkit.getPluginManager().getPlugin(pluginName).getDataFolder() + "/" + filePath);

        // If the file does not exist, save the resource from the plugin's jar
        if (!file.exists()) {
            try {
                // Save the resource to the file path without overwriting existing files
                Bukkit.getPluginManager().getPlugin(pluginName).saveResource(filePath, false);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Load and return the YAML configuration from the file
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Does not support HEX codes.
     * @param pluginName the name of the plugin.
     * @param filePath   the path to the file without a leading slash.
     * @param path       the path to the list within the file.
     * @return           a list of strings with color codes replaced.
     * @see              ChatColor
     */
    public static List<String> getAndTranslate(String pluginName, String filePath, String path) {
        // Get the YAML configuration
        YamlConfiguration config = (YamlConfiguration) require(pluginName, filePath);
        List<String> list = new ArrayList<String>();

        // Translate alternate color codes for each string in the list
        for (String i : config.getStringList(path)) { 
            list.add(ChatColor.translateAlternateColorCodes('&', i)); 
        }

        return list;
    }

    /**
     * Supports HEX codes.
     * @param pluginName the name of the plugin.
     * @param filePath   the path to the file without a leading slash.
     * @param path       the path to the string within the file.
     * @return           a string with color codes replaced.
     * @see              ChatColor
     */
    public static String getAndTranslateString(String pluginName, String filePath, String path) {
        // Get the YAML configuration
        YamlConfiguration config = (YamlConfiguration) require(pluginName, filePath);
        String getted = config.getString(path);

        // Replace HEX color codes with ChatColor equivalents
        Matcher match = pattern.matcher(getted);
        while (match.find()) {
            String color = getted.substring(match.start(), match.end());
            getted = getted.replace(color, ChatColor.of(color).toString());
            match = pattern.matcher(getted);
        }
        // Translate alternate color codes and remove remaining '&' characters
        getted = ChatColor.translateAlternateColorCodes('&', getted);
        getted = getted.replaceAll("&", "");

        return getted;
    }
}
