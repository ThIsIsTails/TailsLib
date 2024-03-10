package ru.thisistails.tailslib.CustomItems.Tests;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.Data.CustomItemData;

public class SimpleCustomBlockItem implements CustomItem {

    @Override
    public @NotNull CustomItemData getItemData() {
        return new CustomItemData("simplecustomitemforblock", "Простой блок", Material.GLASS);
    }
    
}
