package com.brainsmash.broken_world.items.magical;

import com.brainsmash.broken_world.entity.SpellEntity;
import com.brainsmash.broken_world.entity.impl.PlayerDataExtension;
import com.brainsmash.broken_world.items.CustomUsePoseItem;
import com.brainsmash.broken_world.screenhandlers.descriptions.WandGuiDescription;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Wand extends Item implements CustomUsePoseItem {

    public static Text CONTAINER_NAME = Text.translatable("container.wand");
    final int size;
    public ScreenHandlerType<WandGuiDescription> screenHandlerType;

    public Wand(Settings settings, ScreenHandlerType<WandGuiDescription> type, int size) {
        super(settings);
        screenHandlerType = type;
        this.size = size;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.getOffHandStack().isEmpty()) return TypedActionResult.fail(user.getStackInHand(hand));
        if (user.isSneaking() && hand == Hand.MAIN_HAND) {
            user.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, player) -> new WandGuiDescription(screenHandlerType, syncId, inventory, size),
                    CONTAINER_NAME));
        } else {
//            NbtCompound nbtCompound = user.getStackInHand(hand).getOrCreateNbt();
//            if (nbtCompound != null && !nbtCompound.isEmpty()) {
//                for (NbtElement element : nbtCompound.getList("inventory", NbtElement.COMPOUND_TYPE)) {
//                    ItemStack rune = ItemStack.fromNbt((NbtCompound) element);
//
//                    // TODO : Apply Magic Rune Interpreter
//                }
//            }
            if (!world.isClient()) {
                world.spawnEntity(new SpellEntity(user, world));
            }
        }
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {

        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world instanceof ServerWorld && user instanceof PlayerDataExtension dataExtension) {
            SpellEntity spellEntity = dataExtension.getSpellEntity();
//            Vec3d normal = user.getEyePos().add(spellEntity.normal.multiply(2));
//            world.addParticle(ParticleRegister.MAGIC_SPELL_TYPE, true, normal.x, normal.y, normal.z, 0, 0, 0);
            spellEntity.discard();
        }

        return stack;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (world instanceof ServerWorld && user instanceof PlayerDataExtension dataExtension) {
            SpellEntity spellEntity = dataExtension.getSpellEntity();
//            Vec3d normal = user.getEyePos().add(spellEntity.normal.multiply(2));
//            world.addParticle(ParticleRegister.MAGIC_SPELL_TYPE, true, normal.x, normal.y, normal.z, 0, 0, 0);
            spellEntity.discard();
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public BipedEntityModel.ArmPose getUsePose() {
        return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}
