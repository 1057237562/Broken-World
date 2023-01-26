package com.brainsmash.broken_world.blocks.entity.electric;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.net.*;
import alexiil.mc.lib.net.impl.CoreMinecraftNetUtil;
import alexiil.mc.lib.net.impl.McNetworkStack;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.registry.BlockRegister;
import com.brainsmash.broken_world.screenhandlers.descriptions.CentrifugeGuiDescription;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class CentrifugeBlockEntity extends ConsumerBlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private static final FluidAmount SINGLE_TANK_CAPACITY = FluidAmount.BUCKET.mul(8);

    public final SimpleFixedFluidInv fluidInv = new SimpleFixedFluidInv(1, SINGLE_TANK_CAPACITY);

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(20, ItemStack.EMPTY);

    public static final ParentNetIdSingle<CentrifugeBlockEntity> NET_PARENT;
    public static final NetIdDataK<CentrifugeBlockEntity> CHANGED_LIQUID;
    private Set<ActiveConnection> activeConnections = new HashSet<>();

    static {
        NET_PARENT = McNetworkStack.BLOCK_ENTITY.subType(CentrifugeBlockEntity.class, "broken_world:centrifuge");
        CHANGED_LIQUID = NET_PARENT.idData("CHANGE_BRIGHTNESS").setReceiver(CentrifugeBlockEntity::receiveLiquidChange);
    }

    protected final void sendLiquidChange() {
        for (ActiveConnection connection : activeConnections) {
            CHANGED_LIQUID.send(connection, this, (be, buf, ctx) -> {
                ctx.assertServerSide();

                buf.writeNbt(fluidInv.toTag());
            });
        }
    }

    private void receiveLiquidChange(NetByteBuf buf, IMsgReadCtx ctx) throws InvalidInputDataException {
        ctx.assertClientSide();

        fluidInv.fromTag(buf.readNbt());
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
        maxProgression = 100;
        setMaxCapacity(1000);
        powerConsumption = 5;
    }

    public boolean checkRecipe() {


        return false;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if (!world.isClient) {
            sendLiquidChange();
            if (canRun()) {

            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
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

}
