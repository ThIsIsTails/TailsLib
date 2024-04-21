package ru.thisistails.tailslib;

import org.bukkit.plugin.java.JavaPlugin;

import ru.thisistails.tailslib.Commands.GiveCItem;
import ru.thisistails.tailslib.Commands.ListItems;
import ru.thisistails.tailslib.Commands.PBlocksCommand;
import ru.thisistails.tailslib.Commands.SettingsCommand;
import ru.thisistails.tailslib.CustomBlocks.CustomBlockListener;
import ru.thisistails.tailslib.CustomBlocks.CustomBlockManager;
import ru.thisistails.tailslib.CustomBlocks.Tests.TestBlock;
import ru.thisistails.tailslib.CustomItems.CustomItemListener;
import ru.thisistails.tailslib.CustomItems.CustomItemManager;
import ru.thisistails.tailslib.CustomItems.Tests.SimpleCustomBlockItem;
import ru.thisistails.tailslib.CustomItems.Tests.SimpleItem;
import ru.thisistails.tailslib.Tools.Config;
import ru.thisistails.tailslib.Utils.Modules;

public class TailsLib extends JavaPlugin {

    @Override
    public void onDisable() {
        CustomBlockManager.getInstance().savePlacedBlocksDatas();
    }

    @Override
    public void onEnable() {
        Config.reloadConfig();

        CustomItemManager itemManager = CustomItemManager.getManager();
        CustomBlockManager blockManager = CustomBlockManager.getInstance();
        Modules modules = new Modules();
        getServer().getPluginManager().registerEvents(CustomItemListener.getListener(), this);
        getServer().getPluginManager().registerEvents(CustomBlockListener.getListener(), this);
        
        if (Config.getConfig().getBoolean("items.registerTestItems")) {
            itemManager.register(new SimpleItem());
            itemManager.register(new SimpleCustomBlockItem());
            
            blockManager.register(new TestBlock());
        }
        
        
        getCommand("listitems").setExecutor(new ListItems());
        getCommand("citem").setExecutor(new GiveCItem());
        getCommand("citem").setTabCompleter(new GiveCItem());
        
        getCommand("tailslib").setExecutor(new SettingsCommand());
        getCommand("tailslib").setTabCompleter(new SettingsCommand());
        
        getCommand("cblockplaced").setExecutor(new PBlocksCommand());

        getServer().getPluginManager().registerEvents(modules, this);
    }
    
}
