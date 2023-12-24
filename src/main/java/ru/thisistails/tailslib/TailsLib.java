package ru.thisistails.tailslib;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ru.thisistails.tailslib.Commands.ListItems;
import ru.thisistails.tailslib.CustomItems.ItemManager;
import ru.thisistails.tailslib.Items.SimpleItem;
import ru.thisistails.tailslib.Tools.Config;
import ru.thisistails.tailslib.Tools.YAMLManager;

public class TailsLib extends JavaPlugin {

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading...");

        YAMLManager.setup();
        Config.reloadConfig();

        ItemManager itemManager = ItemManager.getManager();
        itemManager.register(new SimpleItem());

        getCommand("listitems").setExecutor(new ListItems());
    }
    
}
