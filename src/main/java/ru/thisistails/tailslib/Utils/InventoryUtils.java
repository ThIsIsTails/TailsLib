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
        // Создаем копию списка требуемых предметов
        List<ItemStack> check = List.copyOf(required);

        // Проверяем каждый слот инвентаря
        for (ItemStack stack : inventory.getContents()) {
            if (stack == null) continue; // Пропускаем пустые слоты
            // Ищем соответствующий предмет из списка required
            for (int i = 0; i < check.size(); i++) {
                ItemStack requiredItem = check.get(i);
                if (stack.isSimilar(requiredItem) && stack.getAmount() >= requiredItem.getAmount()) {
                    // Удаляем найденный предмет из списка check
                    check.remove(i);
                    break;
                }
            }
        }

        // Если список check пуст, значит все предметы найдены
        return check.isEmpty();
    }

    /**
     * Will remove all items from inventory if all required items exist.
     * <p>
     * This method does not completely remove items from the player's inventory.
     * You can specify the quantity of each item in the list so that this method removes only a certain amount of each item in the list.
     * </p>
     * @param toRemove  To remove list
     * @return          Success
     */
    public static boolean removeAll(List<ItemStack> toRemove, Inventory inventory) {
        // Проверяем, содержатся ли все предметы из списка toRemove в инвентаре
        if (!containsAll(toRemove, inventory)) return false;

        // Удаляем указанное количество каждого предмета из инвентаря
        for (ItemStack removeItem : toRemove) {
            // Проходим по всем слотам инвентаря
            for (ItemStack invItem : inventory.getContents()) {
                // Если текущий слот соответствует предмету для удаления
                if (invItem != null && invItem.isSimilar(removeItem)) {
                    // Уменьшаем количество предмета в слоте
                    int remainingAmount = invItem.getAmount() - removeItem.getAmount();
                    if (remainingAmount <= 0) {
                        // Удаляем весь стак, если остаток равен или меньше 0
                        inventory.remove(invItem);
                        break;
                    } else {
                        // Обновляем количество предмета в слоте
                        invItem.setAmount(remainingAmount);
                        break;
                    }
                }
            }
        }

        return true;
    }

}
