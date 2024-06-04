package ru.thisistails.tailslib.CustomEffects.Data;

import ru.thisistails.tailslib.CustomEffects.CustomEffectManager;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class CustomEffectData {

    /**
     * ID for {@link CustomEffectManager}
     */
    private @Setter(AccessLevel.NONE) @NotNull String id;
    /**
     * Name for placeholders.
     */
    private @Setter(AccessLevel.NONE) @NotNull String name;
    /**
     * Maximum amplifier for effect.
     * @apiNote Max level = 1 + amplifier. Set amplifier to 0 for max level = 1.
     */
    private @Setter(AccessLevel.NONE) @NotNull int maxAmplifier;
    /**
     * Can undo the effects with milk
     */
    private @NotNull boolean canBeClearedByMilk;
    /**
     * Cancel the effect on death
     */
    private @NotNull boolean clearOnDeath;

    public int getMaxLevel() {
        return 1 + maxAmplifier;
    }
    
}
