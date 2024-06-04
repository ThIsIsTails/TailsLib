package ru.thisistails.tailslib.CustomItems.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.CustomItemFlag;
import ru.thisistails.tailslib.Utils.Description.IDescBuilder;

@Data
public class CustomItemData {
    
    private @NotNull @Setter(AccessLevel.NONE) String id;
    private @NotNull @Setter(AccessLevel.NONE) String name;
    private @NotNull @Setter(AccessLevel.NONE) List<String> lore;
    private @NotNull @Setter(AccessLevel.NONE) Material material;
    private @Nullable @Getter(AccessLevel.NONE) CustomItemFlag[] flags;

    public CustomItemData(String id, String name, IDescBuilder lore, Material material) {
        this.id = id;
        this.name = name;
        this.lore = lore.build();
        this.material = material;
    }

    /**
     * Means that item no longer can used in oridnary crafts like diamond in diamond sword craft.
     * In our case we don't want to use it in craft like this.
     * <p>Example: Your item material is iron and this setting is true.
     * Players cant craft iron sword with your item but can craft something in custom crafts where {@link ExactChoice} is used.</p>
     */
    private @Nullable boolean asUniqueMaterial = true;

    /**
     * Means that tailslib should use UUID for items.
     * That also means UUID argument in functions like {@link CustomItem#rightClick(PlayerInteractEvent, UUID)} would never be null.
     */
    private boolean shouldBeUnique = true;

    public List<CustomItemFlag> getFlags() {
        if (flags == null) {
            return new ArrayList<>();
        }
        
        return Arrays.asList(flags);
    }

    public void setFlags(CustomItemFlag... flags) {
        this.flags = flags;
    }

}
