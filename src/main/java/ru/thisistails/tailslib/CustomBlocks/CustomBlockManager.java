package ru.thisistails.tailslib.CustomBlocks;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import ru.thisistails.tailslib.CustomBlocks.Data.PlacedBlockData;
import ru.thisistails.tailslib.CustomItems.CustomItem;
import ru.thisistails.tailslib.Exceptions.IDPatternException;
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
    private Pattern idPattern = Pattern.compile("^[a-z_]+$");

    private CustomBlockManager() {
        blocks = new HashMap<>();
    }

    public void register(@NotNull CustomBlock block) {
        if (blocks.containsValue(block)) {
            Bukkit.getLogger().warning("Seems like " + block.getData().getBlockId() + " is registering again. Skipping...");
            return;
        }

        String id = block.getData().getBlockId();

        Matcher matcher = idPattern.matcher(id);

        if (!matcher.find())
            throw new IDPatternException(id + " is not matching pattern " + idPattern.pattern());

        blocks.put(block.getData().getBlockId(), block);
        Bukkit.getLogger().info("Custom block " + block.getData().getBlockId() + " registered.");
    }

    public void loadBlocks() {
        Bukkit.getLogger().info("[BlockManager] Called block loading. Waiting " + String.valueOf(Config.getConfig().getInt("waitBeforeLoadBlocks")) + " before loading.");
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
        Logger logger = Bukkit.getLogger();
        
        try {
            File file = new File(PlacedBlocks.getSaveFilePath());
            if (!file.exists()) {
                if (!file.createNewFile()) 
                    logger.warning("Failed to create save file: " + file.getPath());
            }
            placedBlocks.save();
        } catch (IOException e) {
            logger.severe("An error occurred while saving placed blocks data: " + e.getMessage());
            e.printStackTrace();
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

        PlacedBlockData data = new PlacedBlockData(UUID.randomUUID(), location, block);
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
