package ru.thisistails.tailslib.CustomItems;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface CustomItem {
    
    /**
     * Айди которое использует сервер и другие плагины.
     */
    public String getId();
    /**
     * Отображаемое имя для игрока.
     */
    public String getName();
    /**
     * Описание предмета
     */
    public IDescBuilder getLore();
    /**
     * Тип предмета
     */
    public Material getMaterial();

    /**
     * Используется для настройки предмета майнкрафта.
     * @param current   Предмет.
     * @apiNote         Предмет уже имеет встроенный ID, имя и лор.
     */
    public default ItemStack getImprovedItemStack(ItemStack current) { return current; }

    public default void leftClick(PlayerInteractEvent event) {}
    public default void rightClick(PlayerInteractEvent event) {}

}
