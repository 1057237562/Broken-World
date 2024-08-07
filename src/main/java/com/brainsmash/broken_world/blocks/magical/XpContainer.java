package com.brainsmash.broken_world.blocks.magical;

import com.brainsmash.broken_world.blocks.entity.magical.XpContainerEntity;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.ItemRegister;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class XpContainer extends BlockWithEntity {
    public XpContainer(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XpContainerEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem().equals(ItemRegister.bucket_item[6])) {
            if (!world.isClient) {
                XpContainerEntity entity = (XpContainerEntity) world.getBlockEntity(pos);
                if (entity != null) {
                    try (var transaction = Transaction.openOuter()) {
                        entity.xpStorage.insert(FluidVariant.of(FluidRegister.still_fluid[6]), FluidConstants.BUCKET,
                                transaction);
                        if (!player.isCreative()) player.setStackInHand(hand, Items.BUCKET.getDefaultStack());
                        transaction.commit();
                    }
                }
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
