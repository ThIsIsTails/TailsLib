package ru.thisistails.tailslib.CustomItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import ru.thisistails.tailslib.Exceptions.IDPatternException;
import ru.thisistails.tailslib.Exceptions.ItemRecipeException;
import ru.thisistails.tailslib.Tools.Config;
import ru.thisistails.tailslib.Tools.YAMLManager;

public class CustomItemManager {
    
    private @Getter Map<String, CustomItem> items;
    private static CustomItemManager instance;
    private @Getter NamespacedKey customItemKey = new NamespacedKey(Bukkit.getPluginManager().getPlugin("TailsLib"), "customitem");
    private @Getter NamespacedKey uuidKey = new NamespacedKey(Bukkit.getPluginManager().getPlugin("TailsLib"), "customitemuuid");

    private Pattern idPattern = Pattern.compile("^[a-z_]+$");
    
    private @Getter List<CustomItem> blacklistedItems = new ArrayList<>();

    private CustomItemManager() {
        items = new HashMap<>();
    }

    public static CustomItemManager getManager() {
        if (instance == null)
            instance = new CustomItemManager();
        
        return instance;
    }

    public static @Nullable UUID getUUIDFromCustomItem(ItemStack stack) {
        CustomItem citem = tryGetCItemFromItemStack(stack);
        if (citem == null || stack.getItemMeta() == null) return null;
        UUID uuid = null;

        try {
            uuid = UUID.fromString(stack.getItemMeta().getPersistentDataContainer().get(getManager().getUuidKey(), PersistentDataType.STRING));
        } catch (IllegalArgumentException | NullPointerException e) {}

        return uuid ;
    }

    protected ItemStack putUUID(ItemStack stack) {
        ItemStack item = stack.clone();
        if (item.getItemMeta() == null) return null;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(uuidKey, PersistentDataType.STRING, UUID.randomUUID().toString());

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Tries to retrieve the identifier (ID) of a custom item from its ItemStack.
     * @param item The ItemStack from which the ID is to be retrieved.
     * @return The identifier (ID) of the custom item, or null if the ID is not found or the ItemStack does not contain metadata.
     */
    public static String tryGetIDFromItemStack(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return null;
        
        String id = item.getItemMeta().getPersistentDataContainer().get(getManager().getCustomItemKey(), PersistentDataType.STRING);
        return id;
    }

    /**
     * Tries to retrieve a custom item (CustomItem) from an ItemStack.
     * @param item The ItemStack for which to retrieve the CustomItem.
     * @return The CustomItem corresponding to the ItemStack, or null if the item is not custom.
     */
    public static CustomItem tryGetCItemFromItemStack(@NotNull ItemStack item) {
        return CustomItemManager.getItemByID(tryGetIDFromItemStack(item));
    }

    /**
     * Gets the exact choice (ExactChoice) from the ID of a custom item.
     * @param id The identifier (ID) of the custom item.
     * @return The ExactChoice created from the custom item with the specified ID, or null if the item is not found.
     */
    public static @Nullable ExactChoice getExactChoiceFromID(@NotNull String id) {
        CustomItem item = getItemByID(id);
        if (item == null) return null;

        return new ExactChoice(createItem(item));
    }

    /**
     * Gets the exact choice (ExactChoice) from a custom item.
     * @param item The CustomItem for which to create the ExactChoice.
     * @return The ExactChoice created from the specified custom item, or null if the item is not found.
     */
    public static @Nullable ExactChoice getExactChoiceFromItem(@NotNull CustomItem item) {
        return getExactChoiceFromID(item.getItemData().getId());
    }

    /**
     * Creates an ItemStack from the ID of a custom item.
     * @param id The identifier (ID) of the custom item.
     * @return The ItemStack created from the custom item with the specified ID, or null if the item is not found.
     */
    public static @Nullable ItemStack createItemFromID(@NotNull String id) {
        if (getItemByID(id) == null) return null;

        return CustomItemManager.createItem(getItemByID(id));
    }

    /**
     * Registers your custom item.
     * @param item  Custom item to register
     * @apiNote If your items, blocks, or effects use your item in calls, make sure you register that item first before others.
     * @throws ItemRecipeException If something does not match with version given be the manager.
     * @throws IDPatternException If id pattern is wrong.
     */
    public void register(@NotNull CustomItem item) {
        String id = item.getItemData().getId();

        Matcher matcher = idPattern.matcher(id);

        if (!matcher.find())
            throw new IDPatternException(id + " is not matching pattern " + idPattern.pattern());

        if (isItemBlocked(item)) {
            if (!Config.getConfig().getBoolean("items.loadBlockedItems")) {
                Bukkit.getLogger().warning("[ItemManager] " + "Item with ID: " + id + " in blacklist. Skipping.");
                return;
            } else {
                Bukkit.getLogger().warning("[ItemManager] " + "Item with ID: " + id + " in blacklist but still registering.");
            }
        }

        Bukkit.getLogger().info("Adding recipe for " + id);
        NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("TailsLib"), id + "_recipe");
        ItemStack itemstack = createRecipeItem(item);
        Recipe recipe = item.recipe(key, itemstack);
        if (recipe != null) {
            if (!recipe.getResult().equals(itemstack))
                throw new ItemRecipeException("The item recipe is provided, but the item that should be the result does not match the original version given when the method is called. Item will not be registered.");
            if (Bukkit.addRecipe(recipe)) {
                Bukkit.getLogger().info("Added recipe for " + id + ".");
                if (Bukkit.getRecipe(key) == null) throw new ItemRecipeException("The item recipe is provided, but its key does not match the key given by the manager. Item will not be registered.");
            }
            else
                Bukkit.getLogger().severe("Failed to add recipe for " + id + " for some reason.");
        }
        else
            Bukkit.getLogger().info(id + " returned null as a recipe, skipping.");

        items.put(id, item);

        Bukkit.getLogger().info("[ItemManager] " + id + " registered.");
    }

