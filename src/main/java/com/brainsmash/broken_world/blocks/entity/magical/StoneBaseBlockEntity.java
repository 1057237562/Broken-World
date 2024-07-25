package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.FluidRegister;
import com.brainsmash.broken_world.registry.enums.FluidRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StoneBaseBlockEntity extends BlockEntity implements BlockEntityTicker<StoneBaseBlockEntity> {

    public ItemStack itemStack = ItemStack.EMPTY;
    public float tick = 0;
    public boolean crafting = false;
    public int progress = 0;
    public final int maxProgress = 100;

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("item", itemStack.writeNbt(new NbtCompound()));
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        itemStack = ItemStack.fromNbt(nbt.getCompound("item"));
    }

    public StoneBaseBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.STONE_BASE_ENTITY_TYPE, pos, state);
    }


    public void startCrafting() {
        if (!world.isClient) {
            crafting = true;
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound compound = new NbtCompound();
        writeNbt(compound);
        return compound;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, StoneBaseBlockEntity blockEntity) {
        if (crafting) {
            if (world.getBlockEntity(pos.offset(Direction.DOWN)) instanceof XpContainerEntity containerEntity) {
                try (Transaction transaction = Transaction.openOuter()) {
                    if (containerEntity.xpStorage.simulateExtract(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)),
                            FluidConstants.BOTTLE / 100, null) == FluidConstants.BOTTLE / 100) {
                        containerEntity.xpStorage.extract(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)),
                                FluidConstants.BOTTLE / 100, transaction);
                        progress++;
                    }
                    transaction.commit();
                }
                if (progress == maxProgress) {
                    progress = 0;
                    crafting = false;

                    // TODO : Implement the crafting logic
                }
            }
        }
    }
}
