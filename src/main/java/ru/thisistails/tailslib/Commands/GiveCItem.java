package ru.thisistails.tailslib.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.CustomItemManager;
import ru.thisistails.tailslib.Tools.CommandsHelper;

public class GiveCItem implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (args.length == 1 && !CommandsHelper.isNotConsoleUser(sender, "You cant use this command this way."))
            return false;

        boolean giveToAnotherPlayer = args.length == 2;

        String itemID = args[0];
        Player toPlayer = null;
        
        if (giveToAnotherPlayer) {
            toPlayer = Bukkit.getPlayer(args[1]);
        }

        if (toPlayer == null && giveToAnotherPlayer) {
            sender.sendMessage("Не удалось найти игрока с именем " + args[1]);
            return true;
        }

        CustomItemManager manager = CustomItemManager.getManager();
        CustomItem citem = manager.getItemByID(itemID);

        if (citem == null) {
            sender.sendMessage("Предмета с ID " + itemID +" не существует.");
            return true;
        }

        if (giveToAnotherPlayer) toPlayer.getInventory().addItem(manager.createItem(citem));
        else ((Player) sender).getInventory().addItem(manager.createItem(citem));

        sender.sendMessage("Успешно выдан " + itemID);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] args) {
        List<String> toReturn = new ArrayList<>();
        toReturn.addAll(CustomItemManager.getManager().getItems().keySet());

        if (args.length == 1)
            return toReturn;
        else return null;
    }
    
}
