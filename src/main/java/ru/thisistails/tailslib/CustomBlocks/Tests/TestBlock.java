package ru.thisistails.tailslib.CustomBlocks.Tests;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ru.thisistails.tailslib.CustomBlocks.CustomBlock;
import ru.thisistails.tailslib.CustomBlocks.Data.CustomBlockData;
import ru.thisistails.tailslib.CustomItems.CustomItemManager;

public class TestBlock implements CustomBlock {

    @Override
    public @NotNull CustomBlockData getData() {
        return new CustomBlockData(CustomItemManager.getManager().getItemByID("simplecustomitemforblock"), "testcustomblock", Material.DARK_OAK_LOG);
    }

    @Override
    public void onRightClickOnBlock(Player player) {
        player.sendMessage("Yo, how r u?");
    }

    @Override
    public void onCreation(BlockPlaceEvent event) {
        event.getPlayer().sendMessage("Thug shacker central is here.");
    }

    @Override
    public void onDestroy(BlockBreakEvent event) {
        event.getPlayer().sendMessage("Yo, i'm leaving.");
    }

    @Override
    public JsonElement onSave() {
        JsonObject obj = new JsonObject();

        obj.addProperty("ABoba", "aboba");
        return obj;
    }
    
}
