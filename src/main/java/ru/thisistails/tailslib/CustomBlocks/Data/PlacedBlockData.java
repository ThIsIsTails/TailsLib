package ru.thisistails.tailslib.CustomBlocks.Data;

import java.util.UUID;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ru.thisistails.tailslib.CustomBlocks.CustomBlock;

@Data
public class PlacedBlockData {

    private @Setter(AccessLevel.NONE) UUID uuid = UUID.randomUUID();

    private @NotNull Location location;
    // Может быть null если блок был поставлен командой.
    private @Nullable UUID ownerUuid;
    private @NotNull CustomBlock placedBlock;
    
}
