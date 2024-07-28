package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.recipe.LuminInjectorRecipe;
import com.brainsmash.broken_world.recipe.util.ItemInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LuminInjectorEntity extends BlockEntity implements BlockEntityTicker<LuminInjectorEntity> {

    public List<ItemStack> itemStacks = new ArrayList<>();
    public List<Vec2f> shift = new ArrayList<>();
    public boolean crafting = false;
    public int tick = 0;
    public int progress = 0;
    public int maxProgress = 400;

    public LuminInjectorEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.LUMIN_INJECTOR_ENTITY_TYPE, pos, state);
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtCompound nbtArray = new NbtCompound();
        nbtArray.putInt("size", itemStacks.size());
        for (int i = 0; i < itemStacks.size(); i++) {
            nbtArray.put("item" + i, itemStacks.get(i).writeNbt(new NbtCompound()));
        }
        nbt.put("items", nbtArray);
        nbt.putInt("progress", progress);
        nbt.putBoolean("crafting", crafting);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        NbtCompound nbtArray = nbt.getCompound("items");
        int size = nbtArray.getInt("size");
        itemStacks.clear();
        for (int i = 0; i < size; i++) {
            itemStacks.add(ItemStack.fromNbt(nbtArray.getCompound("item" + i)));
        }
        progress = nbt.getInt("progress");
        crafting = nbt.getBoolean("crafting");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound compound = new NbtCompound();
        writeNbt(compound);
        return compound;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, LuminInjectorEntity blockEntity) {
        if (world instanceof ServerWorld serverWorld) {
            if (crafting) {
                progress++;
                if (progress >= maxProgress) {
                    progress = 0;
                    crafting = false;
                    Optional<LuminInjectorRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(
                            LuminInjectorRecipe.Type.INSTANCE, new ItemInventory(itemStacks), world);
                    optional.ifPresent(
                            recipe -> Block.dropStack(world, pos, recipe.craft(new ItemInventory(itemStacks))));
                    for (ItemStack stack : itemStacks) {
                        Block.dropStack(world, pos, stack);
                    }

                    itemStacks.clear();
                    shift.clear();
                }
                serverWorld.getChunkManager().markForUpdate(pos);
            }
        }
    }
}
