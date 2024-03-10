package ru.thisistails.tailslib.CustomBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import ru.thisistails.tailslib.CustomBlocks.Data.PlacedBlockData;

public class CustomBlockManager {

    private static @Getter CustomBlockManager instance;

    static {
        if (instance == null) {
            instance = new CustomBlockManager();
        }
    }

    private @Getter Map<String, CustomBlock> blocks;

    private @Getter List<PlacedBlockData> placedBlocks = new ArrayList<>();

    public void setPlacedBlocks(List<PlacedBlockData> datas) {
        Bukkit.getLogger().warning("Something want to rewrite PlacedBlock datas.");
        placedBlocks = datas;
    }

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

    public boolean registerPlacedBlockData(PlacedBlockData data) {
        if (placedBlocks.contains(data)) {
            Bukkit.getLogger().warning(data.toString() + " already exists. Skipping...");
            return false;
        }

        return placedBlocks.add(data);
    }

    public boolean placeBlock(@NotNull CustomBlock block, @NotNull Location location, @Nullable UUID owner) {
        location.getBlock().setType(block.getData().getBlockMaterial());

        PlacedBlockData data = new PlacedBlockData(location, block);
        data.setOwnerUuid(owner);
        return registerPlacedBlockData(data);
    }

    public boolean removePlacedBlock(PlacedBlockData data) {
        if (!placedBlocks.contains(data))
            return false; // Это чтобы не менять блоки просто так
        
        data.getLocation().getBlock().setType(Material.AIR);
        placedBlocks.remove(data);
        return true;
    }
    
}
