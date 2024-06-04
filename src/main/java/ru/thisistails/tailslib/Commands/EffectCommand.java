package ru.thisistails.tailslib.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.thisistails.tailslib.Localization;
import ru.thisistails.tailslib.CustomEffects.CustomEffect;
import ru.thisistails.tailslib.CustomEffects.CustomEffectManager;
import ru.thisistails.tailslib.CustomEffects.EffectInterruptionReason;
import ru.thisistails.tailslib.CustomEffects.Data.AppliedEffectData;

public class EffectCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] args) {
        if (args.length == 0) return false;
        StringBuffer buffer = new StringBuffer();

        switch (args[0]) {
            case "list":
                for (CustomEffect effect : CustomEffectManager.getInstance().getRegisteredEffects().values()) {
                    buffer.append(effect.getEffectData().getId()).append(" ");
                }

                sender.sendMessage(Localization.prefix + " [" + String.valueOf(CustomEffectManager.getInstance().getRegisteredEffects().values().size()) + "]: " + buffer.toString());
                return true;
            case "check":
                if (args.length < 2) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Localization.onlyPlayers);
                        return true;
                    }

                    Player player = (Player) sender;
                    List<AppliedEffectData> datas = CustomEffectManager.getAllPlayerAppliedEffects(player);
                    if (datas.isEmpty()) {
                        sender.sendMessage(Localization.prefix + " " + Localization.noEffects);
                        return true;
                    }

                    for (AppliedEffectData data : datas)
                        buffer.append(data.getEffect().getEffectData().getId()).append(" ");
                    
                    sender.sendMessage(Localization.prefix + " " + buffer.toString());
                }
                return true;
            case "apply":
                if (args.length != 5) return false;
                Player player = Bukkit.getPlayerExact(args[1]);
                if (player == null) {
                    sender.sendMessage(Localization.prefix + " " + Localization.playerNotFound.replace("%player%", args[1]));
                    return true;
                }
                CustomEffect effect = CustomEffectManager.getEffectByID(args[2]);
                if (effect == null) {
                    sender.sendMessage(Localization.prefix + " " + Localization.effectNotFound
                        .replace("%effect_id%", args[2]));
                    return true;
                }

                int level, dur;

                try {
                    level = Integer.parseInt(args[3]);
                    dur = Integer.parseInt(args[4]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(Localization.prefix + " " + Localization.numberFormatException);
                    return true;
                }

                CustomEffectManager.getInstance().applyEffect(player, effect, level, dur);
                sender.sendMessage(Localization.prefix + " " + Localization.effectAppliedSuccessfully
                    .replace("%effect_id%", effect.getEffectData().getId())
                    .replace("%effect_name%", effect.getEffectData().getName())
                    .replace("%effect_duration%", String.valueOf(dur))
                    .replace("%effect_level%", String.valueOf(level))
                    .replace("%player%", player.getName())
                );
                return true;
            case "remove":
                if (args.length < 2) return false;
                Player target;
                if (args.length == 3) {
                    target = Bukkit.getPlayerExact(args[2]);
                    if (target == null) {
                        sender.sendMessage(Localization.prefix + " " + Localization.playerNotFound.replace("%player%", args[2]));
                        return true;
                    }
                } else {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("You should use this command other way.");
                        return false;
                    }
                    target = (Player) sender;
                }

                if (args[1].equals("all")) {
                    CustomEffectManager.getAllPlayerAppliedEffects(target)
                    .forEach(ceffect -> CustomEffectManager.getInstance().interruptEffect(target, ceffect.getEffect(), EffectInterruptionReason.Plugin));
                    sender.sendMessage(Localization.prefix + " " + Localization.allEffectsCleared.replace("%player%", target.getName()));
                    return true;
                }

                if (CustomEffectManager.getEffectByID(args[1]) == null && !CustomEffectManager.containEffect(target, CustomEffectManager.getEffectByID(args[1]))) {
                    sender.sendMessage(Localization.prefix + " " + Localization.effectNotFound.replace("%effect_id%", args[1]));
                    return true;
                }
                CustomEffectManager.getInstance().interruptEffect(target, CustomEffectManager.getEffectByID(args[1]), EffectInterruptionReason.Plugin);
                sender.sendMessage(Localization.prefix + " " + Localization.effectCleared.replace("%effect_id%", args[1])
                    .replace("%player%", target.getName()));
                return true;
        
            default:
                return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> subCommands = Arrays.asList("list", "check", "apply", "remove");

        if (args.length == 1) {
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
            return completions;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("apply")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(player.getName());
                    }
                }
            }
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("apply")) {
            Set<String> effectIds = CustomEffectManager.getInstance().getRegisteredEffects().keySet();
            for (String effectId : effectIds) {
                if (effectId.toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add(effectId);
                }
            }
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("apply")) {
            completions.add("<level>");
        }

        if (args.length == 5 && args[0].equalsIgnoreCase("apply")) {
            completions.add("<duration>");
        }

        return completions;
    }
    
}
