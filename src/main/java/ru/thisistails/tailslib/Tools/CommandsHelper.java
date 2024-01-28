package ru.thisistails.tailslib.Tools;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CommandsHelper {
    
    /**
     * Проверяет на того кто отправил команду. Если это консоль, то пишет ей сообщение, что она не может использовать эту команду.
     * @param sender            Отправитель команды
     * @param optinalMessage    Собственное сообщение по желанию
     * @return                  true - если отправитель игрок, иначе false
     */
    public static boolean isNotConsoleUser(CommandSender sender, @Nullable String optinalMessage) {
        if (sender instanceof Player) return true;

        if (optinalMessage == null) optinalMessage = "Console user cant use this command.";
        sender.sendMessage(optinalMessage);
        return false;
    }

}
