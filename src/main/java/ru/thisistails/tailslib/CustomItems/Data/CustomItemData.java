package ru.thisistails.tailslib.CustomItems.Data;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import lombok.Data;
import ru.thisistails.tailslib.Utils.Description.IDescBuilder;

@Data
public class CustomItemData {
    
    private @NotNull String id;
    private @NotNull String name;
    private IDescBuilder lore;
    private @NotNull Material material;

}
