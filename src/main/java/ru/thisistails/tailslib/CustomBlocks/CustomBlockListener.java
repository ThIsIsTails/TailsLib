package ru.thisistails.tailslib.CustomBlocks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import ru.thisistails.tailslib.CustomBlocks.Data.PlacedBlockData;
import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.CustomItemManager;

public class CustomBlockListener implements Listener {

    private static CustomBlockListener listener;

    public static CustomBlockListener getListener() {
        if (listener == null) listener = new CustomBlockListener();

        return listener;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        CustomItem item = CustomItemManager.tryGetCItemFromItemStack(event.getItemInHand());
        if (item == null) {
            return;
        }

        CustomBlock block = CustomBlockManager.getInstance().getCustomBlockByCustomItem(item);

        if (block == null)
            return;
        
        Location location = event.getBlockPlaced().getLocation();

        CustomBlockManager.getInstance().placeBlock(block, location, player.getUniqueId());
        block.onCreation(event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDestroyed(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlacedBlockData placedBlockData = CustomBlockManager.getInstance().getPlacedBlocks().searchPlacedBlockData(event.getBlock().getLocation());

        if (placedBlockData == null) return;

        CustomItem citem = placedBlockData.getPlacedBlock().getData().getItem();
        event.setDropItems(false);
        if (placedBlockData.getPlacedBlock().getData().isDropItem()) {
            player.getInventory().addItem(CustomItemManager.getManager().createItem(citem));
        }

        CustomBlockManager.getInstance().removePlacedBlock(placedBlockData);
        placedBlockData.getPlacedBlock().onDestroy(event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRightClickOnBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            PlacedBlockData data = CustomBlockManager.getInstance().getPlacedBlocks().searchPlacedBlockData(event.getClickedBlock().getLocation());

            if (data == null) return;

            if (event.getHand().equals(EquipmentSlot.HAND) && event.getItem() != null)
                data.getPlacedBlock().onRightClickOnBlockWithItem(player, event.getItem());
            else
                data.getPlacedBlock().onRightClearClickOnBlock(player);
        }
    }
}
