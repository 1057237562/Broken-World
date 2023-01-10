package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.enums.OreTypeRegistry;
import com.brainsmash.broken_world.screenhandlers.descriptions.MinerGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MinerBlockEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(25, ItemStack.EMPTY);
    public BlockPos pointer = new BlockPos(16,-1,16);
    private final int speed = 3;

    public MinerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.MINER_ENTITY_TYPE,pos, state);
        setMaxCapacity(4000);
        maxProgression = 0;
        powerConsumption = 50;
    }

    public boolean insertItem(ItemStack stack){
        for(int i = 0;i<inventory.size()-1;i++){
            if(inventory.get(i).isEmpty()){
                inventory.set(i,stack);
                return true;
            }
            if(inventory.get(i).getItem().equals(stack.getItem())){
                int insertCount = Math.min(inventory.get(i).getMaxCount() - inventory.get(i).getCount(),stack.getCount());
                inventory.get(i).increment(insertCount);
                stack.decrement(insertCount);
            }
            if(stack.getCount() == 0)
                return true;
        }
        if(stack.getCount() == 0)
            return true;
        return false;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(!world.isClient){
            if(canRun()){
                running = true;

                for(int i = 0; i < speed;i++)
                    if(!world.isOutOfHeightLimit(pos.getY() + pointer.getY())) {
                        BlockPos pointPos = pos.add(pointer.getX(),pointer.getY(),pointer.getZ());
                        for(RegistryEntry<Block> blockRegistryEntry : Registry.BLOCK.iterateEntries(ConventionalBlockTags.ORES)){
                            if(world.getBlockState(pointPos).getBlock().equals(blockRegistryEntry.value())){
                                if(!insertItem(new ItemStack(world.getBlockState(pointPos).getBlock().asItem(),1))){
                                    EntityHelper.spawnItem(world,new ItemStack(world.getBlockState(pointPos).getBlock().asItem(),1),1, Direction.UP,pos);
                                }
                                world.setBlockState(pointPos,new BlockState(Blocks.AIR,null,null));
                                markDirty();
                            }
                        }
                        if (pointer.getX() > -16){
                            pointer = pointer.add(-1,0,0);
                        }else if(pointer.getZ() > -16){
                            pointer = pointer.add(32,0,-1);
                        }else{
                            pointer = pointer.add(32,-1,32);
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
        Inventories.writeNbt(nbt,inventory);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt,inventory);
        pointer = BlockPos.fromLong(nbt.getLong("pointer"));
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
