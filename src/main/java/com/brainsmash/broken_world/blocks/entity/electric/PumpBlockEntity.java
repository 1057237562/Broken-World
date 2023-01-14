package com.brainsmash.broken_world.blocks.entity.electric;

import alexiil.mc.lib.attributes.CombinableAttribute;
import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidInsertable;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.fluid.world.FluidWorldUtil;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.MinerGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class PumpBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private FluidVolume stored = FluidVolumeUtil.EMPTY;
    public BlockPos pointer = new BlockPos(3,-1,3);

    public PumpBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.PUMP_ENTITY_TYPE,pos, state);
        setMaxCapacity(500);
        maxProgression = 0;
        powerConsumption = 4;
    }

    @Nonnull
    public <T> T getNeighbourAttribute(CombinableAttribute<T> attr, Direction dir) {
        return attr.get(getWorld(), getPos().offset(dir), SearchOptions.inDirection(dir));
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(!world.isClient){
            if(canRun() && pointer.getY() <= 5) {
                running = true;
                if (!world.isOutOfHeightLimit(pos.getY() + pointer.getY())) {
                    BlockPos pointPos = pos.add(pointer.getX(), pointer.getY(), pointer.getZ());
                    if (!world.isChunkLoaded(pointPos)) {
                        return;
                    }
                    for (Direction direction : Direction.values()) {
                        if (!stored.isEmpty() && direction != Direction.DOWN) {
                            FluidInsertable insertable = getNeighbourAttribute(FluidAttributes.INSERTABLE, direction);
                            stored = insertable.attemptInsertion(stored, Simulation.ACTION);
                            if (stored.isEmpty()) {
                                break;
                            }
                            markDirty();
                        }
                    }
                    if (stored.isEmpty()) {
                        FluidVolume drained = FluidWorldUtil.drain(getWorld(), pointPos, Simulation.ACTION);
                        if (!drained.isEmpty()) {
                            stored = drained;
                            markDirty();
                        }

                        if (pointer.getX() > -3) {
                            pointer = pointer.add(-1, 0, 0);
                        } else if (pointer.getZ() > -3) {
                            pointer = pointer.add(6, 0, -1);
                        } else {
                            pointer = pointer.add(6, -1, 6);
                        }
                    }
                }
            }else{
                running = false;
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putLong("pointer",pointer.asLong());
        nbt.put("fluid", stored.toTag());
        Inventories.writeNbt(nbt,inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        pointer = BlockPos.fromLong(nbt.getLong("pointer"));
        stored = FluidVolume.fromTag(nbt.getCompound("fluid"));
        Inventories.readNbt(nbt,inventory);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MinerGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
}
