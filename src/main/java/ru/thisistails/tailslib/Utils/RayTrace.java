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

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Помошник для рейтрейса объектов
 */
@RequiredArgsConstructor
public class RayTrace {

    /**
     * Опции для рейтрейса существ
     */
    @Data
    public class RayTraceEntitySettings {
        private List<Entity> ignore;
    }

    /**
     * Опции для частиц
     */
    @Data
    public class RayTraceParticleSettings {
        private Particle particleType;
        private int amount;
        private DustOptions dustOptions;
    }

    /**
     * Опции для рейтрейса блоков
     */
    @Data
    public class RayTraceBlockSettings {
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
     * Отправляет луч и проверяет ТОЛЬКО на существ.
     * @param settings  {@link RayTraceEntitySettings}
     * @return          {@link RayTraceResult} или null если ни с чем не столкнулось.
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
     * Отправляет луч и проверяет ТОЛЬКО на существ.
     * @param settings  {@link RayTraceEntitySettings}
     * @param particleOptions  {@link RayTraceParticleSettings}
     * @return  {@link RayTraceResult} или null если ни с чем не столкнулось.
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
     * Отправляет луч который сталкивается только с блоками
     * @param settings  {@link RayTraceBlockSettings}
     * @return                          {@link RayTraceResult} или null если ни с чем не столкнулось.
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
     * Отправляет луч который сталкивается только с блоками
     * @param settings  {@link RayTraceBlockSettings}
     * @param particleOptions  {@link RayTraceParticleSettings}
     * @return                          {@link RayTraceResult} или null если ни с чем не столкнулось.
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
     * Отправляет луч и проверяет на столкновение с чем угодно.
     * @param blockSettings {@link RayTraceBlockSettings}
     * @param entitySettings {@link RayTraceEntitySettings}
     * @return {@link RayTraceResult} или null если ни с чем не столкнулось.
     */
    public @Nullable RayTraceResult rayTraceAny(RayTraceBlockSettings blockSettings, RayTraceEntitySettings entitySettings) {
        return world.rayTrace(start, direction, raySize,
            blockSettings.getFluidCollisionMode(), blockSettings.isIgnorePassableBlocks(), 
            distance, (e) -> !(entitySettings.getIgnore().contains(e))
        );
    }

    /**
     * 
     * @param blockSettings {@link RayTraceBlockSettings}
     * @param entitySettings {@link RayTraceEntitySettings}
     * @param particleOptions {@link RayTraceParticleSettings}
     * @return {@link RayTraceResult} или null если ни с чем не столкнулось.
     */
    public @Nullable RayTraceResult rayTraceAny(RayTraceBlockSettings blockSettings, RayTraceEntitySettings entitySettings, RayTraceParticleSettings particleOptions) {
        for (int i = 0; i < distance; i++) {
            Location ss = start.add(direction);
            start.getWorld().spawnParticle(particleOptions.getParticleType(), ss, particleOptions.getAmount(), particleOptions.getDustOptions());
        }

        return rayTraceAny(blockSettings, entitySettings);
    }

}
