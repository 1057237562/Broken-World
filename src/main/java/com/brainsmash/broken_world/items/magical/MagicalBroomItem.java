package com.brainsmash.broken_world.items.magical;

import com.brainsmash.broken_world.registry.EntityRegister;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public class MagicalBroomItem extends Item {
    public MagicalBroomItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld() instanceof ServerWorld serverWorld) {
            if (EntityRegister.MAGIC_BROOM_ENTITY_TYPE.spawnFromItemStack(serverWorld, context.getStack(),
                    context.getPlayer(), context.getBlockPos(), SpawnReason.SPAWN_EGG, true, false) != null) {
                context.getStack().decrement(1);
            }
        }
        return ActionResult.SUCCESS;
    }
}
