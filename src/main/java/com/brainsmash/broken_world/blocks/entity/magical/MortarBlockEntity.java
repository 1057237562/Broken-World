package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.recipe.GrindRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.util.EntityHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class MortarBlockEntity extends BlockEntity {

    public final int MAX_GRIND_TIME = 100;
    public int grindTime = 0;
    private ItemStack grindItem = ItemStack.EMPTY;
    private ItemStack outputItem = ItemStack.EMPTY;

    public MortarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.MORTAR_ENTITY_TYPE, pos, state);
    }

    public void grind(PlayerEntity player, Hand hand) {
        if (world instanceof ServerWorld serverWorld) {
            grindTime++;
            if (grindTime >= MAX_GRIND_TIME) {
                if (outputItem.isEmpty()) outputItem = GrindRecipe.GRINDING_RECIPES.get(grindItem.getItem()).copy();
                else if (outputItem.isOf(GrindRecipe.GRINDING_RECIPES.get(grindItem.getItem()).getItem()))
                    outputItem.increment(GrindRecipe.GRINDING_RECIPES.get(grindItem.getItem()).getCount());
                else EntityHelper.spawnItem(world, GrindRecipe.GRINDING_RECIPES.get(grindItem.getItem()).copy(), 1,
                            Direction.UP, pos);
                grindItem.decrement(1);
                grindTime = 0;
            }
            markDirty();
            serverWorld.getChunkManager().markForUpdate(this.getPos());
        }
    }

    public void setGrindItem(ItemStack itemStack) {
        if (world instanceof ServerWorld serverWorld) {
            this.grindItem = itemStack;
            markDirty();
            serverWorld.getChunkManager().markForUpdate(this.getPos());
        }
    }

    public ItemStack getGrindItem() {
        return grindItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public ItemStack takeGrindItem() {
        if (world instanceof ServerWorld serverWorld) {
            ItemStack itemStack;
            if (!outputItem.isEmpty()) {
                itemStack = outputItem;
                outputItem = ItemStack.EMPTY;
            } else {
                itemStack = grindItem;
                grindItem = ItemStack.EMPTY;
            }
            markDirty();
            serverWorld.getChunkManager().markForUpdate(this.getPos());
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("grindItem", grindItem.writeNbt(new NbtCompound()));
        nbt.putInt("grindTime", grindTime);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        grindItem = ItemStack.fromNbt(nbt.getCompound("grindItem"));
        grindTime = nbt.getInt("grindTime");
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
}
