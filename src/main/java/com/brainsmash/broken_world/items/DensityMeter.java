package com.brainsmash.broken_world.items;

import com.brainsmash.broken_world.worldgen.BWDensityFunctionTypes;
import com.brainsmash.broken_world.worldgen.Pos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DensityMeter extends Item {

    public DensityMeter(Settings settings) { super(settings); }
    String message;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            BlockPos blockPos = user.getBlockPos();
            Pos pos = new Pos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            message = "At block pos " + blockPos + '\n';
            BWDensityFunctionTypes.Debug.TAGS.forEach((tag, debug) ->
                    DensityMeter.this.message += "Tag: " + tag + ", output: " + debug.argument().sample(pos) + '\n'
           );
            user.sendMessage(Text.of(message));
        }
        return TypedActionResult.success(itemStack);
    }
}
