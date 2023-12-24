package ru.thisistails.tailslib.CustomItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import ru.thisistails.tailslib.Tools.Config;
import ru.thisistails.tailslib.Tools.YAMLManager;

public class ItemManager implements Listener {
    
    private @Getter Map<String, CustomItem> items;
    private static ItemManager instance;
    private NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("TailsLib"), "customitem");
    
    private @Getter List<CustomItem> blacklistedItems = new ArrayList<>();

    private ItemManager() {
        items = new HashMap<>();
    }

    public static ItemManager getManager() {
        if (instance == null)
            instance = new ItemManager();
        
        return instance;
    }

    public void register(CustomItem item) {
        String id = item.getId();

        if (isItemBlocked(item)) {
            if (!Config.getConfig().getBoolean("items.loadBlockedItems")) {
                Bukkit.getLogger().warning("[ItemManager] " + "Item with ID: " + id + " in blacklist. Skipping.");
                return;
            } else {
                Bukkit.getLogger().warning("[ItemManager] " + "Item with ID: " + id + " in blacklist but still registering.");
            }
        }

        items.put(id, item);

        Bukkit.getLogger().info("[ItemManager] " + id + " registered.");
    }

    /**
     * Блокирует предмет на сервере.
     * @param item Предмет
     */
    public void blockItem(CustomItem item) {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        List<String> blackList = (List<String>) yaml.getList("blacklistedItems");
        blackList.add(item.getId());
        yaml.set("blacklistedItems", blackList);
        Config.reloadConfig();
    }

    /**
     * Разблокирует предмет на сервере
     * @param item Предмет
     */
    public void unBlockItem(CustomItem item) {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        List<String> blackList = (List<String>) yaml.getList("blacklistedItems");
        blackList.remove(item.getId());
        yaml.set("blacklistedItems", blackList);
        Config.reloadConfig();
    }

    public boolean isItemBlocked(CustomItem item) {
        return blacklistedItems.contains(item);
    }

    public ItemStack createItem(CustomItem item) {
        ItemStack itemstack = new ItemStack(item.getMaterial());
        ItemMeta meta = itemstack.getItemMeta();

        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', item.getName())));
        meta.lore(item.getLore().build());
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, item.getId());
        itemstack.setItemMeta(meta);

        itemstack = item.getImprovedItemStack(itemstack);

        return itemstack;
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void clicks(PlayerInteractEvent event) {
        ItemStack _item = event.getItem();

        if (!event.hasItem()) return;

        for (Map.Entry<String, CustomItem> item : items.entrySet()) {
            if (_item.equals(createItem(item.getValue())) || _item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Bukkit.getPluginManager().getPlugin("TailsLib"), item.getValue().getId()))) {
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    item.getValue().leftClick(event);
                    break;
                } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    item.getValue().rightClick(event);
                    break;
                }

            }

        }

    }

}
