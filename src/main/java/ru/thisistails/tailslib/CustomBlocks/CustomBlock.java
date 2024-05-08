package ru.thisistails.tailslib.CustomBlocks;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;

import ru.thisistails.tailslib.CustomBlocks.Data.CustomBlockData;

public interface CustomBlock {

    public @NotNull CustomBlockData getData();

    public default void onCreation(BlockPlaceEvent event) {}
    public default void onDestroy(BlockBreakEvent event) {}

    /**
     * При клике на блок без предметов.
     * @param player    Автор клика
     */
    public default void onRightClearClickOnBlock(Player player) {}
    /**
     * При клике на блок с предметом
     * @param player    Автор клика
     */
    public default void onRightClickOnBlockWithItem(Player player, ItemStack item) {}

    /**
     * Вызывается при сохранении сервера.
     * @return  Данные которые нужно сохранить в JsonElement
     */
    public default JsonElement onSave() { return null; }

    /**
     * Вызывается при загрузке сервера.
     * @param element   Элемент Json с данными.
     */
    public default void onLoad(@NotNull JsonElement element) {}

}
