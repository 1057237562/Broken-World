package com.brainsmash.broken_world.items;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

public class HyperPocket extends Item {

    public HyperPocket(Settings settings) {
        super(settings);
    }

    public int convert(Direction direction) {
        switch (direction) {
            case EAST -> {
                return 1;
            }
            case SOUTH -> {
                return 2;
            }
            case WEST -> {
                return 3;
            }
            default -> {
                return 0;
            }
        }
    }

    public Direction convert(int rotate) {
        switch (rotate) {
            case 0 -> {
                return Direction.NORTH;
            }
            case 1 -> {
                return Direction.EAST;
            }
            case 2 -> {
                return Direction.SOUTH;
            }
            case 3 -> {
                return Direction.WEST;
            }
            default -> {
                return Direction.UP;
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        ItemStack itemStack = context.getStack();
        context.getPlayer().getItemCooldownManager().set(this, 20);
        if (!context.getWorld().isClient) {
            if (itemStack.hasNbt()) {
                StructureTemplate template = new StructureTemplate();
                NbtCompound compound = itemStack.getNbt();
                template.readNbt((NbtCompound) compound.get("structure"));

                int rotate = (4 + convert(context.getPlayerFacing()) - compound.getInt("direction")) % 4;

                BlockPos archor = context.getBlockPos().offset(Direction.Axis.Y, 1);

                switch (context.getPlayerFacing()) {
                    case NORTH -> archor = archor.add(-8, 0, -16);
                    case SOUTH -> archor = archor.add(-8, 0, 0);
                    case EAST -> archor = archor.add(0, 0, -8);
                    case WEST -> archor = archor.add(-16, 0, -8);
                }

                switch (rotate) {
                    case 1 -> archor = archor.add(16, 0, 0);
                    case 2 -> archor = archor.add(16, 0, 16);
                    case 3 -> archor = archor.add(0, 0, 16);
                }

                StructurePlacementData structurePlacementData = new StructurePlacementData().setMirror(
                        BlockMirror.NONE).setRotation(BlockRotation.values()[rotate]).setIgnoreEntities(true);
                template.place((ServerWorld) context.getWorld(), archor, archor, structurePlacementData,
                        StructureBlockBlockEntity.createRandom(((ServerWorld) context.getWorld()).getSeed()), 2);
                itemStack.setNbt(null);
            } else {
                Direction direction = context.getPlayerFacing();
                BlockPos startPos = context.getBlockPos().offset(direction.rotateYCounterclockwise(), 8).offset(
                        Direction.Axis.Y, 1);
                BlockPos endPos = context.getBlockPos().offset(direction.rotateYClockwise(), 8).offset(direction,
                        16).offset(Direction.Axis.Y, 17);
                NbtCompound compound = new NbtCompound();
                compound.put("structure", saveStructure((ServerWorld) context.getWorld(),
                        new BlockPos(Math.min(startPos.getX(), endPos.getX()), startPos.getY(),
                                Math.min(startPos.getZ(), endPos.getZ())), new Vec3i(16, 16, 16)));
                compound.putInt("direction", convert(direction));
                itemStack.setNbt(compound);
                for (BlockPos pos : BlockPos.iterate(startPos, endPos)) {
                    context.getWorld().removeBlockEntity(pos);
                    context.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState(),
                            Block.FORCE_STATE | Block.SKIP_DROPS | Block.NOTIFY_LISTENERS);
                }
            }
        }
        context.getPlayer().incrementStat(Stats.USED.getOrCreateStat(this));
        return ActionResult.SUCCESS;
    }

    public NbtCompound saveStructure(ServerWorld serverWorld, BlockPos start, Vec3i dimension) {
        StructureTemplate structureTemplate = new StructureTemplate();
        structureTemplate.saveFromWorld(serverWorld, start, dimension, false, Blocks.STRUCTURE_VOID);
        NbtCompound nbtCompound = structureTemplate.writeNbt(new NbtCompound());
        return nbtCompound;
    }
}
