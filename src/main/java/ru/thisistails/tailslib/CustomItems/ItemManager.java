package ru.thisistails.tailslib.CustomItems;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class ItemManager implements Listener {
    
    private Map<String, CustomItem> items;
    private static ItemManager instance;
    private NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("TailsLib"), "customitem");

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

        items.put(id, item);

        Bukkit.getLogger().info(id + " registered.");
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

}
