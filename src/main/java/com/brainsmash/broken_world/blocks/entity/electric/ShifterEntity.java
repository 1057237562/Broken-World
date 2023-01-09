package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.registry.ShifterRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.CrusherGuiDescription;
import com.brainsmash.broken_world.screenhandlers.descriptions.ShifterGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class ShifterEntity extends ConsumerBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(23, ItemStack.EMPTY);
    public final Random random = new Random();

    public ShifterEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.SHIFTER_ENTITY_TYPE,pos, state);
        setMaxCapacity(500);
        maxProgression = 75;
        powerConsumption = 4;
    }
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        //return new TeleporterControllerScreenHandler(syncId, playerInventory, this);
        return new ShifterGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world,pos));
    }

    public boolean insertItem(ItemStack stack){
        for(int i = 0;i<inventory.size()-2;i++){
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
            if(ShifterRegister.recipes.containsKey(inventory.get(21).getItem()) && canRun()){
                running = true;
                if(progression < maxProgression){
                    progression++;
                }else{
                    DefaultedList<Pair<Float, Item>> output = ShifterRegister.recipes.get(inventory.get(21).getItem());
                    for(Pair<Float,Item> pair : output){
                        if(random.nextDouble() < pair.getLeft()){
                            if(!insertItem(new ItemStack(pair.getRight(),1))){
                                EntityHelper.spawnItem(world,new ItemStack(pair.getRight(),1),1, Direction.UP,pos);
                            }
                        }
                    }
                    inventory.get(21).decrement(1);
                    progression = 0;
                }
            }else{
                running = false;
            }
        }
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

}
