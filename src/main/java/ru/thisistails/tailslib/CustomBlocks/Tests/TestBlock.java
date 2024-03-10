package ru.thisistails.tailslib.CustomBlocks.Tests;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomBlocks.CustomBlock;
import ru.thisistails.tailslib.CustomBlocks.Data.CustomBlockData;

public class TestBlock implements CustomBlock {

    @Override
    public @NotNull CustomBlockData getData() {
        return new CustomBlockData(null, "testcustomblock", Material.DARK_OAK_LOG);
    }
    
}
