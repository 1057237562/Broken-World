package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class MortarBlockEntity extends BlockEntity {

    private final int MAX_GRIND_TIME = 100;
    private int grindTime = 0;
    private ItemStack grindItem = ItemStack.EMPTY;
    private ItemStack outputItem = ItemStack.EMPTY;

    public MortarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.MORTAR_ENTITY_TYPE, pos, state);
    }

    public void grind(PlayerEntity player, Hand hand) {
        grindTime++;
        if (grindTime >= MAX_GRIND_TIME) {
            outputItem = new ItemStack(grindItem.getItem(), grindItem.getCount() + 1);
            grindItem.decrement(1);
            grindTime = 0;
        }
    }

    public void setGrindItem(ItemStack itemStack) {
        this.grindItem = itemStack;
    }

    public ItemStack getGrindItem() {
        return grindItem;
    }

    public ItemStack takeGrindItem() {
        ItemStack itemStack;
        if (!outputItem.isEmpty()) {
            itemStack = outputItem;
            outputItem = ItemStack.EMPTY;
        } else {
            itemStack = grindItem;
            grindItem = ItemStack.EMPTY;
        }
        return itemStack;
    }
}
