package ru.thisistails.tailslib.Items;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.IDescBuilder;
import ru.thisistails.tailslib.CustomItems.SimpleDescBuilder;
import ru.thisistails.tailslib.Tools.Debug;

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

    @Override
    public void leftClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("Wow, left click.");
    }

    @Override
    public void rightClick(PlayerInteractEvent event) {
        Debug.info(event.getPlayer(), "Left click debug message");
        Debug.warn(event.getPlayer(), "Left click debug message");
        Debug.error(event.getPlayer(), "Left click debug message");
    }
    
}
