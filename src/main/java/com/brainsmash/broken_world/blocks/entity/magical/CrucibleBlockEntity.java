package com.brainsmash.broken_world.blocks.entity.magical;

import com.brainsmash.broken_world.blocks.fluid.PotionFluid;
import com.brainsmash.broken_world.blocks.fluid.storage.SingleFluidStorage;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class CrucibleBlockEntity extends BlockEntity implements BlockEntityTicker<CrucibleBlockEntity> {

    public final SingleFluidStorage<FluidVariant> fluidStorage = new SingleFluidStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET;
        }
    };

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CRUCIBLE_ENTITY_TYPE, pos, state);
    }


    public ItemStack getFirstItem() {
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.get(i).isEmpty()) {
                return inventory.get(i);
            }
        }
        return ItemStack.EMPTY;
    }

    public void removeFirstItem() {
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.get(i).isEmpty()) {
                inventory.set(i, ItemStack.EMPTY);
                return;
            }
        }
    }

    public void decrementFirstItem(int amount) {
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.get(i).isEmpty()) {
                inventory.get(i).decrement(amount);
                return;
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        fluidStorage.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        fluidStorage.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        if (world instanceof ClientWorld clientWorld) {
            clientWorld.scheduleBlockRenders(ChunkSectionPos.getSectionCoord(pos.getX()),
                    ChunkSectionPos.getSectionCoord(pos.getY()), ChunkSectionPos.getSectionCoord(pos.getZ()));
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound compound = new NbtCompound();
        writeNbt(compound);
        return compound;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public int getFluidColor() {
        if (fluidStorage.isEmpty()) return 0x000000;
        if (fluidStorage.variant.getFluid() != null && fluidStorage.variant.getFluid().matchesType(Fluids.WATER))
            return 0x7442FF;
        return ((PotionFluid) fluidStorage.variant.getFluid()).getRenderColor();
    }


    public ItemStack insertItem(ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getItem().equals(stack.getItem())) {
                int insertCount = Math.min(inventory.get(i).getMaxCount() - inventory.get(i).getCount(),
                        stack.getCount());
                inventory.get(i).increment(insertCount);
                stack.decrement(insertCount);
            }
            if (stack.getCount() == 0) return ItemStack.EMPTY;
        }
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, stack);
                return ItemStack.EMPTY;
            }
        }
        if (stack.getCount() == 0) return ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CrucibleBlockEntity blockEntity) {
        if (world instanceof ServerWorld serverWorld) {
            Potion potion = Potions.WATER;
            if (fluidStorage.variant.getFluid() instanceof PotionFluid potionFluid) {
                potion = potionFluid.potion;
            }
            ItemStack output = new ItemStack(Items.POTION);
            output = PotionUtil.setPotion(output, potion);
            ItemStack firstItem = getFirstItem();
            if (BrewingRecipeRegistry.hasRecipe(output, firstItem)) {
                output = BrewingRecipeRegistry.craft(firstItem, output);
                decrementFirstItem(1);
                Potion result = PotionUtil.getPotion(output);
                fluidStorage.variant = FluidVariant.of(PotionFluid.get(result));
                markDirty();
                serverWorld.getChunkManager().markForUpdate(pos);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
            }
        }
    }
}
