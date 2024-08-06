package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.InfusingTableGuiDescription;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class InfusingTableEntity extends BlockEntity implements NamedScreenHandlerFactory, Nameable {
    public static final int PROPERTY_COUNT = 2;

    protected Text customName;

    public InfusingTableEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.INFUSION_TABLE_ENTITY_TYPE, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new InfusingTableGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public Text getName() {
        if (this.customName != null) {
            return this.customName;
        }
        return Text.translatable("container.infuse");
    }

    public void setCustomName(@Nullable Text customName) {
        this.customName = customName;
    }

    @Override
    @Nullable
    public Text getCustomName() {
        return this.customName;
    }
}