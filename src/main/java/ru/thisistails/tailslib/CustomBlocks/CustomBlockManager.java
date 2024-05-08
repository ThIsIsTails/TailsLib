package ru.thisistails.tailslib.CustomBlocks;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import ru.thisistails.tailslib.CustomBlocks.Data.PlacedBlockData;
import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.Tools.ChatTools;
import ru.thisistails.tailslib.Tools.Config;

public class CustomBlockManager implements Serializable {

    private static @Getter CustomBlockManager instance;

    static {
        if (instance == null) {
            instance = new CustomBlockManager();
        }
    }

    private @Getter Map<String, CustomBlock> blocks;
    private @Getter PlacedBlocks placedBlocks = new PlacedBlocks();

    private CustomBlockManager() {
        blocks = new HashMap<>();
    }

    public void register(@NotNull CustomBlock block) {
        if (blocks.containsValue(block)) {
            Bukkit.getLogger().warning("Seems like " + block.getData().getBlockId() + " is registering again. Skipping...");
            return;
        }

        blocks.put(block.getData().getBlockId(), block);
        Bukkit.getLogger().info("Custom block " + block.getData().getBlockId() + " registered.");
    }

    public void loadBlocks() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // Нужно чтобы сервер успел загрузить блоки
                try {
                    TimeUnit.SECONDS.sleep(Config.getConfig().getInt("waitBeforeLoadBlocks"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                placedBlocks = PlacedBlocks.load();
        
                if (placedBlocks == null) {
                    Bukkit.getLogger().info("Failed to load placed blocks.");
                    placedBlocks = new PlacedBlocks();
                }
            }
        });

        thread.start();
    }

    public void savePlacedBlocksDatas() {
        try {
            placedBlocks.save();
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Failed to save PlacedBlocks.");
            ChatTools.sendAll("Неудалось сохранить PlacedBlocks. " + e.getMessage());
        }
    }

    public void registerPlacedBlockData(PlacedBlockData data) {
        if (placedBlocks.containsPlacedBlock(data)) {
            Bukkit.getLogger().warning(data.toString() + " already exists. Skipping...");
            return;
        }

        placedBlocks.addPlacedBlock(data);
    }

    public void placeBlock(@NotNull CustomBlock block, @NotNull Location location, @Nullable UUID owner) {
        location.getBlock().setType(block.getData().getBlockMaterial());

        PlacedBlockData data = new PlacedBlockData(location, block);
        data.setOwnerUuid(owner);
        registerPlacedBlockData(data);
    }

    public boolean removePlacedBlock(PlacedBlockData data) {
        if (!placedBlocks.containsPlacedBlock(data))
            return false; // Это чтобы не менять блоки просто так
        
        data.getLocation().getBlock().setType(Material.AIR);
        placedBlocks.removePlacedBlock(data);
        return true;
    }

    public @Nullable CustomBlock getCustomBlockByCustomItem(CustomItem item) {
        for (CustomBlock cblock : blocks.values()) {
            if (cblock.getData().getItem() == item)
                return cblock;
        }

        return null;
    }
    
}
