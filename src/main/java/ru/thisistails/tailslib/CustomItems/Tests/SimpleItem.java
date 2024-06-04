package ru.thisistails.tailslib.CustomItems.Tests;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.CustomItemFlag;
import ru.thisistails.tailslib.CustomItems.Data.CustomItemData;
import ru.thisistails.tailslib.Tools.Debug;
import ru.thisistails.tailslib.Utils.Description.SimpleDescBuilder;

public class SimpleItem implements CustomItem {

    @Override
    public void leftClick(PlayerInteractEvent event, UUID uuid) {
        event.getPlayer().sendMessage("Wow, left click.");
        Debug.info(event.getPlayer(), "Fetched UUID: " + String.valueOf(uuid));
    }

    @Override
    public void rightClick(PlayerInteractEvent event, UUID uuid) {
        Debug.info(event.getPlayer(), "Left click debug message");
        Debug.warn(event.getPlayer(), "Left click debug message");
        Debug.error(event.getPlayer(), "Left click debug message");
    }

    @Override
    public @NotNull CustomItemData getItemData() {
        CustomItemFlag[] flags = {
            CustomItemFlag.NoEchants
        };

        CustomItemData data = new CustomItemData("testitem", "Test item", new SimpleDescBuilder().addDesc("Простой предмет."), Material.IRON_AXE);
        data.setFlags(flags);
        data.setAsUniqueMaterial(true);

        return data;
    }

    @Override
    public @NotNull Recipe recipe(NamespacedKey key, ItemStack item) {
        ShapelessRecipe r = new ShapelessRecipe(key, item);
        r.addIngredient(Material.STICK);
        return r;
    }
    
}
