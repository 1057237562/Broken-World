package com.brainsmash.broken_world.blocks.magical;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.PotionFluidKey;
import com.brainsmash.broken_world.blocks.entity.magical.CrucibleBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.BlockRegistry;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import it.unimi.dsi.fastutil.objects.AbstractObject2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public interface CrucibleBehavior {

    Map<Item, CauldronBehavior> CRUCIBLE_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CrucibleBehavior> BREW = createMap();

    static void registerBehaviour() {
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(ItemRegister.get(ItemRegistry.AMETHYST_POWDER),
                (state, world, pos, player, hand, stack) -> {
                    if (world instanceof ServerWorld serverWorld) {
                        Item item = stack.getItem();
                        stack.decrement(1);
                        player.incrementStat(Stats.FILL_CAULDRON);
                        player.incrementStat(Stats.USED.getOrCreateStat(item));
                        world.setBlockState(pos,
                                BlockRegister.get(BlockRegistry.CRUCIBLE).getDefaultState().with(CrucibleBlock.LEVEL,
                                        3));
                        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucibleBlockEntity) {
                            crucibleBlockEntity.fluidInv.setInvFluid(0,
                                    FluidKeys.get(Fluids.WATER).withAmount(FluidAmount.BUCKET), Simulation.ACTION);
                            crucibleBlockEntity.markDirty();
                            serverWorld.getChunkManager().markForUpdate(pos);
                        }
                        world.playSound((PlayerEntity) null, pos, SoundEvents.AMBIENT_UNDERWATER_EXIT,
                                SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.emitGameEvent((Entity) null, GameEvent.FLUID_PLACE, pos);
                    }

                    return ActionResult.success(world.isClient);
                });
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (PotionUtil.getPotion(stack) != Potions.WATER) {
                Item item = stack.getItem();
                if (world instanceof ServerWorld serverWorld) {
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(item));
                    world.setBlockState(pos, BlockRegister.get(BlockRegistry.CRUCIBLE).getDefaultState());
                    if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucibleBlockEntity) {
                        crucibleBlockEntity.fluidInv.setInvFluid(0,
                                FluidKeys.get(PotionUtil.getPotion(stack)).withAmount(FluidAmount.BOTTLE),
                                Simulation.ACTION);
                        crucibleBlockEntity.markDirty();
                        serverWorld.getChunkManager().markForUpdate(pos);
                    }
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                    player.setStackInHand(hand,
                            ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));

                }
                return ActionResult.success(world.isClient);
            } else {
                if (!world.isClient) {
                    Item item = stack.getItem();
                    player.setStackInHand(hand,
                            ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(item));
                    world.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState());
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                }

                return ActionResult.success(world.isClient);
            }
        });
        CRUCIBLE_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucibleBlockEntity) {
                if (crucibleBlockEntity.fluidInv.attemptInsertion(
                        FluidKeys.get(PotionUtil.getPotion(stack)).withAmount(FluidAmount.BOTTLE),
                        Simulation.SIMULATE).isEmpty()) {
                    if (world instanceof ServerWorld serverWorld) {
                        player.setStackInHand(hand,
                                ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        player.incrementStat(Stats.USE_CAULDRON);
                        player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                        world.setBlockState(pos, state.cycle(CrucibleBlock.LEVEL));
                        crucibleBlockEntity.fluidInv.insert(
                                FluidKeys.get(PotionUtil.getPotion(stack)).withAmount(FluidAmount.BOTTLE));
                        crucibleBlockEntity.markDirty();
                        serverWorld.getChunkManager().markForUpdate(pos);
                        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                    }

                    return ActionResult.success(world.isClient);
                }
            }
            return ActionResult.PASS;
        });
        CRUCIBLE_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (world instanceof ServerWorld serverWorld) {
                if (world.getBlockEntity(
                        pos) instanceof CrucibleBlockEntity crucibleBlockEntity && crucibleBlockEntity.fluidInv.getInvFluid(
                        0).getFluidKey() instanceof PotionFluidKey potionFluidKey) {
                    Item item = stack.getItem();
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player,
                            PotionUtil.setPotion(new ItemStack(Items.POTION), potionFluidKey.potion)));
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(item));
                    CrucibleBlock.decrementFluidLevel(state, world, pos);
                    crucibleBlockEntity.fluidInv.extract(FluidAmount.BOTTLE);
                    crucibleBlockEntity.markDirty();
                    serverWorld.getChunkManager().markForUpdate(pos);
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
                }
            }

            return ActionResult.success(world.isClient);
        });
    }

    ItemStack interact(BlockState state, World world, BlockPos pos, ItemStack input);

    static Object2ObjectOpenHashMap<Item, CrucibleBehavior> createMap() {
        return Util.make(new Object2ObjectOpenHashMap<>(), AbstractObject2ObjectFunction::defaultReturnValue);
    }
}
