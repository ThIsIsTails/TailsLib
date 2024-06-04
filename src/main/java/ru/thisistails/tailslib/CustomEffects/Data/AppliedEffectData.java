package ru.thisistails.tailslib.CustomEffects.Data;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ru.thisistails.tailslib.CustomEffects.CustomEffect;

/**
 * Data class for applied effects.
 */
@Data
public class AppliedEffectData {
    
    private @Setter(AccessLevel.NONE) UUID uuid = UUID.randomUUID();
    /**
     * Applied effect
     */
    private @Setter(AccessLevel.NONE) @NotNull CustomEffect effect;
    /**
     * Player that have this effect
     */
    private @Setter(AccessLevel.NONE) @NotNull Player player;
    /**
     * Level of the effect.
     * @apiNote Level must less or equals to 1 + {@link CustomEffectData#getMaxAmplifier()} or else on tick calls manager will interrupt effect.
     */
    private @Setter(AccessLevel.NONE) @NotNull int level;
    private @Setter(AccessLevel.PUBLIC) @NotNull int duration;

}
