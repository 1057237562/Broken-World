package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Multiblock {
    public Multiblock(World world) {

    }

    public boolean onDisassemble(boolean forced) {
        return false;
    }

    public static void revertToBlock(World world, BlockPos pos, Vec3i sz) {
        for (int x = 0; x < sz.getX(); x++) {
            for (int y = 0; y < sz.getY(); y++) {
                for (int z = 0; z < sz.getZ(); z++) {
                    BlockPos p = pos.add(x, y, z);
                    if (!(world.getBlockEntity(p) instanceof DummyBlockEntity)) continue;
                    Block originalBlock = ((DummyBlockEntity) world.getBlockEntity(p)).getImitateBlock();
                    if (originalBlock == Blocks.AIR) continue;
                    NbtCompound nbt = ((DummyBlockEntity) world.getBlockEntity(p)).getImitateNbt();
                    world.setBlockState(p, originalBlock.getDefaultState());
                    if (originalBlock instanceof BlockWithEntity bwe) {
                        world.getBlockEntity(p).readNbt(nbt);
                    }
                }
            }
        }
    }

    public static void convertToDummy(World world, BlockPos pos, Vec3i sz) {
        for (int x = 0; x < sz.getX(); x++) {
            for (int y = 0; y < sz.getY(); y++) {
                for (int z = 0; z < sz.getZ(); z++) {
                    BlockPos p = pos.add(x, y, z);
                    Block originalBlock = world.getBlockState(p).getBlock();
                    NbtCompound nbt = new NbtCompound();
                    BlockEntity originalBlockEntity;
                    if (originalBlock instanceof BlockWithEntity bwe) {
                        originalBlockEntity = world.getBlockEntity(p);
                        nbt = originalBlockEntity.createNbt();
                    }
                    if (originalBlock == Blocks.AIR) continue;
                    world.setBlockState(p, BlockRegister.dummy.getDefaultState(), 32);
                    ((DummyBlockEntity) world.getBlockEntity(p)).setImitateBlock(originalBlock, nbt);
                }
            }
        }
    }
}
