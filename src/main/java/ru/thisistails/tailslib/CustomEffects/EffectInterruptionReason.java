package ru.thisistails.tailslib.CustomEffects;

public enum EffectInterruptionReason {
    /**
     * Effect interrupted because player has died.
     */
    PlayerDied,
    PlayerQuit,
    /**
     * Effect inturrupted because player has drank the milk.
     */
    PlayerDrinksMilk,
    /**
     * Plugin has canceled the effect.
     */
    Plugin,
    /**
     * Plugin has applied new effcet of this type but higher level or with more duration.
     * <p>Don't worry, the interrupt call will be first before a new effect overlay is applied</p>
     */
    ApplyingHigherLevelEffect,
    /**
     * Effect have errors and has been canceled.
     * <p>Thats will trigger TailsLib to send message to player about that even {@code config.effects.sendMessages} is false.
     * The only way to disable it its only by turning {@code config.debug.supressWarnings} on</p>
     */
    ErrorOccured,
    /**
     * Signals that TailsLib is disabling.
     */
    LibDisabling;
}
