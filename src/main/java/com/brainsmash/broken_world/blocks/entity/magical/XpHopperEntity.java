package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.enums.FluidRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

import static com.brainsmash.broken_world.registry.BlockRegister.XP_HOPPER_ENTITY_TYPE;

public class XpHopperEntity extends XpContainerEntity implements BlockEntityTicker<XpHopperEntity> {
    private double radius = 8.0;

    public XpHopperEntity(BlockPos pos, BlockState state) {
        super(XP_HOPPER_ENTITY_TYPE, pos, state);
    }


    @Override
    public void tick(World world, BlockPos pos, BlockState state, XpHopperEntity blockEntity) {
        if (world instanceof ServerWorld) {
            List<ExperienceOrbEntity> list = this.world.getEntitiesByType(
                    TypeFilter.instanceOf(ExperienceOrbEntity.class), new Box(pos).expand(radius),
                    experienceOrbEntity -> experienceOrbEntity.getPos().subtract(pos.getX(), pos.getY(),
                            pos.getZ()).length() <= radius);
            Iterator var2 = list.iterator();

            while (var2.hasNext()) {
                ExperienceOrbEntity experienceOrbEntity = (ExperienceOrbEntity) var2.next();
                experienceOrbEntity.addVelocity((pos.getX() - experienceOrbEntity.getX()) * 0.01f,
                        (pos.getY() - experienceOrbEntity.getY()) * 0.01f,
                        (pos.getZ() - experienceOrbEntity.getZ()) * 0.01f);
                if (new Box(pos).expand(0.5).contains(experienceOrbEntity.getPos())) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        this.xpStorage.insert(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)),
                                experienceOrbEntity.getExperienceAmount() * FluidConstants.BOTTLE / 16, transaction);
                        experienceOrbEntity.discard();
                    }
                }
            }
        }
    }
}
