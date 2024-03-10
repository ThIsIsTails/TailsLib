package ru.thisistails.tailslib.CustomBlocks.Data;


import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import lombok.Data;
import ru.thisistails.tailslib.CustomItems.CustomItem;

@Data
public class CustomBlockData {

    private @NotNull CustomItem item;
    private @NotNull String blockId;
    private @NotNull Material blockMaterial;

}
