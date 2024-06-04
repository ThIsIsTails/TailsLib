package ru.thisistails.tailslib.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;

/**
 * A simple implementation of the cooldown for
 * Using it in different moments.
 * <p>Use calls from {@link BukkitTask} which makes it impossible to call
 * cooldown before the server boots up. </p>
 * @see BukkitTask
 * @see BukkitRunnable
 */
public class Cooldown<T> {

    private @Getter Map<T, BukkitTask> cooldowns;
    private @Getter Plugin plugin;
    
    public Cooldown(String pluginName) {
        cooldowns = new ConcurrentHashMap<>();
        plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin == null) throw new NullPointerException("Plugin " + pluginName + " not found.");
    }

    /**
     * Sends T into a cooldown.
     * @param arg       Argument to send to cd
     * @param seconds   Seconds (Already multiplied by 20 for ticks)
     * @apiNote         If T already exists then this function will do nothing.
     */
    public void addNewCooldown(@NotNull T arg, long seconds) {
        seconds = seconds * 20;

        if (cooldowns.containsKey(arg)) return;

        cooldowns.put(arg, new BukkitRunnable() {

            @Override
            public void run() {
                cooldowns.remove(arg);
            }
            
        }.runTaskLater(plugin, seconds));
    }

    /**
     * Sends T into a cooldown.
     * @param arg       Argument to send to cd
     * @param seconds   Seconds (Already multiplied by 20 for ticks)
     * @apiNote         If T already exists then this function will do nothing.
     */
    public void addNewCooldown(@NotNull T arg, long seconds, boolean rewriteIfExists) {
        seconds = seconds * 20;

        if (cooldowns.containsKey(arg)) {
            if (!rewriteIfExists) return;

            cooldowns.get(arg).cancel();
        }

        cooldowns.put(arg, new BukkitRunnable() {

            @Override
            public void run() {
                cooldowns.remove(arg);
            }
            
        }.runTaskLater(plugin, seconds));
    }

    /**
     * Sends T into a cooldown.
     * @param arg       Argument to send to cd
     * @param seconds   Seconds (Already multiplied by 20 for ticks)
     * @param runnable  Which will be accomplished after the time is up.
     * @apiNote         If T already exists then this function will do nothing.
     */
    public void addNewCooldown(@NotNull T arg, long seconds, @NotNull Runnable runnable) {
        seconds = seconds * 20;

        if (cooldowns.containsKey(arg)) return;

        cooldowns.put(arg, new BukkitRunnable() {

            // Перезаписываем т.к почему бы и нет)
            // Никто не будет в конце приписывать cooldown#remove
            // Все это и так знают
            @Override
            public void run() {
                cooldowns.remove(arg);
                runnable.run();
            }
            
        }.runTaskLater(plugin, seconds));
    }

    /**
     * Sends T into a cooldown.
     * @param arg       Argument to send to cd
     * @param seconds   Seconds (Already multiplied by 20 for ticks)
     * @param runnable  Which will be accomplished after the time is up.
     * @apiNote         If T already exists and rewriteIfExists is true then it will cancel previous task and create a new one.
     */
    public void addNewCooldown(@NotNull T arg, long seconds, @NotNull Runnable runnable, boolean rewriteIfExists) {
        seconds = seconds * 20;

        if (cooldowns.containsKey(arg)) {
            if (!rewriteIfExists) return;

            cooldowns.get(arg).cancel();
        }

        cooldowns.put(arg, new BukkitRunnable() {

            // Перезаписываем т.к почему бы и нет)
            // Никто не будет в конце приписывать cooldown#remove
            // Все это и так знают
            // +мы гарантируем, что кд будет удалён после истечения времени
            @Override
            public void run() {
                cooldowns.remove(arg);
                runnable.run();
            }
            
        }.runTaskLater(plugin, seconds));
    }

    /**
     * Removes T from cooldown
     * @param arg   T like argument.
     */
    public void removeCooldown(@NotNull T arg) {
        cooldowns.get(arg).cancel();
        cooldowns.remove(arg);
    }

    public boolean containsCooldown(T arg) {
        return cooldowns.containsKey(arg);
    }

}
