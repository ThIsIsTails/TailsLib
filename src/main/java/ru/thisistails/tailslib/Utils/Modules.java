package ru.thisistails.tailslib.Utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

import ru.thisistails.tailslib.CustomBlocks.CustomBlockManager;
import ru.thisistails.tailslib.CustomBlocks.PlacedBlocks;

// Я хуй знает как это работает.
/**
 * @deprecated Buggy and messy. Will be replaced with new system.
 */
@Deprecated(forRemoval = true)
public class Modules implements Listener {

    private Plugin plugin = Bukkit.getPluginManager().getPlugin("TailsLib");

    @EventHandler
    public void serverLoadEvent(ServerLoadEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
    
            Bukkit.getLogger().info("Creating backup of placed blocks.");
    
            File backupFile = new File(plugin.getDataFolder() + "/backup-placedBlocks.json.bak");
            try {
                FileUtils.copyFile(new File(PlacedBlocks.getSaveFilePath()), backupFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CustomBlockManager.getInstance().loadBlocks();
        }, 2 * 20);
    }
    
}
