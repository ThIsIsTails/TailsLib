package ru.thisistails.tailslib.CustomItems;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import ru.thisistails.tailslib.Localization;
import ru.thisistails.tailslib.Tools.Debug;

public class CustomItemListener implements Listener {

    private static final CustomItemManager manager = CustomItemManager.getManager();

    public CustomItemListener() {}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(PrepareItemEnchantEvent event) {
        CustomItem citem = CustomItemManager.tryGetCItemFromItemStack(event.getItem());

        if (citem == null) return;

        if (citem.getItemData().getFlags().contains(CustomItemFlag.NoEchants))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void clicks(PlayerInteractEvent event) {
        
        ItemStack item = event.getItem();
        Action action = event.getAction();

        if (item == null)
            return;

        ItemMeta meta = item.getItemMeta();
        // Проверяем предмет на ключ и берём ID если есть
        if (meta != null && meta.getPersistentDataContainer().has(manager.getCustomItemKey(), PersistentDataType.STRING)) {
            String itemID = item.getItemMeta().getPersistentDataContainer().get(manager.getCustomItemKey(), PersistentDataType.STRING);
            CustomItem citem = manager.getItems().get(itemID);

            if (citem == null) {
                // Пишем, что предмет использует TailsLib, но он не зарегистрирован в нём.
                Debug.error(event.getPlayer(), Localization.prefix + " " + Localization.itemNotRegistered
                    .replace("%player%", event.getPlayer().getName())
                    .replace("%customitem_id%", itemID)
                );
                return;
            }

            // Получаем флаги после того как уверены в том что предмет вообще есть.
            List<CustomItemFlag> flags = citem.getItemData().getFlags();

            if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                if (flags.contains(CustomItemFlag.DisableActions)) {
                    event.setCancelled(true);
                }
                citem.leftClick(event, CustomItemManager.getUUIDFromCustomItem(item));
                return;
            }

            if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                if (flags.contains(CustomItemFlag.DisableActions)) {
                    event.setCancelled(true);
                }
                citem.rightClick(event, CustomItemManager.getUUIDFromCustomItem(item));
                return;
            }
        }

    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        // Проверка на "уникальные" материалы
        for (ItemStack ingredient : event.getInventory().getMatrix()) {
            if (ingredient == null) continue;
            CustomItem citem = CustomItemManager.tryGetCItemFromItemStack(ingredient);
            if (citem == null) continue;
            if (citem != null && citem.getItemData().isAsUniqueMaterial()) {
                // Если предмет содержит запрещенный материал, отменяем событие крафта
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void checkItem(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        CustomItem item = CustomItemManager.tryGetCItemFromItemStack(event.getCurrentItem());
        if (item == null) return;

        if (CustomItemManager.getUUIDFromCustomItem(event.getCurrentItem()) == null && item.getItemData().isShouldBeUnique()) {
            ItemStack stack = CustomItemManager.getManager().putUUID(event.getCurrentItem());
            event.setCurrentItem(stack);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void damage(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        ItemStack heldItem = damager.getInventory().getItemInMainHand();
        ItemStack offHandHeldItem = damager.getInventory().getItemInOffHand();

        String heldItemID, offHandHeldItemID;

        // Проверяем основную руку на кастомный предмет и выполняем с ней действия
        if (heldItem != null && heldItem.getItemMeta() != null && heldItem.getItemMeta().getPersistentDataContainer().has(manager.getCustomItemKey(), PersistentDataType.STRING)) {
            heldItemID = heldItem.getItemMeta().getPersistentDataContainer().get(manager.getCustomItemKey(), PersistentDataType.STRING);
            CustomItem citem = manager.getItems().get(heldItemID);
            if (citem == null) {
                Debug.error(damager, Localization.prefix + " " + Localization.itemNotRegistered
                    .replace("%player%", damager.getName())
                    .replace("%customitem_id%", heldItemID)
                );
            } else {
                List<CustomItemFlag> flags = citem.getItemData().getFlags();
                if (flags.contains(CustomItemFlag.DisableActions)) {
                    event.setCancelled(true);
                    return;
                }

                if (flags.contains(CustomItemFlag.NoPVP) && event.getEntity() instanceof Player) {
                    event.setCancelled(true);
                    return;
                }

                citem.itemDamagedEntity(event, CustomItemManager.getUUIDFromCustomItem(heldItem));
            }
        }

        // Тоже самое для вторичной руки
        if (offHandHeldItem != null && offHandHeldItem.getItemMeta() != null && offHandHeldItem.getItemMeta().getPersistentDataContainer().has(manager.getCustomItemKey(), PersistentDataType.STRING)) {
            offHandHeldItemID = offHandHeldItem.getItemMeta().getPersistentDataContainer().get(manager.getCustomItemKey(), PersistentDataType.STRING);
            CustomItem citem = manager.getItems().get(offHandHeldItemID);
            if (citem == null) {
                Debug.error(damager, Localization.prefix + " " + Localization.itemNotRegistered
                    .replace("%player%", damager.getName())
                    .replace("%customitem_id%", offHandHeldItemID)
                );
            } else {
                List<CustomItemFlag> flags = citem.getItemData().getFlags();
                if (flags.contains(CustomItemFlag.DisableActions)) {
                    event.setCancelled(true);
                    return;
                }

                if (flags.contains(CustomItemFlag.NoPVP) && event.getEntity() instanceof Player) {
                    event.setCancelled(true);
                    return;
                }
                
                citem.itemDamagedEntity(event, CustomItemManager.getUUIDFromCustomItem(offHandHeldItem));
            }
        }

    }

}
