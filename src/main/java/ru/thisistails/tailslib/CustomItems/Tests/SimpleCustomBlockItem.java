package ru.thisistails.tailslib.CustomItems.Tests;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.CustomItemManager;
import ru.thisistails.tailslib.CustomItems.Data.CustomItemData;
import ru.thisistails.tailslib.Utils.Description.SimpleDescBuilder;

public class SimpleCustomBlockItem implements CustomItem {

    @Override
    public @NotNull CustomItemData getItemData() {
        CustomItemData customItemData = new CustomItemData("simplecustomitemforblock", "Простой блок", new SimpleDescBuilder().setOther("Thug shaker central"), Material.GLASS);
        return customItemData;
    }

    @Override
    public @NotNull Recipe recipe(NamespacedKey key, ItemStack item) {
        ShapelessRecipe r = new ShapelessRecipe(key, item);
        r.addIngredient(CustomItemManager.getExactChoiceFromID("testitem"));
        return r;
    }
    
}
