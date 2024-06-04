package ru.thisistails.tailslib.CustomItems;

import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.thisistails.tailslib.CustomItems.Data.CustomItemData;
import ru.thisistails.tailslib.Exceptions.ItemRecipeException;

public interface CustomItem {
    
    public @NotNull CustomItemData getItemData();

    /**
     * Use it only for setting attributes and etc.
     * <p>For custom model use {@link #customModelID(int)}</p>
     * @param current   Предмет.
     * @apiNote         Предмет уже имеет встроенный ID, имя и лор.
     */
    public default @NotNull ItemStack getImprovedItemStack(@NotNull ItemStack current) { return current; }

    /**
     * Indentifies that u want use your own model for this item.
     * <p>You need to make your own texturepack for this function or else nothing will happen.</p>
     * @return Custom model ID for client.
     */
    public default int customModelID() { return 0; }

    /**
     * Left click event for your item. It will not call if {@link #itemDamagedEntity(EntityDamageByEntityEvent, UUID)} is called.
     * @param event Event
     * @param uuid  Item UUID if {@link CustomItemData#isShouldBeUnique()} is true or else it will be null.
     */
    public default void leftClick(@NotNull PlayerInteractEvent event, @Nullable UUID uuid) {}
    /**
     * Right click event for your item.
     * @param event Event
     * @param uuid Item UUID if {@link CustomItemData#isShouldBeUnique()} is true or else it will be null.
     */
    public default void rightClick(@NotNull PlayerInteractEvent event, @Nullable UUID uuid) {}
    public default void itemDamagedEntity(@NotNull EntityDamageByEntityEvent event, @Nullable UUID uuid) {}

    /**
     * Recipe for your item.
     * @param key   NamespacedKey for recipe constructor
     * @param item  Itemstack of your item for recipe constructor.
     * @return  Recipe
     * @apiNote Use item and key in constructon or else manager will throw {@link ItemRecipeException}.
     * @implNote Use {@link CustomItemManager#getExactChoiceFromID(String)} or {@link CustomItemManager#getExactChoiceFromItem(CustomItem)} to get {@link ExactChoice}.
     */
    public default @NotNull Recipe recipe(NamespacedKey key, ItemStack item) { return null; }
    
    // /**
    //  * Will be called every second until player change slot to another item.
    //  * @param player    Player that holding item.
    //  * @param hand      Hand
    //  */
    // public default void holdEvent(Player player, EquipmentSlot hand) {}

}
