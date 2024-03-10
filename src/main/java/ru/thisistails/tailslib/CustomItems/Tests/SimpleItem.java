package ru.thisistails.tailslib.CustomItems.Tests;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.Data.CustomItemData;
import ru.thisistails.tailslib.Tools.Debug;
import ru.thisistails.tailslib.Utils.Description.SimpleDescBuilder;

public class SimpleItem implements CustomItem {

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

    @Override
    public @NotNull CustomItemData getItemData() {
        CustomItemData data = new CustomItemData("testitem", "Простой тестовый предмет", Material.IRON_AXE);
        data.setLore(new SimpleDescBuilder().addDesc("Простой предмет."));

        return data;
    }
    
}
