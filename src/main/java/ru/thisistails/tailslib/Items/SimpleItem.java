package ru.thisistails.tailslib.Items;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.IDescBuilder;
import ru.thisistails.tailslib.CustomItems.SimpleDescBuilder;

public class SimpleItem implements CustomItem {

    @Override
    public @NotNull String getId() {
        return "testitem";
    }

    @Override
    public String getName() {
        return "Простой тестовый предмет";
    }

    @Override
    public IDescBuilder getLore() {
        return new SimpleDescBuilder()
        .addDesc("Просто тестовое описание")
        .setOther("Что-то остальное")
        .addBuff("Это бафф")
        .addDebuff("Это дебафф");
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_AXE;
    }
    
}
