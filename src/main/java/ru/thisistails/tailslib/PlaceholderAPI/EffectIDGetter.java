package ru.thisistails.tailslib.PlaceholderAPI;

import java.util.List;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import ru.thisistails.tailslib.CustomEffects.CustomEffectManager;
import ru.thisistails.tailslib.CustomEffects.Data.AppliedEffectData;

public class EffectIDGetter extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "ThIsIsTails";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "geteffectfromplayer";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        
        List<AppliedEffectData> datas = CustomEffectManager.getAllPlayerAppliedEffects(player);
        int index = 0;

        if (params.isEmpty()) return null;

        try {
            index = Integer.valueOf(params);
        } catch (NumberFormatException error) {
            return null;
        }

        return datas.get(index).getEffect().getEffectData().getId();
    }

    
    
}
