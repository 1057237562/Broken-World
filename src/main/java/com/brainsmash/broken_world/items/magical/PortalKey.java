package com.brainsmash.broken_world.items.magical;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PortalKey extends Item {

    public PortalKey(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                for (int k = -1; k < 1; k++) {
                    BlockPos pos = context.getBlockPos().add(i, j, k);
                }
            }
        }
        return super.useOnBlock(context);
    }
}
