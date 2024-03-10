package ru.thisistails.tailslib.CustomItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import ru.thisistails.tailslib.Tools.Config;
import ru.thisistails.tailslib.Tools.YAMLManager;

public class CustomItemManager {
    
    private @Getter Map<String, CustomItem> items;
    private static CustomItemManager instance;
    private @Getter NamespacedKey customItemKey = new NamespacedKey(Bukkit.getPluginManager().getPlugin("TailsLib"), "customitem");
    
    private @Getter List<CustomItem> blacklistedItems = new ArrayList<>();

    private CustomItemManager() {
        items = new HashMap<>();
    }

    public static CustomItemManager getManager() {
        if (instance == null)
            instance = new CustomItemManager();
        
        return instance;
    }

    public void register(CustomItem item) {
        String id = item.getItemData().getId();

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
    @SuppressWarnings("unchecked")
    public void blockItem(CustomItem item) {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        List<String> blackList = (List<String>) yaml.getList("blacklistedItems");
        blackList.add(item.getItemData().getId());
        yaml.set("blacklistedItems", blackList);
        Config.reloadConfig();
    }

    /**
     * Разблокирует предмет на сервере
     * @param item Предмет
     */
    @SuppressWarnings("unchecked")
    public void unblockItem(CustomItem item) {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        List<String> blackList = (List<String>) yaml.getList("blacklistedItems");
        blackList.remove(item.getItemData().getId());
        yaml.set("blacklistedItems", blackList);
        Config.reloadConfig();
    }

    public boolean isItemBlocked(CustomItem item) {
        return blacklistedItems.contains(item);
    }

    @SuppressWarnings("deprecation")
    /*
     * Papermc далбаёбы и не могут сделать нихуя нормально (:
     * Эта хуйня на Component не хочет делать новые линии
     * и заменяет их ебаным хер пойми чем.
     * 
     * Проще после такого на Spigot перейти и не ебаться.
     */
    public ItemStack createItem(CustomItem item) {
        ItemStack itemstack = new ItemStack(item.getItemData().getMaterial());
        ItemMeta meta = itemstack.getItemMeta();

        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', item.getItemData().getName())));
        meta.setLore(item.getItemData().getLore().build());
        meta.getPersistentDataContainer().set(customItemKey, PersistentDataType.STRING, item.getItemData().getId());
        itemstack.setItemMeta(meta);

        itemstack = item.getImprovedItemStack(itemstack);

        return itemstack;
    }

    public @Nullable CustomItem getItemByID(String id) {
        return items.get(id);
    }

}
