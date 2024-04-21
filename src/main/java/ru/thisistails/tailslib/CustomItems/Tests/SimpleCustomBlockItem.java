package ru.thisistails.tailslib.CustomItems.Tests;

import java.util.ArrayList;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.Data.CustomItemData;
import ru.thisistails.tailslib.Utils.Description.SimpleDescBuilder;

public class SimpleCustomBlockItem implements CustomItem {

    @Override
    public @NotNull CustomItemData getItemData() {
        CustomItemData customItemData = new CustomItemData("simplecustomitemforblock", "Простой блок", new SimpleDescBuilder().setOther("Thug shaker central"), Material.GLASS, new ArrayList<>());
        return customItemData;
    }
    
}
