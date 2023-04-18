package ru.thisistails.tailslib;

import org.bukkit.plugin.java.JavaPlugin;

import ru.thisistails.tailslib.Tools.YAMLManager;

public class TailsLib extends JavaPlugin {

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading...");

        YAMLManager.setup();
    }
    
}
