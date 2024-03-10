package ru.thisistails.tailslib;

import org.bukkit.plugin.java.JavaPlugin;

import ru.thisistails.tailslib.Commands.GiveCItem;
import ru.thisistails.tailslib.Commands.ListItems;
import ru.thisistails.tailslib.Commands.SettingsCommand;
import ru.thisistails.tailslib.CustomItems.CustomItemListener;
import ru.thisistails.tailslib.CustomItems.CustomItemManager;
import ru.thisistails.tailslib.CustomItems.Tests.SimpleItem;
import ru.thisistails.tailslib.Tools.Config;

public class TailsLib extends JavaPlugin {

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        Config.reloadConfig();

        CustomItemManager itemManager = CustomItemManager.getManager();
        getServer().getPluginManager().registerEvents(CustomItemListener.getListener(), this);

        if (Config.getConfig().getBoolean("items.registerTestItems")) {
            itemManager.register(new SimpleItem());
        }

        getCommand("listitems").setExecutor(new ListItems());
        getCommand("citem").setExecutor(new GiveCItem());
        getCommand("citem").setTabCompleter(new GiveCItem());

        getCommand("tailslib").setExecutor(new SettingsCommand());
        getCommand("tailslib").setTabCompleter(new SettingsCommand());
    }
    
}
