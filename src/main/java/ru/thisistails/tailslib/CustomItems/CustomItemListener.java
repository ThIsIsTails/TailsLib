package ru.thisistails.tailslib.CustomItems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import ru.thisistails.tailslib.Tools.Debug;

public class CustomItemListener implements Listener {

    private static final CustomItemManager manager = CustomItemManager.getManager();

    private static CustomItemListener listener;

    private CustomItemListener() {}

    public static CustomItemListener getListener() {
        if (listener == null) listener = new CustomItemListener();

        return listener;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void clicks(PlayerInteractEvent event) {
        
        ItemStack item = event.getItem();

        if (item == null)
            return;

        if (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(manager.getCustomItemKey(), PersistentDataType.STRING)) {
            String itemID = item.getItemMeta().getPersistentDataContainer().get(manager.getCustomItemKey(), PersistentDataType.STRING);
            CustomItem citem = manager.getItems().get(itemID);

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

        if (heldItem != null && heldItem.getItemMeta() != null && heldItem.getItemMeta().getPersistentDataContainer().has(manager.getCustomItemKey(), PersistentDataType.STRING)) {
            heldItemID = heldItem.getItemMeta().getPersistentDataContainer().get(manager.getCustomItemKey(), PersistentDataType.STRING);
            CustomItem citem = manager.getItems().get(heldItemID);
            if (citem == null) {
                Debug.error(damager, "Предмет " + heldItemID + " не зарегистрирован. (Предмет игрока: " + damager.getName() + ")");
            } else {
                citem.itemDamagedEntity(event);
            }
        }

        if (offHandHeldItem != null && offHandHeldItem.getItemMeta() != null && offHandHeldItem.getItemMeta().getPersistentDataContainer().has(manager.getCustomItemKey(), PersistentDataType.STRING)) {
            offHandHeldItemID = offHandHeldItem.getItemMeta().getPersistentDataContainer().get(manager.getCustomItemKey(), PersistentDataType.STRING);
            CustomItem citem = manager.getItems().get(offHandHeldItemID);
            if (citem == null) {
                Debug.error(damager, "Предмет " + offHandHeldItemID + " не зарегистрирован. (Предмет игрока: " + damager.getName() + ")");
            } else {
                citem.itemDamagedEntity(event);
            }
        }

    }
    
}
