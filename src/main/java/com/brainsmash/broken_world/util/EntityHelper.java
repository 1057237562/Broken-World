package com.brainsmash.broken_world.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class EntityHelper {

    public static Position getOutputLocation(BlockPos pointer, Direction direction) {
        double d = pointer.getX() + 0.7 * (double) direction.getOffsetX();
        double e = pointer.getY() + 0.7 * (double) direction.getOffsetY();
        double f = pointer.getZ() + 0.7 * (double) direction.getOffsetZ();
        return new PositionImpl(d, e, f);
    }

    public static boolean spawnItem(World world, ItemStack stack, int speed, Direction side, BlockPos blockPos) {
        Position pos = getOutputLocation(blockPos, side);
        double d = pos.getX();
        double e = pos.getY();
        double f = pos.getZ();
        e = side.getAxis() == Direction.Axis.Y ? (e -= 0.125) : (e -= 0.15625);
        ItemEntity itemEntity = new ItemEntity(world, d, e, f, stack);
        double g = world.random.nextDouble() * 0.1 + 0.2;
        itemEntity.setVelocity(world.random.nextTriangular((double) side.getOffsetX() * g, 0.0172275 * (double) speed),
                world.random.nextTriangular(0.2, 0.0172275 * (double) speed),
                world.random.nextTriangular((double) side.getOffsetZ() * g, 0.0172275 * (double) speed));
        return world.spawnEntity(itemEntity);
    }

    public static boolean spawnItem(World world, ItemStack stack, int speed, Direction side, Position pos) {
        double d = pos.getX();
        double e = pos.getY();
        double f = pos.getZ();
        e = side.getAxis() == Direction.Axis.Y ? (e -= 0.125) : (e -= 0.15625);
        ItemEntity itemEntity = new ItemEntity(world, d, e, f, stack);
        double g = world.random.nextDouble() * 0.1 + 0.2;
        itemEntity.setVelocity(world.random.nextTriangular((double) side.getOffsetX() * g, 0.0172275 * (double) speed),
                world.random.nextTriangular(0.2, 0.0172275 * (double) speed),
                world.random.nextTriangular((double) side.getOffsetZ() * g, 0.0172275 * (double) speed));
        return world.spawnEntity(itemEntity);
    }

    public static Box calculateBoundsForPose(Entity entity, EntityPose pos) {
        EntityDimensions entityDimensions = entity.getDimensions(pos);
        float f = entityDimensions.width / 2.0f;
        Vec3d vec3d = new Vec3d(entity.getX() - (double) f, entity.getY(), entity.getZ() - (double) f);
        Vec3d vec3d2 = new Vec3d(entity.getX() + (double) f, entity.getY() + (double) entityDimensions.height,
                entity.getZ() + (double) f);
        return new Box(vec3d, vec3d2);
    }
}
