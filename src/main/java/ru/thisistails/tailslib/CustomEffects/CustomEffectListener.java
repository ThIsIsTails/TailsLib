package ru.thisistails.tailslib.CustomEffects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;

import ru.thisistails.tailslib.CustomEffects.Data.AppliedEffectData;

public class CustomEffectListener implements Listener {

    private final CustomEffectManager manager = CustomEffectManager.getInstance();
    private static CustomEffectListener instance;

    private CustomEffectListener() {}

    public static CustomEffectListener getListener() {
        if (instance == null) instance = new CustomEffectListener();

        return instance;
    }


    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("TailsLib"), new Runnable() {

            // chatgpt saved my fuckin time on this.
            // Im fuckin dumb to understand that i need create a clone of my list
            @Override
            public void run() {
                List<AppliedEffectData> toRemove = new ArrayList<>();
                List<AppliedEffectData> appliedEffects = new ArrayList<>(manager.getAppliedEffects()); // Создаем копию коллекции для итерации
                for (AppliedEffectData data : appliedEffects) {
                    if (data.getDuration() == 0) {
                        if (!toRemove.contains(data))
                            toRemove.add(data);
                        else
                            continue;
                    }

                    if (!manager.callEffect(data)) {
                        manager.interruptEffect(data.getPlayer(), data.getEffect(), EffectInterruptionReason.ErrorOccured);
                    }

                }

                for (AppliedEffectData data : toRemove)
                    manager.effectEnded(data);
            }
            
        }, 20, 20);
        Bukkit.getLogger().info("[CustomEffectListener] Scheduler task started.");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        for (AppliedEffectData data : CustomEffectManager.getAllPlayerAppliedEffects(player)) {
            manager.interruptEffect(player, data.getEffect(), EffectInterruptionReason.PlayerQuit);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        CustomEffectManager.getAllPlayerAppliedEffects(player).forEach((data) -> {
            if (data.getEffect().getEffectData().isClearOnDeath())
                CustomEffectManager.getInstance().interruptEffect(player, data.getEffect(), EffectInterruptionReason.PlayerDied);
        });
    }

    
    @EventHandler
    public void onMilkDrink(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item.getType() == Material.MILK_BUCKET) {
            CustomEffectManager.getAllPlayerAppliedEffects(player).forEach((data) -> {
                if (data.getEffect().getEffectData().isCanBeClearedByMilk())
                    CustomEffectManager.getInstance().interruptEffect(player, data.getEffect(), EffectInterruptionReason.PlayerDrinksMilk);
            });
        }
    }

}
