package ru.thisistails.tailslib.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;

/**
 * Простая реализация кулдауна для
 * использования её в разных моментах.
 * <p>Использует вызовы из {@link BukkitTask} из-за чего вызывать
 * кд до момента загрузки сервера нельзя.</p>
 * @see BukkitTask
 * @see BukkitRunnable
 */
public class Cooldown<T> {

    private @Getter Map<T, BukkitTask> cooldowns;
    
    public Cooldown() {
        cooldowns = new HashMap<>();
    }

    /**
     * Отправляет T в кулдаун.
     * @param arg       Аргумент который отправлять в кд
     * @param seconds   Секунды (уже умножены на 20 для тиков)
     */
    public void addNewCooldown(T arg, long seconds) {
        seconds = seconds * 20;
        Plugin plugin = Bukkit.getPluginManager().getPlugin("TailsLib");

        cooldowns.put(arg, new BukkitRunnable() {

            @Override
            public void run() {
                cooldowns.remove(arg);
            }
            
        }.runTaskLater(plugin, seconds));
    }

    /**
     * Отправляет T в кулдаун.
     * @param arg       Аргумент который отправлять в кд
     * @param seconds   Секунды (уже умножены на 20 для тиков)
     * @param runnable  Что будет выполнено после того как выйдет время.
     */
    public void addNewCooldown(T arg, long seconds, BukkitRunnable runnable) {
        seconds = seconds * 20;
        Plugin plugin = Bukkit.getPluginManager().getPlugin("TailsLib");

        cooldowns.put(arg, runnable.runTaskLater(plugin, seconds));
    }

    /**
     * Убирает T из кулдауна.
     * @param arg   Аргумент типа T который находится в списке.
     */
    public void removeCooldown(T arg) {
        cooldowns.get(arg).cancel();
        cooldowns.remove(arg);
    }

    public boolean containsCooldown(T arg) {
        return cooldowns.containsKey(arg);
    }

}
