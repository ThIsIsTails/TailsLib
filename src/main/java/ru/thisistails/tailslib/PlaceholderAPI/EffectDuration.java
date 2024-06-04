package ru.thisistails.tailslib.PlaceholderAPI;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import ru.thisistails.tailslib.CustomEffects.CustomEffect;
import ru.thisistails.tailslib.CustomEffects.CustomEffectManager;
import ru.thisistails.tailslib.CustomEffects.Data.AppliedEffectData;

public class EffectDuration extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "ThIsIsTails";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "effectduration";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        CustomEffect effect = CustomEffectManager.getEffectByID(params);
        if (effect == null) return null;

        AppliedEffectData data = CustomEffectManager.getAppliedEffectData(player, effect);
        if (data == null) return null;

        return String.valueOf(data.getDuration());
    }
    
}
