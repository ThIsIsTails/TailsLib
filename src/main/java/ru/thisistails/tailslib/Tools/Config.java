package ru.thisistails.tailslib.Tools;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;

public class Config {
    
    private static @Getter YamlConfiguration config;

    public static void reloadConfig() {
        config = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        Bukkit.getLogger().info("Config reloading...");
    }

}
