package ru.thisistails.tailslib;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ru.thisistails.tailslib.Commands.EffectCommand;
import ru.thisistails.tailslib.Commands.GiveCItem;
import ru.thisistails.tailslib.Commands.ListItems;
import ru.thisistails.tailslib.Commands.PBlocksCommand;
import ru.thisistails.tailslib.Commands.SettingsCommand;
import ru.thisistails.tailslib.CustomBlocks.CustomBlockListener;
import ru.thisistails.tailslib.CustomBlocks.CustomBlockManager;
import ru.thisistails.tailslib.CustomBlocks.Tests.TestBlock;
import ru.thisistails.tailslib.CustomEffects.CustomEffectListener;
import ru.thisistails.tailslib.CustomEffects.CustomEffectManager;
import ru.thisistails.tailslib.CustomEffects.EffectInterruptionReason;
import ru.thisistails.tailslib.CustomEffects.Tests.SayHelloEffect;
import ru.thisistails.tailslib.CustomItems.CustomItemListener;
import ru.thisistails.tailslib.CustomItems.CustomItemManager;
import ru.thisistails.tailslib.CustomItems.Tests.SimpleCustomBlockItem;
import ru.thisistails.tailslib.CustomItems.Tests.SimpleItem;
import ru.thisistails.tailslib.PlaceholderAPI.EffectDuration;
import ru.thisistails.tailslib.PlaceholderAPI.EffectIDGetter;
import ru.thisistails.tailslib.PlaceholderAPI.EffectsFormattedPlaceholder;
import ru.thisistails.tailslib.PlaceholderAPI.EffectsRawPlaceholder;
import ru.thisistails.tailslib.Tools.Config;
import ru.thisistails.tailslib.Utils.Modules;

public class TailsLib extends JavaPlugin {

    @Override
    public void onDisable() {
        CustomBlockManager.getInstance().savePlacedBlocksDatas();
        // Предупредить эффекты перед выключением.
        CustomEffectManager.getInstance().getAppliedEffects()
            .forEach((effect) -> {
                CustomEffectManager.getInstance().interruptEffect(effect.getPlayer(), effect.getEffect(), EffectInterruptionReason.LibDisabling);
            });
    }

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 22145);
        Config.reloadConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().info("<--------------------------------->");
            getLogger().info("TailsLib supports PlaceholderAPI.");
            getLogger().info("You should install PlaceholderAPI for using placeholders in other plugins that not use TailsLib.");
            getLogger().info("<--------------------------------->");
        }
        else {
            new EffectDuration().register();
            new EffectIDGetter().register();
            new EffectsFormattedPlaceholder().register();
            new EffectsRawPlaceholder().register();
            getLogger().info("Placeholders registered.");
        }

        CustomItemManager itemManager = CustomItemManager.getManager();
        CustomBlockManager blockManager = CustomBlockManager.getInstance();
        CustomEffectManager effectManager = CustomEffectManager.getInstance();
        Modules modules = new Modules();
        getServer().getPluginManager().registerEvents(new CustomItemListener(), this);
        getServer().getPluginManager().registerEvents(CustomBlockListener.getListener(), this);
        getServer().getPluginManager().registerEvents(CustomEffectListener.getListener(), this);
        getLogger().info("TailsLib is not supporting saving for custom effect right now.");
        getLogger().info("It will be introduced in newer versions.");
        getLogger().info("Custom listeners registered.");
        
        getLogger().info("Trying to register test things.");
        if (Config.getConfig().getBoolean("tests.registerTests")) {
            itemManager.register(new SimpleItem());
            itemManager.register(new SimpleCustomBlockItem());
            
            blockManager.register(new TestBlock());

            effectManager.register(new SayHelloEffect());
            getLogger().info("Test things registered.");
        } else
            getLogger().info("tests.registerTests returned false. Skipping...");
        
        
        getCommand("listitems").setExecutor(new ListItems());
        getCommand("citem").setExecutor(new GiveCItem());
        getCommand("citem").setTabCompleter(new GiveCItem());
        
        getCommand("tailslib").setExecutor(new SettingsCommand());
        getCommand("tailslib").setTabCompleter(new SettingsCommand());
        
        getCommand("cblockplaced").setExecutor(new PBlocksCommand());
        getCommand("customeffect").setExecutor(new EffectCommand());
        getCommand("customeffect").setTabCompleter(new EffectCommand());
        getLogger().info("Commands registered.");

        getLogger().info("Registering deprecated listener \"Modules\".");
        getLogger().info("It will be deleted in next versions.");
        getLogger().info("Change config.waitBeforeLoadBlocks to correct Modules class if something gone wrong with block registering.");
        getServer().getPluginManager().registerEvents(modules, this);
        getLogger().info("All seems good! Waiting for block manager decision.");
    }
    
}
