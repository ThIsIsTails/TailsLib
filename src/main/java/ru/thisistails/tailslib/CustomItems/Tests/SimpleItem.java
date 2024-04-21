package ru.thisistails.tailslib.CustomItems.Tests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.CustomItemFlag;
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

        List<CustomItemFlag> flags = new ArrayList<>();
        flags.add(CustomItemFlag.AsUniqueMaterial);

        CustomItemData data = new CustomItemData("testitem", "Простой тестовый предмет", new SimpleDescBuilder().addDesc("Простой предмет."), Material.STICK, flags);

        return data;
    }
    
}
