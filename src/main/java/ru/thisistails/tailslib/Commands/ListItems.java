package ru.thisistails.tailslib.Commands;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.ItemManager;

public class ListItems implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command label, @NotNull String arg2,
            @NotNull String[] args) {


        Map<String, CustomItem> lItems = ItemManager.getManager().getItems();

        // Эта херабора работает только так.
        // Я в душе не ебу почему и как, но Component#append не хочет работать как задуманно.
        // Поэтому это работает только так.
        Component toSend;
        StringBuffer text = new StringBuffer();
        
        if (lItems.isEmpty()) {
            text.append("Сейчас нету загруженных предметов.");
            toSend = Component.text(text.toString());
            sender.sendMessage(toSend);
            return true;
        }

        text.append("Загруженные предметы: ");

        for (Entry<String, CustomItem> items : lItems.entrySet()) {
            CustomItem item = items.getValue();
            if (ItemManager.getManager().isItemBlocked(item)) {
                text.append(ChatColor.RED + item.getId() + ChatColor.RESET + " ");
                continue;
            }
            text.append(item.getId() + " ");
        }
        
        toSend = Component.text(text.toString());
        sender.sendMessage(toSend);

        return true;
    }
    
}
