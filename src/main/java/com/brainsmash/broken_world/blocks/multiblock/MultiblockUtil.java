package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.util.SerializationHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MultiblockUtil {
    public static Map<Identifier, MultiblockPattern> patternMap = new HashMap<>();
    public static Logger LOGGER = LoggerFactory.getLogger(MultiblockUtil.class);

    public static void revertToBlock(World world, BlockPos pos, Vec3i sz) {
        for (int x = 0; x < sz.getX(); x++) {
            for (int y = 0; y < sz.getY(); y++) {
                for (int z = 0; z < sz.getZ(); z++) {
                    BlockPos p = pos.add(x, y, z);
                    if (!(world.getBlockEntity(p) instanceof DummyBlockEntity)) continue;
                    BlockState originalBlock = ((DummyBlockEntity) world.getBlockEntity(p)).getImitateBlockState();
                    if (originalBlock.getBlock() == Blocks.AIR) continue;
                    NbtCompound nbt = ((DummyBlockEntity) world.getBlockEntity(p)).getImitateNbt();
                    world.setBlockState(p, originalBlock);
                    if (originalBlock.getBlock() instanceof BlockWithEntity bwe) {
                        world.getBlockEntity(p).readNbt(nbt);
                    }
                }
            }
        }
    }

    public static void tryAssemble(World world, Identifier identifier, BlockPos pos, BlockPos archor) {
        MultiblockPattern pattern = patternMap.get(identifier);
        if (pattern == null) {
            LOGGER.warn("MultiblockPattern {} does not exist", identifier);
            return;
        }
        for (BlockRotation rotation : BlockRotation.values()) {
            if (pattern.test(world, pos, rotation, archor)) {
                convertToDummy(world, pos.subtract(archor.rotate(rotation)), pattern.size);
                return;
            }
        }
    }

    public static void convertToDummy(World world, BlockPos pos, Vec3i sz) {
        for (int x = 0; x < sz.getX(); x++) {
            for (int y = sz.getY() - 1; y >= 0; y--) {
                for (int z = 0; z < sz.getZ(); z++) {
                    BlockPos p = pos.add(x, y, z);
                    NbtCompound originalBlock = SerializationHelper.saveBlockState(world.getBlockState(p));
                    NbtCompound nbt = new NbtCompound();
                    BlockEntity originalBlockEntity;
                    if (world.getBlockState(p).getBlock() instanceof BlockWithEntity bwe) {
                        originalBlockEntity = world.getBlockEntity(p);
                        nbt = originalBlockEntity.createNbt();
                        world.removeBlockEntity(p);
                    }
                    if (world.getBlockState(p).getBlock() == Blocks.AIR) continue;
                    if (x == 0 && y == 0 && z == 0) {
                        world.setBlockState(p, BlockRegister.multiblock.getDefaultState());
                        ((MultiblockEntity) world.getBlockEntity(p)).setMultiblockSize(sz);
                    } else world.setBlockState(p, BlockRegister.dummy.getDefaultState());
                    ((DummyBlockEntity) world.getBlockEntity(p)).setImitateBlock(
                            SerializationHelper.loadBlockState(originalBlock), nbt);
                    ((DummyBlockEntity) world.getBlockEntity(p)).setLink(pos);
                }
            }
        }
    }

}
