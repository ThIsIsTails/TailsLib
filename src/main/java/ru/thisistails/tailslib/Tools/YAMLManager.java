package ru.thisistails.tailslib.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class YAMLManager {

    private static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    /**
     * Запрос на определённый файл yml по определённуму пути.
     * Если файла не существует то возьмёт и создаст этот файл а после вернёт как искомое.
     *
     * @param   filePath    путь к файлу без слэша в начале.
     * @return              Запрошенный файл конфигурации.
     */
    public static FileConfiguration require(String pluginName, String filePath) {

        File file = new File(Bukkit.getPluginManager().getPlugin(pluginName).getDataFolder() + "/" + filePath);

        if (!file.exists()) {
            try {
                //locFile.mkdir();
                Bukkit.getPluginManager().getPlugin(pluginName).saveResource(filePath, false);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }

        return YamlConfiguration.loadConfiguration(file);

    }

    /**
     * Не поддерживает HEX коды.
     * @param filePath  Путь файла без слэша в начале
     * @param path      Путь к списку в самом файле.
     * @return          Список строк с заменой символа цвета
     * @see             ChatColor
     */
    public static List<String> getAndTranslate(String pluginName, String filePath, String path) {
        YamlConfiguration config = (YamlConfiguration) require(pluginName, filePath);
        List<String> list = new ArrayList<String>();

        for (String i : config.getStringList(path)) { list.add(ChatColor.translateAlternateColorCodes('&', i)); }

        return list;
    }

    /**
     * Поддерживает HEX коды.
     * @param filePath  Путь файла без слэша в начале
     * @param path      Путь к строке в самом файле.
     * @return          Строка с заменой символа цвета
     * @see             ChatColor
     */
    public static String getAndTranslateString(String pluginName, String filePath, String path) {
        YamlConfiguration config = (YamlConfiguration) require(pluginName, filePath);
        String getted = config.getString(path);

        Matcher match = pattern.matcher(getted);
        while (match.find()) {
            String color = getted.substring(match.start(), match.end());
            getted = getted.replace(color, ChatColor.of(color).toString());
            match = pattern.matcher(getted);
        }
        getted = ChatColor.translateAlternateColorCodes('&', getted);
        getted = getted.replaceAll("&", "");

        return getted;
    }


}
