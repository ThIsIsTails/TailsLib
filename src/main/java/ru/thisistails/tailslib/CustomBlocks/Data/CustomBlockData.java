package ru.thisistails.tailslib.CustomBlocks.Data;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ru.thisistails.tailslib.CustomItems.CustomItem;

@Data
public class CustomBlockData {

    private @Setter(AccessLevel.NONE) @NotNull CustomItem item;
    private @Setter(AccessLevel.NONE) @NotNull String blockId;
    private @Setter(AccessLevel.NONE) @NotNull Material blockMaterial;
    private boolean dropItem = true;

}
