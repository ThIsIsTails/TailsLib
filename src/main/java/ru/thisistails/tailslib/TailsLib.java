package ru.thisistails.tailslib;

import org.bukkit.plugin.java.JavaPlugin;

import ru.thisistails.tailslib.Commands.GiveCItem;
import ru.thisistails.tailslib.Commands.ListItems;
import ru.thisistails.tailslib.Commands.SettingsCommand;
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
        YAMLManager.setup();
        Config.reloadConfig();

        ItemManager itemManager = ItemManager.getManager();
        getServer().getPluginManager().registerEvents(itemManager, this);

        if (Config.getConfig().getBoolean("items.registerTestItems")) {
            itemManager.register(new SimpleItem());
        }

        getCommand("listitems").setExecutor(new ListItems());
        getCommand("citem").setExecutor(new GiveCItem());

        getCommand("tailslib").setExecutor(new SettingsCommand());
        getCommand("tailslib").setTabCompleter(new SettingsCommand());
    }
    
}
