package ru.thisistails.tailslib.CustomBlocks.Data;

import java.util.UUID;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.annotations.Expose;

import lombok.Data;
import ru.thisistails.tailslib.CustomBlocks.CustomBlock;

@Data
public class PlacedBlockData {

    private @Expose UUID uuid = UUID.randomUUID();

    private @Expose @NotNull Location location;
    // Может быть null если блок был поставлен командой.
    private @Expose @Nullable UUID ownerUuid;
    private @Expose @NotNull CustomBlock placedBlock;
    
}
