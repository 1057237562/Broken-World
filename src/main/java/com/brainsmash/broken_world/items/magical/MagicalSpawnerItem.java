package com.brainsmash.broken_world.items.magical;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

public class MagicalSpawnerItem extends BlockItem {
    public MagicalSpawnerItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        NbtCompound blockNbt = new NbtCompound();
        NbtCompound entityNbt = new NbtCompound();
        entityNbt.putString("id", Registry.ENTITY_TYPE.getId(entity.getType()).toString());
        blockNbt.put("spawnEntity", entityNbt);
        user.getStackInHand(hand).setSubNbt("BlockEntityTag", blockNbt);
        return ActionResult.SUCCESS;
    }
}
