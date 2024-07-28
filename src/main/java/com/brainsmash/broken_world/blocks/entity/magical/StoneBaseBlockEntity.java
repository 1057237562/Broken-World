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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StoneBaseBlockEntity extends BlockEntity implements BlockEntityTicker<StoneBaseBlockEntity> {

    public ItemStack itemStack = ItemStack.EMPTY;
    public float tick = 0;
    public boolean crafting = false;
    public int progress = 0;
    public BlockPos linkPos = BlockPos.ORIGIN;
    public final int maxProgress = 200;
    public boolean isBlack;

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("item", itemStack.writeNbt(new NbtCompound()));
        nbt.putInt("progress", progress);
        nbt.putBoolean("crafting", crafting);
        nbt.putLong("linkPos", linkPos.asLong());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        itemStack = ItemStack.fromNbt(nbt.getCompound("item"));
        progress = nbt.getInt("progress");
        crafting = nbt.getBoolean("crafting");
        linkPos = BlockPos.fromLong(nbt.getLong("linkPos"));
    }

    public StoneBaseBlockEntity(BlockPos pos, BlockState state, boolean isBlack) {
        super(isBlack ? BlockRegister.BLACK_STONE_BASE_ENTITY_TYPE : BlockRegister.STONE_BASE_ENTITY_TYPE, pos, state);
        this.isBlack = isBlack;
    }


    public void startCrafting(BlockPos blockPos) {
        if (world instanceof ServerWorld serverWorld) {
            crafting = true;
            linkPos = blockPos;
            serverWorld.getChunkManager().markForUpdate(pos);
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
            if (world instanceof ClientWorld clientWorld) {
                ++progress;
                for (int i = 0; i < 360; i += 90) {
                    double dx = 0.3 * Math.sin((clientWorld.getTime() * 4 + i) * Math.PI / 180), dz = 0.3 * Math.cos(
                            (clientWorld.getTime() * 4 + i) * Math.PI / 180);
                    world.addParticle(ParticleTypes.ENCHANT, pos.getX() + 0.5 + dx,
                            pos.getY() + (isBlack ? 0.5 - (double) progress / maxProgress : 0.75),
                            pos.getZ() + 0.5 + dz, 0, 0.5 + (double) progress / maxProgress, 0);
                }
                if (progress == maxProgress) {
                    progress = 0;
                    crafting = false;
                    if (isBlack && clientWorld.getBlockEntity(linkPos) instanceof DimInfuserEntity entity) {
                        entity.shift.add(new Vec2f(pos.getX() - linkPos.getX(), pos.getZ() - linkPos.getZ()));
                        entity.vitemStacks.add(itemStack);
                    }
                    if (!isBlack && clientWorld.getBlockEntity(linkPos) instanceof LuminInjectorEntity entity) {
                        entity.shift.add(new Vec2f(pos.getX() - linkPos.getX(), pos.getZ() - linkPos.getZ()));
                        entity.vitemStacks.add(itemStack);
                    }
                    itemStack = ItemStack.EMPTY;
                }
            }
            if (world instanceof ServerWorld serverWorld) {
                if (world.getBlockEntity(pos.offset(
                        isBlack ? Direction.UP : Direction.DOWN)) instanceof XpContainerEntity containerEntity) {
                    if (containerEntity.xpStorage.simulateExtract(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)),
                            FluidConstants.BOTTLE / 100, null) == FluidConstants.BOTTLE / 100) {
                        try (Transaction transaction = Transaction.openOuter()) {
                            containerEntity.xpStorage.extract(FluidVariant.of(FluidRegister.get(FluidRegistry.XP)),
                                    FluidConstants.BOTTLE / 100, transaction);
                            progress++;
                            serverWorld.getChunkManager().markForUpdate(pos);
                            transaction.commit();
                        }
                    }
                    if (progress == maxProgress) {
                        progress = 0;
                        crafting = false;

                        if (!isBlack && world.getBlockEntity(linkPos) instanceof LuminInjectorEntity injectorEntity) {
                            injectorEntity.itemStacks.add(itemStack);
                        }
                        if (isBlack && world.getBlockEntity(linkPos) instanceof DimInfuserEntity infuserEntity) {
                            infuserEntity.itemStacks.add(itemStack);
                        }

                        itemStack = ItemStack.EMPTY;
                    }
                }
            }
        }
    }
}
