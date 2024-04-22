package ru.thisistails.tailslib.CustomItems.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import ru.thisistails.tailslib.CustomItems.CustomItemFlag;
import ru.thisistails.tailslib.Utils.Description.IDescBuilder;

@Data
public class CustomItemData {
    
    private @NotNull String id;
    private @NotNull String name;
    private @NotNull IDescBuilder lore;
    private @NotNull Material material;
    private @Nullable @Getter(AccessLevel.NONE) CustomItemFlag[] flags;

    public List<CustomItemFlag> getFlags() {
        if (flags == null) {
            return new ArrayList<>();
        }
        
        return Arrays.asList(flags);
    }

}
