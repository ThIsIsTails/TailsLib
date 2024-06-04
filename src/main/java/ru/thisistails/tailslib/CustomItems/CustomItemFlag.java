package ru.thisistails.tailslib.CustomItems;

/**
 * Enum representing various flags that can be applied to custom items.
 */
public enum CustomItemFlag {
    /**
     * Flag to disable actions for the custom item.
     */
    DisableActions,
    
    /**
     * Flag to prevent the custom item from being used in Player vs Player (PVP) combat.
     */
    NoPVP,
    
    /**
     * Flag to prevent the custom item from being enchanted.
     */
    NoEchants;
}