    /**
     * Blocks item from the server.
     * @param item Item to block
     */
    @SuppressWarnings("unchecked")
    public void blockItem(@NotNull CustomItem item) {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        List<String> blackList = (List<String>) yaml.getList("blacklistedItems");
        blackList.add(item.getItemData().getId());
        yaml.set("blacklistedItems", blackList);
        Config.reloadConfig();
    }

    /**
     * Unblocks item on server.
     * @param item Item to unblock
     */
    @SuppressWarnings("unchecked")
    public void unblockItem(CustomItem item) {
        YamlConfiguration yaml = (YamlConfiguration) YAMLManager.require("TailsLib", "config.yml");
        List<String> blackList = (List<String>) yaml.getList("blacklistedItems");
        blackList.remove(item.getItemData().getId());
        yaml.set("blacklistedItems", blackList);
        Config.reloadConfig();
    }

    public boolean isItemBlocked(CustomItem item) {
        return blacklistedItems.contains(item);
    }

    //
    //  * Papermc далбаёбы и не могут сделать нихуя нормально (:
    //  * Эта хуйня на Component не хочет делать новые линии
    //  * и заменяет их ебаным хер пойми чем.
    //  * 
    //  * Проще после такого на Spigot перейти и не ебаться.
    
    /**
     * Creates item from {@link CustomItem}.
     * @param item  {@link CustomItem}
     * @return      {@link ItemStack}
     */
    @SuppressWarnings("deprecation")
    public static ItemStack createItem(@NotNull CustomItem item) {
        ItemStack itemstack = new ItemStack(item.getItemData().getMaterial());
        ItemMeta meta = itemstack.getItemMeta();

        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', item.getItemData().getName())));
        List<String> lore = new ArrayList<>();
        lore.addAll(item.getItemData().getLore());
        meta.setLore(lore);
        if (item.customModelID() != 0)
            meta.setCustomModelData(item.customModelID());

            
        meta.getPersistentDataContainer().set(getManager().customItemKey, PersistentDataType.STRING, item.getItemData().getId());
        itemstack.setItemMeta(meta);
            
        if (item.getItemData().isShouldBeUnique()) {
            itemstack = getManager().putUUID(itemstack);
        }

        itemstack = item.getImprovedItemStack(itemstack);

        return itemstack;
    }

    @SuppressWarnings("deprecation")
    private ItemStack createRecipeItem(CustomItem item) {
        ItemStack itemstack = new ItemStack(item.getItemData().getMaterial());
        ItemMeta meta = itemstack.getItemMeta();

        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', item.getItemData().getName())));
        List<String> lore = new ArrayList<>();
        lore.addAll(item.getItemData().getLore());
        meta.setLore(lore);
        if (item.customModelID() != 0)
            meta.setCustomModelData(item.customModelID());

        meta.getPersistentDataContainer().set(customItemKey, PersistentDataType.STRING, item.getItemData().getId());
        itemstack.setItemMeta(meta);

        itemstack = item.getImprovedItemStack(itemstack);

        return itemstack;
    }

    public static @Nullable CustomItem getItemByID(String id) {
        return getManager().items.get(id);
    }

}
