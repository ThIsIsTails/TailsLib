package ru.thisistails.tailslib.CustomItems;

import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.Data.CustomItemData;

public interface CustomItem {
    
    public @NotNull CustomItemData getItemData();

    /**
     * Используется для настройки предмета майнкрафта.
     * @param current   Предмет.
     * @apiNote         Предмет уже имеет встроенный ID, имя и лор.
     */
    public default @NotNull ItemStack getImprovedItemStack(ItemStack current) { return current; }

    public default void leftClick(PlayerInteractEvent event) {}
    public default void rightClick(PlayerInteractEvent event) {}
    public default void itemDamagedEntity(EntityDamageByEntityEvent event) {}

    public default Recipe recipe(NamespacedKey key, ItemStack item) { return null; }
    
    // public default void onHoldEvent(PlayerInventorySlotChangeEvent event) {}
    // public default void onHoldEvent(Player player) {}

}
