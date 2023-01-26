package com.brainsmash.broken_world.blocks.entity.electric;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.impl.SimpleFixedFluidInv;
import alexiil.mc.lib.net.*;
import alexiil.mc.lib.net.impl.CoreMinecraftNetUtil;
import alexiil.mc.lib.net.impl.McNetworkStack;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.blocks.impl.ImplementedInventory;
import com.brainsmash.broken_world.screenhandlers.descriptions.CentifugeGuiDescription;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class CentifugeBlockEntity extends ConsumerBlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private static final FluidAmount SINGLE_TANK_CAPACITY = FluidAmount.BUCKET.mul(8);

    public final SimpleFixedFluidInv fluidInv = new SimpleFixedFluidInv(1, SINGLE_TANK_CAPACITY);

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public static final ParentNetIdSingle<CentifugeBlockEntity> NET_PARENT;
    public static final NetIdDataK<CentifugeBlockEntity> CHANGED_LIQUID;

    static {
        NET_PARENT = McNetworkStack.BLOCK_ENTITY.subType(CentifugeBlockEntity.class, "broken_world:centifuge");
        CHANGED_LIQUID = NET_PARENT.idData("CHANGE_BRIGHTNESS").setReceiver(CentifugeBlockEntity::receiveLiquidChange);
    }

    protected final void sendLiquidChange() {
        for (ActiveConnection connection : CoreMinecraftNetUtil.getPlayersWatching(getWorld(), getPos())) {
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

    public CentifugeBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
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
        return new CentifugeGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world,pos));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

}
