package com.brainsmash.broken_world.items.magical;

import com.brainsmash.broken_world.blocks.multiblock.Multiblock;
import com.brainsmash.broken_world.screenhandlers.descriptions.WandGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Wand extends Item {

    public ScreenHandlerType<WandGuiDescription> screenHandlerType;
    public static Text CONTAINER_NAME = Text.translatable("container.wand");
    final int size;

    public Wand(Settings settings, ScreenHandlerType<WandGuiDescription> type, int size) {
        super(settings);
        screenHandlerType = type;
        this.size = size;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (user.isSneaking() && hand == Hand.MAIN_HAND) {
            user.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, player) -> new WandGuiDescription(screenHandlerType, syncId, inventory, size),
                    CONTAINER_NAME));
        } else {
            NbtCompound nbtCompound = user.getStackInHand(hand).getOrCreateNbt();
            if (nbtCompound != null && !nbtCompound.isEmpty())
                for (NbtElement element : nbtCompound.getList("inventory", NbtElement.COMPOUND_TYPE)) {
                    ItemStack rune = ItemStack.fromNbt((NbtCompound) element);

                    // TODO : Apply Magic Rune Interpreter
                }
        }
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Multiblock.revertToBlock(context.getWorld(), context.getBlockPos(), new Vec3i(3, 3, 3));
        return super.useOnBlock(context);
    }
}
