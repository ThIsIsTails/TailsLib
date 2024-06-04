package ru.thisistails.tailslib.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;

import ru.thisistails.tailslib.Tools.Debug;

public class SettingsCommand implements CommandExecutor, TabCompleter {

    @AllArgsConstructor
    public enum Option {
        DebugLog("dlog"),
        Info("info");

        public static Option of(String name) {
            for (Option option : Option.values()) {
                if (option.name.equalsIgnoreCase(name))
                    return option;
            }

            return null;
        }

        public final @Getter String name;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] args) {
        if (args.length == 0) return false;

        Option option = Option.of(args[0]);

        if (option == null) return false;

        switch (option) {
            case DebugLog:
                if (args.length != 2) {
                    sender.sendMessage("You need to specify true or false for this command.");
                    return true;
                }
                if (!(sender instanceof Player)) return true;

                boolean value = Boolean.parseBoolean(args[1]);

                if (value) {
                    Debug.ignorePlayer((Player) sender);
                    sender.sendMessage("You will no longer recieve debug logs.");
                } else {
                    Debug.stopIgnoringPlayer((Player) sender);
                    sender.sendMessage("You will recieve debug logs.");
                }
                break;
            
            case Info:
                sender.sendMessage("TailsLib by ThIsIsTails\nSource: https://gitlab.com/ThIsIsTails/tailslib-mc");
                break;
        
            default:
                return false;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] args) {
        List<String> toReturn = new ArrayList<>();

        if (args.length == 1) {
            for (Option option : Option.values())
                toReturn.add(option.name);
        } else {
            toReturn = null;
        }

        return toReturn;
    }
    
}
