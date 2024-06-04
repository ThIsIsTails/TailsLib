package ru.thisistails.tailslib.CustomEffects;

import org.jetbrains.annotations.NotNull;

import ru.thisistails.tailslib.CustomEffects.Data.AppliedEffectData;
import ru.thisistails.tailslib.CustomEffects.Data.CustomEffectData;

public interface CustomEffect {

    public @NotNull CustomEffectData getEffectData();

    /**
     * Called every second.
     * @param data Applied effect data.
     * @see AppliedEffectData
     * @apiNote If this event give an error then this effect will be interrupted with reason {@link EffectInterruptionReason#ErrorOccured}
     */
    public default void onTickCall(AppliedEffectData data) {}

    /**
     * Called when the effect ends on its own and has not been interrupted.
     * @param data Applied effect data.
     */
    public default void onEffectEnd(AppliedEffectData data) {}

    /**
     * Called when the effect has been interrupted for any reason.
     * @param data Applied effect data.
     * @param reason    Iterruption reason
     * @see EffectInterruptionReason
     * @apiNote You should check reason because player can leave the server or do something.
     */
    public default void onEffectInterrupted(AppliedEffectData data, EffectInterruptionReason reason) {}

    /**
     * Called when the effect has been applied.
     * @param data  Applied effect data
     * @apiNote If this event give an error then this effect will be interrupted with reason {@link EffectInterruptionReason#ErrorOccured}
     */
    public default void onEffectApplied(AppliedEffectData data) {}


}
