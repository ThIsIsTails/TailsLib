package ru.thisistails.tailslib.Commands;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.CustomItems.ItemManager;

// FIXME: Не пишет зарегестрированные предметы.
public class ListItems implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command label, @NotNull String arg2,
            @NotNull String[] args) {
        
        if (!sender.hasPermission("tailslib.listregistereditems")) {
            sender.sendMessage(Component.text("Sorry, but you don't have permission to check registered items.").color(TextColor.color(255, 0, 0)));
            return true;
        }

        Map<String, CustomItem> lItems = ItemManager.getManager().getItems();
        Component text = Component.text("List of reg items: ");
        
        if (lItems.isEmpty()) {
            text.append(Component.text("Theres no registered items right now. [Use ItemManager.getManager().register(CustomItem) to register.]"));
            sender.sendMessage(text);
            return true;
        }

        for (Entry<String, CustomItem> items : lItems.entrySet()) {
            CustomItem item = items.getValue();
            if (ItemManager.getManager().isItemBlocked(item)) {
                text.append(Component.text(item.getId()).color(TextColor.color(255, 0, 0))).append(Component.text(" "));
            }

            text.append(Component.text(item.getId() + " "));
        }
        
        sender.sendMessage(text);

        return true;
    }
    
}
