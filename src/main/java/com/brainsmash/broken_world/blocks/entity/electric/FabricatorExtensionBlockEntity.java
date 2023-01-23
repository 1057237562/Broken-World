package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FabricatorExtensionBlockEntity extends FabricatorBlockEntity {

    public FabricatorExtensionBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            BlockState blockState = world.getBlockState(pos);
            BlockEntity entity = world.getBlockEntity(pos.offset(blockState.get(Properties.HORIZONTAL_FACING)));
            if (entity instanceof FabricatorBlockEntity source) {
                powered = source.isPowered();
                if (!source.getItems().get(18).isEmpty()) {
                    if (insertItem(new ItemStack(source.getItems().get(18).getItem(), 1))) {
                        source.getItems().get(18).decrement(1);
                    }
                }
            } else {
                powered = false;
            }
        }
        super.tick(world, pos, state, blockEntity);
    }
}
