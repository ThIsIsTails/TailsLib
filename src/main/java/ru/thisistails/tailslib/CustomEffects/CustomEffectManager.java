package ru.thisistails.tailslib.CustomEffects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import ru.thisistails.tailslib.Localization;
import ru.thisistails.tailslib.CustomEffects.Data.AppliedEffectData;
import ru.thisistails.tailslib.Exceptions.IDPatternException;
import ru.thisistails.tailslib.Tools.Config;
import ru.thisistails.tailslib.Tools.Debug;


public class CustomEffectManager {
    private @Getter boolean sendMessagesToPlayers;
    
    private @Getter Map<String, CustomEffect> registeredEffects = new HashMap<>();
    private @Getter List<AppliedEffectData> appliedEffects = new ArrayList<>();
    private Pattern idPattern = Pattern.compile("^[a-z_]+$");

    private static CustomEffectManager instance;

    private CustomEffectManager() {
        FileConfiguration yaml = Config.getConfig();
        sendMessagesToPlayers = yaml.getBoolean("effects.sendMessages");
    }

    public static CustomEffectManager getInstance() {
        if (instance == null) instance = new CustomEffectManager();

        return instance;
    }

    /**
     * Registers the effect.
     * @param effect    Effect to register
     * @apiNote If your items, blocks, or effects use your effect in calls, make sure you register that effect first before others.
     */
    public void register(@NotNull CustomEffect effect) {
        if (registeredEffects.containsValue(effect)) {
            Bukkit.getLogger().warning("Effect with ID " + effect.getEffectData().getId() + " already registered. Skipping...");
            return;
        }

        String id = effect.getEffectData().getId();

        Matcher matcher = idPattern.matcher(id);

        if (!matcher.find())
            throw new IDPatternException(id + " is not matching pattern " + idPattern.pattern());

        registeredEffects.put(effect.getEffectData().getId(), effect);
        Bukkit.getLogger().info("Custom effect with ID " + effect.getEffectData().getId() + " registered.");
    }

    /**
     * Returns registered custom effect or null.
     * @param id    ID of custom effect
     * @return      Registered custom effect or null.
     */
    public static @Nullable CustomEffect getEffectByID(@NotNull String id) {
        return getInstance().getRegisteredEffects().get(id);
    }

    /**
     * Triggers TailsLib to save all applied effect on this moment.
     * Do not use it if you dont understand what are you doing! It may cause some bugs after restart.
     * <p>Basicly it will trigger again when server will disabling or restarting.</p>
     * @return  Success
     */
    public boolean saveAllAppliedEffects() {
        Bukkit.getLogger().info("[CustomEffectManager] Triggered saving applied effects. Trying to save all applied effects...");

        return true;
    }

    protected void decreaseDur(AppliedEffectData d, int amount) {
        appliedEffects.forEach((data) -> {
            if (data.getUuid().equals(d.getUuid())) {
                data.setDuration(data.getDuration() - amount);
            }
        });
    }

    /**
     * Returns applied effect data on player.
     * @param player    Player to check
     * @param effect    Effect to get
     * @return  {@link AppliedEffectData} or null if player doesn't have this effect on him.
     */
    public static @Nullable AppliedEffectData getAppliedEffectData(@NotNull Player player, @NotNull CustomEffect effect) {
        for (AppliedEffectData data : getInstance().appliedEffects) {
            if (data.getEffect().equals(effect) && data.getPlayer() == player)
                return data;
        }

        return null;
    }

    /**
     * Checks if player have specific effect on him.
     * <p>This function do not checks level or duration of the effect.</p>
     * @param player    Player
     * @param effect    Effect
     * @return  True if player have specific effect or false if doesn't
     */
    public static boolean containEffect(@NotNull Player player, @NotNull CustomEffect effect) {
        return getAppliedEffectData(player, effect) != null;
    }

    /**
     * Returns all applied effects on player in this current tick.
     * @param player Player
     * @return  Applied effects
     */
    public static List<AppliedEffectData> getAllPlayerAppliedEffects(@NotNull Player player) {
        List<AppliedEffectData> list = new ArrayList<>();

        for (AppliedEffectData data : getInstance().appliedEffects) {
            if (data.getPlayer() == player)
                list.add(data);
        }

        return list;
    }

    /**
     * This function returns all {@link AppliedEffectData}'s of effect
     * @param effect    Effect
     * @return          All {@link AppliedEffectData}'s of effect
     */
    public static List<AppliedEffectData> getAllDatasWithEffect(@NotNull CustomEffect effect) {
        List<AppliedEffectData> list = new ArrayList<>();

        for (AppliedEffectData data : getInstance().appliedEffects) {
            if (data.getEffect() == effect)
                list.add(data);
        }

        return list;
    }


