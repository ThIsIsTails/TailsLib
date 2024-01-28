package ru.thisistails.tailslib.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class ChatTools {

    private static @Getter Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static void sendAll(String message) {
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            player.sendMessage(message);
    }

    /**
     * Возращает строку с цветами для Minecraft.
     * @param message   Само сообщение
     * @return          Строка с цветами
     * @apiNote         Поддерживает HEX коды.
     * @see YAMLManager#getAndTranslateString
     */
    public static String translateString(String message) {
        Matcher match = pattern.matcher(message);
        while (match.find()) {
            String color = message.substring(match.start(), match.end());
            message = message.replace(color, ChatColor.of(color).toString());
            match = pattern.matcher(message);
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = message.replaceAll("&", "");

        return message;
    }
    
}
