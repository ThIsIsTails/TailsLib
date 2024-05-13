package ru.thisistails.tailslib.Utils;

import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    
    /**
     * Checks if inventory contains all required items.
     * @param required      List with required items
     * @param inventory     Inventory to check
     * @return              Contains all items or not
     */
    public static boolean containsAll(List<ItemStack> required, Inventory inventory) {
        for (ItemStack stack : required) {
            if (!inventory.contains(stack))
                return false;
        }

        return true;
    }

}
