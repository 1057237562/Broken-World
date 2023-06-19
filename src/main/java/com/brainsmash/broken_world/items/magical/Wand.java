package com.brainsmash.broken_world.items.magical;

import com.brainsmash.broken_world.screenhandlers.WandScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Wand extends Item {

    public int runeSize = 9;
    public static Text CONTAINER_NAME = Text.translatable("container.wand");

    public Wand(Settings settings, int size) {
        super(settings);
        runeSize = size;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (user.isSneaking() && hand == Hand.MAIN_HAND) {
            user.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, player) -> new WandScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId,
                            inventory, 1), CONTAINER_NAME));
        } else {
            NbtCompound nbtCompound = user.getStackInHand(hand).getNbt();
            for (NbtElement element : nbtCompound.getList("inventory", NbtElement.COMPOUND_TYPE)) {

            }
        }
        return super.use(world, user, hand);
    }
}
