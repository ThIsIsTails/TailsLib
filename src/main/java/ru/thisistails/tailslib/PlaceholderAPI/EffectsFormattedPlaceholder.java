package ru.thisistails.tailslib.PlaceholderAPI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import ru.thisistails.tailslib.Localization;
import ru.thisistails.tailslib.CustomEffects.CustomEffectManager;
import ru.thisistails.tailslib.CustomEffects.Data.AppliedEffectData;

public class EffectsFormattedPlaceholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "ThIsIsTails";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "effectsformatted";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return null;

        List<AppliedEffectData> datas = CustomEffectManager.getAllPlayerAppliedEffects(player);

        if (datas.isEmpty()) 
            return Localization.noEffects;
        
        List<String> strings = new ArrayList<>();

        datas.forEach((data) -> {
            strings.add(data.getEffect().getEffectData().getName());
        });

        return String.join(", ", strings);
    }
    
}
