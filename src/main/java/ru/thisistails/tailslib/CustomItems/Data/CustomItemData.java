package ru.thisistails.tailslib.CustomItems.Data;

import java.util.List;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import lombok.Data;
import ru.thisistails.tailslib.CustomItems.CustomItemFlag;
import ru.thisistails.tailslib.Utils.Description.IDescBuilder;

@Data
public class CustomItemData {
    
    private @NotNull String id;
    private @NotNull String name;
    private @NotNull IDescBuilder lore;
    private @NotNull Material material;
    private @NotNull List<CustomItemFlag> flags;

}
