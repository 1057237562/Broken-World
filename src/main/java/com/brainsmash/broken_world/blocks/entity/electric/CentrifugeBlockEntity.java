package com.brainsmash.broken_world.blocks.entity.electric;

import alexiil.mc.lib.net.*;
import alexiil.mc.lib.net.impl.CoreMinecraftNetUtil;
import alexiil.mc.lib.net.impl.McNetworkStack;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.fluid.storage.SingleFluidStorage;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.recipe.CentrifugeRecipe;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.CentrifugeGuiDescription;
import com.brainsmash.broken_world.util.EntityHelper;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CentrifugeBlockEntity extends ConsumerBlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private SingleFluidStorage<FluidVariant> createTank(boolean input) {
        return new SingleFluidStorage<>() {
            @Override
            protected FluidVariant getBlankVariant() {
                return FluidVariant.blank();
            }

            @Override
            protected long getCapacity(FluidVariant variant) {
                return 8 * FluidConstants.BUCKET;
            }

            @Override
            protected boolean canInsert(FluidVariant variant) {
                // Only allow the specified variant.
                return input;
            }

            @Override
            protected boolean canExtract(FluidVariant variant) {
                return !input;
            }

            @Override
            protected void onFinalCommit() {
                markDirty();
            }
        };
    }

    public final SingleFluidStorage<FluidVariant> inputInv = createTank(true);
    public final SingleFluidStorage<FluidVariant> outputInv = createTank(false);

    public Storage<FluidVariant> fluidStorage = new CombinedStorage<>(List.of(inputInv, outputInv));

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(14, ItemStack.EMPTY);

    public static final ParentNetIdSingle<CentrifugeBlockEntity> NET_PARENT;
    public static final NetIdDataK<CentrifugeBlockEntity> CHANGED_LIQUID;
    private Set<ActiveConnection> activeConnections = new HashSet<>();

    static {
        NET_PARENT = McNetworkStack.BLOCK_ENTITY.subType(CentrifugeBlockEntity.class, "broken_world:centrifuge");
        CHANGED_LIQUID = NET_PARENT.idData("CHANGE_BRIGHTNESS").setReceiver(CentrifugeBlockEntity::receiveLiquidChange);
    }

    private final Random random = new Random();
    private Item lastItem;
    public float tick = 0;

    protected final void sendLiquidChange() {
        for (ActiveConnection connection : activeConnections) {
            CHANGED_LIQUID.send(connection, this, (be, buf, ctx) -> {
                ctx.assertServerSide();
                NbtCompound nbtCompound = new NbtCompound();
                inputInv.writeNbt(nbtCompound);
                buf.writeNbt(nbtCompound);
                nbtCompound = new NbtCompound();
                outputInv.writeNbt(nbtCompound);
                buf.writeNbt(nbtCompound);
            });
        }
    }

    private void receiveLiquidChange(NetByteBuf buf, IMsgReadCtx ctx) throws InvalidInputDataException {
        ctx.assertClientSide();

        inputInv.readNbt(buf.readNbt());
        outputInv.readNbt(buf.readNbt());
    }

    @Override
    public void onOpen(PlayerEntity player) {
        ImplementedInventory.super.onOpen(player);
        activeConnections.add(CoreMinecraftNetUtil.getConnection(player));
    }

    @Override
    public void onClose(PlayerEntity player) {
        ImplementedInventory.super.onClose(player);
        activeConnections.remove(CoreMinecraftNetUtil.getConnection(player));
    }

    public CentrifugeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.CENTRIFUGE_ENTITY_TYPE, pos, state);
        maxProgression = 300;
        setMaxCapacity(1000);
        powerConsumption = 5;
    }

    public boolean insertItem(ItemStack stack) {
        for (int i = 2; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, stack);
                return true;
            }
            if (inventory.get(i).getItem().equals(stack.getItem())) {
                int insertCount = Math.min(inventory.get(i).getMaxCount() - inventory.get(i).getCount(),
                        stack.getCount());
                inventory.get(i).increment(insertCount);
                stack.decrement(insertCount);
            }
            if (stack.getCount() == 0) return true;
        }
        if (stack.getCount() == 0) return true;
        return false;
    }


    public boolean checkRecipe() {
        if (!inventory.get(1).isEmpty()) {
            if (inputInv.amount >= FluidConstants.BUCKET) {
                Pair<Fluid, Item> key = new Pair<>(inputInv.variant.getFluid(), inventory.get(1).getItem());
                if (CentrifugeRecipe.recipes.containsKey(key)) {
                    return outputInv.amount <= outputInv.getCapacity() - FluidConstants.BOTTLE;
                }
            }
            Pair<Fluid, Item> key = new Pair<>(null, inventory.get(1).getItem());
            if (CentrifugeRecipe.recipes.containsKey(key)) {
                return outputInv.amount <= outputInv.getCapacity() - FluidConstants.BOTTLE;
            }
        }
        if (inputInv.amount >= FluidConstants.BUCKET) {
            Pair<Fluid, Item> key = new Pair<>(inputInv.variant.getFluid(), null);
            if (CentrifugeRecipe.recipes.containsKey(key)) {
                return outputInv.amount <= outputInv.getCapacity() - FluidConstants.BOTTLE;
            }
        }

        return false;
    }

    public void process() {
        if (!inventory.get(1).isEmpty()) {
            Pair<Fluid, Item> key = new Pair<>(inputInv.variant.getFluid(), inventory.get(1).getItem());
            if (CentrifugeRecipe.recipes.containsKey(key)) {
                Pair<List<Pair<Float, Item>>, Fluid> recipe = CentrifugeRecipe.recipes.get(key);

                for (Pair<Float, Item> pair : recipe.getFirst()) {
                    if (random.nextDouble() < pair.getFirst()) {
                        if (!insertItem(new ItemStack(pair.getSecond(), 1))) {
                            EntityHelper.spawnItem(world, new ItemStack(pair.getSecond(), 1), 1, Direction.UP, pos);
                        }
                    }
                }
                if (recipe.getSecond() != null) {
                    if (outputInv.isResourceBlank()) {
                        outputInv.variant = FluidVariant.of(recipe.getSecond());
                        outputInv.amount = FluidConstants.BOTTLE;
                    } else {
                        outputInv.amount += FluidConstants.BOTTLE;
                    }
                }
                inventory.get(1).decrement(1);
                inputInv.amount -= FluidConstants.BUCKET;
                return;
            }
            key = new Pair<>(null, inventory.get(1).getItem());
            if (CentrifugeRecipe.recipes.containsKey(key)) {
                Pair<List<Pair<Float, Item>>, Fluid> recipe = CentrifugeRecipe.recipes.get(key);

                for (Pair<Float, Item> pair : recipe.getFirst()) {
                    if (random.nextDouble() < pair.getFirst()) {
                        if (!insertItem(new ItemStack(pair.getSecond(), 1))) {
                            EntityHelper.spawnItem(world, new ItemStack(pair.getSecond(), 1), 1, Direction.UP, pos);
                        }
                    }
                }
                if (recipe.getSecond() != null) {
                    if (outputInv.isResourceBlank()) {
                        outputInv.variant = FluidVariant.of(recipe.getSecond());
                        outputInv.amount = FluidConstants.BOTTLE;
                    } else {
                        outputInv.amount += FluidConstants.BOTTLE;
                    }
                }
                inventory.get(1).decrement(1);
                return;
            }
        }
        Pair<Fluid, Item> key = new Pair<>(inputInv.variant.getFluid(), null);
        if (CentrifugeRecipe.recipes.containsKey(key)) {
            Pair<List<Pair<Float, Item>>, Fluid> recipe = CentrifugeRecipe.recipes.get(key);

            for (Pair<Float, Item> pair : recipe.getFirst()) {
                if (random.nextDouble() < pair.getFirst()) {
                    if (!insertItem(new ItemStack(pair.getSecond(), 1))) {
                        EntityHelper.spawnItem(world, new ItemStack(pair.getSecond(), 1), 1, Direction.UP, pos);
                    }
                }
            }

            if (recipe.getSecond() != null) {
                if (outputInv.isResourceBlank()) {
                    outputInv.variant = FluidVariant.of(recipe.getSecond());
                    outputInv.amount = FluidConstants.BOTTLE;
                } else {
                    outputInv.amount += FluidConstants.BOTTLE;
                }
            }
            inputInv.amount -= FluidConstants.BUCKET;
        }
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            if (!outputInv.isResourceBlank()) {
                for (Direction direction : Direction.values()) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        Storage<FluidVariant> fluidInv = FluidStorage.SIDED.find(world, pos, direction);
                        outputInv.amount -= fluidInv.insert(outputInv.variant, outputInv.amount, transaction);
                        transaction.commit();
                    }
                    if (outputInv.isResourceBlank() || outputInv.amount == 0) {
                        break;
                    }
                    markDirty();
                }
            }
            sendLiquidChange();
            if (checkEnergy() && checkRecipe()) {
                running = true;
                if (progression < maxProgression) {
                    progression++;
                } else {
                    process();
                    progression = 0;
                }
                if (!inventory.get(0).getItem().equals(lastItem)) {
                    lastItem = inventory.get(0).getItem();
                    progression = 0;
                }
            } else {
                running = false;
                progression = 0;
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            chargeUseItem(inventory.get(1));
        }
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CentrifugeGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        nbt.putBoolean("running", running);
        NbtCompound nbtCompound = new NbtCompound();
        inputInv.writeNbt(nbtCompound);
        nbt.put("inputInv", nbtCompound);
        nbtCompound = new NbtCompound();
        outputInv.writeNbt(nbtCompound);
        nbt.put("outputInv", nbtCompound);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        running = nbt.getBoolean("running");
        inputInv.readNbt(nbt.getCompound("inputInv"));
        outputInv.readNbt(nbt.getCompound("outputInv"));
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

}
