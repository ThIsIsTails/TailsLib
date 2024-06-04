package ru.thisistails.tailslib.Utils;

import java.util.List;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * RayTrace helper
 */
@AllArgsConstructor
public class RayTrace {

    /**
     * Options for entity raytrace
     */
    @Data
    @AllArgsConstructor
    public static class RayTraceEntitySettings {
        private List<Entity> ignore;
    }

    /**
     * Options for particles
     */
    @Data
    @AllArgsConstructor
    public static class RayTraceParticleSettings {
        private Particle particleType;
        private int amount;
        private DustOptions dustOptions;
    }

    /**
     * Options for block raytrace
     */
    @Data
    @AllArgsConstructor
    public static class RayTraceBlockSettings {
        private FluidCollisionMode fluidCollisionMode;
        private boolean ignorePassableBlocks;
    }

    //---------------------------\\

    private int distance;
    private double raySize;
    private Location start;
    private Vector direction;
    private World world;

    //---------------------------\\

    /**
     * Sends a beam and checks ONLY against creatures.
     * @param settings  {@link RayTraceEntitySettings}
     * @return          {@link RayTraceResult} or null if you haven't run into anything.
     */
    public @Nullable RayTraceResult rayTraceEntity(RayTraceEntitySettings settings) {
        return world.rayTraceEntities(
                start,
                direction,
                raySize,
                (double) distance,
                (e) -> !(settings.getIgnore().contains(e))
        );
    }

    /**
     * Sends a beam and checks ONLY against creatures.
     * @param settings  {@link RayTraceEntitySettings}
     * @param particleOptions  {@link RayTraceParticleSettings}
     * @return  {@link RayTraceResult} or null if you haven't run into anything.
     */
    public @Nullable RayTraceResult rayTraceEntity(RayTraceEntitySettings settings, RayTraceParticleSettings particleOptions) {
        RayTraceResult result = rayTraceEntity(settings);

        for (int i = 0; i < distance; i++) {
            Location ss = start.add(direction);
            start.getWorld().spawnParticle(particleOptions.getParticleType(), ss, particleOptions.getAmount(), particleOptions.getDustOptions());
        }

        return result;
    }

    //---------------------------\\

    /**
     * Sends a beam that only collides with blocks
     * @param settings  {@link RayTraceBlockSettings}
     * @return                          {@link RayTraceResult} or null if you haven't run into anything.
     */
    public @Nullable RayTraceResult rayTraceBlock(RayTraceBlockSettings settings) {
        return world.rayTraceBlocks(
                start,
                direction,
                (double) distance,
                settings.getFluidCollisionMode(),
                settings.isIgnorePassableBlocks()
        );
    }

    /**
     * Sends a beam that only collides with blocks
     * @param settings  {@link RayTraceBlockSettings}
     * @param particleOptions  {@link RayTraceParticleSettings}
     * @return                          {@link RayTraceResult} or null if you haven't run into anything.
     */
    public @Nullable RayTraceResult rayTraceBlock(RayTraceBlockSettings settings, RayTraceParticleSettings particleOptions) {
        for (int i = 0; i < distance; i++) {
            Location ss = start.add(direction);
            start.getWorld().spawnParticle(particleOptions.getParticleType(), ss, particleOptions.getAmount(), particleOptions.getDustOptions());
        }

        return rayTraceBlock(settings);
    }

    //---------------------------\\

    /**
     * Sends out a beam and checks for collision with anything.
     * @param blockSettings {@link RayTraceBlockSettings}
     * @param entitySettings {@link RayTraceEntitySettings}
     * @return {@link RayTraceResult} or null if you haven't run into anything.
     */
    public @Nullable RayTraceResult rayTraceAny(RayTraceBlockSettings blockSettings, RayTraceEntitySettings entitySettings) {
        return world.rayTrace(start, direction, raySize,
            blockSettings.getFluidCollisionMode(), blockSettings.isIgnorePassableBlocks(), 
            distance, (e) -> !(entitySettings.getIgnore().contains(e))
        );
    }

    /**
     * Sends out a beam and checks for collision with anything.
     * @param blockSettings {@link RayTraceBlockSettings}
     * @param entitySettings {@link RayTraceEntitySettings}
     * @param particleOptions {@link RayTraceParticleSettings}
     * @return {@link RayTraceResult} or null if you haven't run into anything.
     */
    public @Nullable RayTraceResult rayTraceAny(RayTraceBlockSettings blockSettings, RayTraceEntitySettings entitySettings, RayTraceParticleSettings particleOptions) {
        for (int i = 0; i < distance; i++) {
            Location ss = start.add(direction);
            start.getWorld().spawnParticle(particleOptions.getParticleType(), ss, particleOptions.getAmount(), particleOptions.getDustOptions());
        }

        return rayTraceAny(blockSettings, entitySettings);
    }

}
