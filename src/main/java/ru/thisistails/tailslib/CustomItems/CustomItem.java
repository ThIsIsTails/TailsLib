package ru.thisistails.tailslib.CustomItems;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CustomItem {

    public boolean isHolding = false;
    
    /**
     * Айди которое использует сервер и другие плагины.
     */
    public @NotNull String getId();
    /**
     * Отображаемое имя для игрока.
     */
    public default String getName() { return "Default name for CustomItem.java"; }
    /**
     * Описание предмета
     */
    public default IDescBuilder getLore() { return new SimpleDescBuilder().addDesc("Just an item."); }
    /**
     * Тип предмета
     */
    public @NotNull Material getMaterial();

    /**
     * Используется для настройки предмета майнкрафта.
     * @param current   Предмет.
     * @apiNote         Предмет уже имеет встроенный ID, имя и лор.
     */
    public default @NotNull ItemStack getImprovedItemStack(ItemStack current) { return current; }

    public default void leftClick(PlayerInteractEvent event) {}
    public default void rightClick(PlayerInteractEvent event) {}
    public default void itemDamagedEntity(EntityDamageByEntityEvent event) {}
    
    // public default void onHoldEvent(PlayerInventorySlotChangeEvent event) {}
    // public default void onHoldEvent(Player player) {}

}
