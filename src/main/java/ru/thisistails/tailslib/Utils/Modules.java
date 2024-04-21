package ru.thisistails.tailslib.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import ru.thisistails.tailslib.CustomBlocks.CustomBlockManager;
import ru.thisistails.tailslib.CustomBlocks.PlacedBlocks;
import ru.thisistails.tailslib.Tools.Config;

// Я хуй знает как это работает.
public class Modules implements Listener {

    private @Getter List<String> modulesToLoad;
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("TailsLib");

    public Modules() {
        modulesToLoad = new ArrayList<>();
        YamlConfiguration config = Config.getConfig();
        
        modulesToLoad.addAll(config.getStringList("modules"));

        if (modulesToLoad.isEmpty()) {
            CustomBlockManager.getInstance().loadBlocks();
        }
    }

    public void onPluginLoaded(PluginEnableEvent event) {
        YamlConfiguration config = Config.getConfig();
        
        String name = event.getPlugin().getName();

        if (config.getStringList("modules").contains(name)) {
            modulesToLoad.remove(name);
        }

        if (modulesToLoad.isEmpty()) {
            Bukkit.getLogger().info("All modules loaded, start loading...");
            CustomBlockManager.getInstance().loadBlocks();
        }
    }

    @EventHandler
    public void serverLoadEvent(ServerLoadEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (modulesToLoad.isEmpty()) return;
    
            Bukkit.getLogger().info("Something went wrong in loading modules.");
            Bukkit.getLogger().info("Loading tailslib without those modules: " + modulesToLoad.toString());
            Bukkit.getLogger().info("Backup of placedBlocks.json will be created.");
    
            File backupFile = new File(plugin.getDataFolder() + "/backup-placedBlocks.json.bak");
            try {
                FileUtils.copyFile(new File(PlacedBlocks.getSaveFilePath()), backupFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomBlockManager.getInstance().loadBlocks();
        }, 10 * 20);
    }
    
}