    protected void effectEnded(AppliedEffectData data) {
        if (data.getDuration() == 0) {
            appliedEffects.remove(data);
            data.getEffect().onEffectEnd(data);

            if (sendMessagesToPlayers) {
                data.getPlayer().sendMessage(Localization.prefix + " " + Localization.onEffectEnd
                    .replace("%effect_name%", data.getEffect().getEffectData().getName())
                    .replace("%effect_id%", data.getEffect().getEffectData().getId())
                    .replace("%effect_duration%", String.valueOf(data.getDuration()))
                    .replace("%effect_level%", String.valueOf(data.getLevel()))
                );
            }
        }
    }

    protected boolean callEffect(@NotNull AppliedEffectData data) {
        if (data.getLevel() > data.getEffect().getEffectData().getMaxLevel()) {
            Bukkit.getLogger().severe("[CustomEffectManager] Something went wrong while executing #callEffect. [" + data.getEffect().getEffectData().getId() + 
            "; player: " + data.getPlayer().getName() + "]");
            Bukkit.getLogger().severe("[CustomEffectManager] AppliedEffectData have more level than max level allowed.");
            Bukkit.getLogger().severe("[CustomEffectManager] Effect will be interrupted with ErrorOccured reason.");
            interruptEffect(data.getPlayer(), data.getEffect(), EffectInterruptionReason.ErrorOccured);
        }

        try {
            data.getEffect().onTickCall(data);
            decreaseDur(data, 1);

            if (data.getDuration() == 0) {
                effectEnded(data);
            }
            return true;
        } catch (Exception error) {
            error.printStackTrace();
            Bukkit.getLogger().severe("Effect " + data.getEffect().getEffectData().getId() + " has occured an exception.");
            Debug.error(data.getPlayer(), Localization.onEffectInterruption
                .replace("%effect_name%", data.getEffect().getEffectData().getName())
                .replace("%effect_id%", data.getEffect().getEffectData().getId())
                .replace("%effect_duration%", "0")
                .replace("%effect_level%", String.valueOf(data.getLevel()))
                );
            return false;
        }
    }

    /**
     * Applies effect to player.
     * @param player    Player
     * @param effect    Custom effect (Must be registered via {@link #register(CustomEffect)})
     * @param level     Level of the effect
     * @param duration  Duration of the effect in seconds. (Not ticks)
     * @return          Success, if not then it means that custom effect have errors.
     */
    public boolean applyEffect(@NotNull Player player, @NotNull CustomEffect effect, @NotNull int level, @NotNull int duration) {
        // То что нужно наложить.
        AppliedEffectData data = new AppliedEffectData(effect, player, level, duration);

        // Для проверки
        AppliedEffectData check = getAppliedEffectData(player, effect);

        if (check != null) {
            if (data.getLevel() > check.getLevel() || data.getDuration() > check.getDuration())
                interruptEffect(player, effect, EffectInterruptionReason.ApplyingHigherLevelEffect);
        }

        try {
            data.getEffect().onEffectApplied(data);
            if (sendMessagesToPlayers) {
                data.getPlayer().sendMessage(Localization.prefix + " " + Localization.onEffectApply
                    .replace("%effect_name%", data.getEffect().getEffectData().getName())
                    .replace("%effect_id%", data.getEffect().getEffectData().getId())
                    .replace("%effect_duration%", String.valueOf(data.getDuration()))
                    .replace("%effect_level%", String.valueOf(data.getLevel()))
                );
            }
        } catch (Exception error) {
            error.printStackTrace();
            data.getEffect().onEffectInterrupted(data, EffectInterruptionReason.ErrorOccured);
            Bukkit.getLogger().severe("Effect " + data.getEffect().getEffectData().getId() + " has occured an exception.");
            return false;
        }

        appliedEffects.add(data);

        return true;
    }

    /**
     * Interrupt effect AKA remove it.
     * <p>This operation will trigger {@link CustomEffect#onEffectInterrupted(Player, int, EffectInterruptionReason)}</p>
     * @param player    Player to remove effect from
     * @param effect    Effect to remove.
     */
    public void interruptEffect(Player player, CustomEffect effect, EffectInterruptionReason reason) {
        AppliedEffectData data = getAppliedEffectData(player, effect);

        if (data == null)
            return;
        
        appliedEffects.remove(data);
        try {
            effect.onEffectInterrupted(data, reason);
        } catch (Exception error) {
            Bukkit.getLogger().log(Level.SEVERE, "Effect: " + effect.getEffectData().getId() + " occured an error.", error);
        }
    }

}
