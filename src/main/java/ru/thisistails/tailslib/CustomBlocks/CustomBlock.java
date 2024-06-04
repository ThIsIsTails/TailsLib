package ru.thisistails.tailslib.CustomBlocks;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;

import ru.thisistails.tailslib.CustomBlocks.Data.CustomBlockData;
import ru.thisistails.tailslib.CustomBlocks.Data.PlacedBlockData;

public interface CustomBlock {

    public @NotNull CustomBlockData getData();

    /**
     * Calls when player create new custom block.
     * @param event     Block place event for more control
     * @apiNote {@link BlockPlaceEvent#setCancelled(boolean)} will not help you. It will just break your block but this still will be loaded as a {@link PlacedBlockData} in manager.
     * So just don't touch that, i may add this feature later.
     */
    public default void onCreation(BlockPlaceEvent event) {}
    /**
     * Calls when player wants to destroy this block
     * @param event Block break event for more control
     * @apiNote {@link BlockBreakEvent#setCancelled(boolean)} will not help you. This action can't be cancelled (I may add this later).
     * <p>This also means {@link BlockBreakEvent#setDropItems(boolean)} may make a new dupe of item that marked as a material for your block.</p>
     */
    public default void onDestroy(BlockBreakEvent event) {}

    /**
     * When player clicks on block without item.
     * @param player    Author of this click
     */
    public default void onRightClearClickOnBlock(Player player) {}
    /**
     * When player clicks on block with item.
     * @param player    Author of this click
     */
    public default void onRightClickOnBlockWithItem(Player player, ItemStack item) {}

    /**
     * Calls when TailsLib is saving all blocks data.
     * @return  Data which needs to be saved.
     * @apiNote It will not call {@link #onLoad(JsonElement)} on load if this function returns null
     */
    public default JsonElement onSave() { return null; }

    /**
     * Calls when TailsLib is loading all blocks data.
     * @param element   Json element with saved data.
     * @apiNote It will not fire if {@link #onSave()} returns null.
     */
    public default void onLoad(@NotNull JsonElement element) {}

}
