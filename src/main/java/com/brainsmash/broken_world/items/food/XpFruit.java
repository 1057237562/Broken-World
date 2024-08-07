package com.brainsmash.broken_world.items.food;

import net.minecraft.block.Block;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class XpFruit extends AliasedBlockItem {
    public XpFruit(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world instanceof ServerWorld) {
            int i = 2 + world.random.nextInt(2) + world.random.nextInt(2);
            ExperienceOrbEntity.spawn((ServerWorld) world, user.getPos(), i);
        }
        return super.finishUsing(stack, world, user);
    }
}
