package com.brainsmash.broken_world.items.weapons.guns;

import com.brainsmash.broken_world.entity.BulletEntity;
import com.brainsmash.broken_world.items.weapons.Util;
import com.brainsmash.broken_world.registry.ItemRegister;
import com.brainsmash.broken_world.registry.enums.ItemRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Rifle extends Item {


    public Rifle(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, 3);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            if (!Util.getAmmo(user,
                    ItemRegister.items[ItemRegistry.HEAVY_AMMO.ordinal()]).isEmpty() || user.getAbilities().creativeMode) {
                BulletEntity lightAmmoEntity = new BulletEntity(world, user);
                lightAmmoEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 4f, 1.0f);
                world.spawnEntity(lightAmmoEntity);
            }
            if (!user.getAbilities().creativeMode) {
                Util.getAmmo(user, ItemRegister.items[ItemRegistry.HEAVY_AMMO.ordinal()]).decrement(1);
            }
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.pass(itemStack);
    }
}
