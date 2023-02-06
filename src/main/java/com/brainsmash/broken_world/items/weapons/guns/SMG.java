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

public class SMG extends Item implements GunBase {

    private float recoil = -0.8f;
    private float spread = 1f;

    private float spreadModifier = 5f;

    public SMG(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void fire(World world, PlayerEntity user) {

    }

    @Override
    public void fireTick(World world, PlayerEntity user) {
        if (user.getItemCooldownManager().isCoolingDown(this)) return;
        user.getItemCooldownManager().set(this, 1);

        if (!Util.getAmmo(user,
                ItemRegister.items[ItemRegistry.LIGHT_AMMO.ordinal()]).isEmpty() || user.getAbilities().creativeMode) {

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                    SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!world.isClient) {
                BulletEntity heavyAmmoEntity = new BulletEntity(world, user, 1.35f);

                float s = (float) (spread + user.getVelocity().length() * spreadModifier);
                heavyAmmoEntity.setVelocity(user, user.getPitch() + world.getRandom().nextFloat() * 2 * s - s,
                        user.getYaw() + world.getRandom().nextFloat() * 2 * s - s, 0.0f, 4f, 1.0f);
                world.spawnEntity(heavyAmmoEntity);
            }
            user.setPitch(user.getPitch() + recoil);
        }
        if (!user.getAbilities().creativeMode) {
            Util.getAmmo(user, ItemRegister.items[ItemRegistry.LIGHT_AMMO.ordinal()]).decrement(1);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
    }
}
