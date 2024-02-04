package ru.thisistails.tailslib.CustomItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import ru.thisistails.tailslib.Tools.Config;
import ru.thisistails.tailslib.Tools.Debug;
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
    public void unblockItem(CustomItem item) {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        List<String> blackList = (List<String>) yaml.getList("blacklistedItems");
        blackList.remove(item.getId());
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
        ItemStack itemstack = new ItemStack(item.getMaterial());
        ItemMeta meta = itemstack.getItemMeta();

        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', item.getName())));
        meta.setLore(item.getLore().build());
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, item.getId());
        itemstack.setItemMeta(meta);

        itemstack = item.getImprovedItemStack(itemstack);

        return itemstack;
    }

    public @Nullable CustomItem getItemByID(String id) {
        return items.get(id);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void clicks(PlayerInteractEvent event) {
        
        ItemStack item = event.getItem();

        if (item == null)
            return;

        if (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String itemID = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
            CustomItem citem = items.get(itemID);

            if (citem == null) {
                Debug.error(event.getPlayer(), "Предмет " + itemID + " не зарегистрирован. (Предмет игрока: " + event.getPlayer() + ")");
                return;
            }

            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                citem.leftClick(event);
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                citem.rightClick(event);
                return;
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void damage(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        ItemStack heldItem = damager.getInventory().getItemInMainHand();
        ItemStack offHandHeldItem = damager.getInventory().getItemInOffHand();

        String heldItemID, offHandHeldItemID;

        if (heldItem != null && heldItem.getItemMeta() != null && heldItem.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            heldItemID = heldItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
            CustomItem citem = items.get(heldItemID);
            if (citem == null) {
                Debug.error(damager, "Предмет " + heldItemID + " не зарегистрирован. (Предмет игрока: " + damager.getName() + ")");
            } else {
                citem.itemDamagedEntity(event);
            }
        }

        if (offHandHeldItem != null && offHandHeldItem.getItemMeta() != null && offHandHeldItem.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            offHandHeldItemID = offHandHeldItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
            CustomItem citem = items.get(offHandHeldItemID);
            if (citem == null) {
                Debug.error(damager, "Предмет " + offHandHeldItemID + " не зарегистрирован. (Предмет игрока: " + damager.getName() + ")");
            } else {
                citem.itemDamagedEntity(event);
            }
        }

    }

    

}
