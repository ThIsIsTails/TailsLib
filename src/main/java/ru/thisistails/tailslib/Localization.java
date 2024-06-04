package ru.thisistails.tailslib;

import ru.thisistails.tailslib.Utils.Phrase;

public class Localization {
    
    public final static Phrase phrase = new Phrase("TailsLib");

    public final static String prefix = phrase.getPhrase("prefix");
    
    public final static String onEffectApply = phrase.getPhrase("effects.onApply");
    public final static String onEffectEnd = phrase.getPhrase("effects.onEffectEnd");
    public final static String onEffectInterruption = phrase.getPhrase("effects.onInterruption");
    public final static String noEffects = phrase.getPhrase("effects.placeholders.effectsNoEffects");
    public final static String effectAppliedSuccessfully = phrase.getPhrase("effects.commands.effectApplied");
    public final static String allEffectsCleared = phrase.getPhrase("effects.commands.allEffectsCleared");
    public final static String effectCleared = phrase.getPhrase("effects.commands.effectCleared");

    public final static String itemNotRegistered = phrase.getPhrase("errors.itemNotRegistered");
    public final static String onlyPlayers = phrase.getPhrase("errors.onlyPlayerAllowed");
    public final static String playerNotFound = phrase.getPhrase("errors.playerNotFound");
    public final static String effectNotFound = phrase.getPhrase("effects.commands.effectNotFound");
    public final static String numberFormatException = phrase.getPhrase("errors.numberFormatException");

}
