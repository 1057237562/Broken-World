package com.brainsmash.broken_world.blocks.multiblock;

import com.brainsmash.broken_world.blocks.multiblock.util.MultiblockComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class MultiblockEntity extends DummyBlockEntity implements BlockEntityTicker<MultiblockEntity> {

    protected Vec3i multiblockSize;
    protected BlockPos anchor;
    protected Identifier type;
    private MultiblockComponent component;

    public MultiblockEntity(BlockPos pos, BlockState state) {
        super(MultiblockUtil.MULTIBLOCK_ENTITY_TYPE, pos, state);
    }

    public Vec3i getMultiblockSize() {
        return multiblockSize;
    }

    public void setMultiblockSize(Vec3i multiblockSize) {
        this.multiblockSize = multiblockSize;
    }

    public BlockPos getAnchor() {
        return anchor;
    }

    public void setAnchor(BlockPos anchor) {
        this.anchor = anchor;
    }

    public void setType(Identifier type) {
        this.type = type;
        component = MultiblockUtil.providerMap.get(type).get(world, pos);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putLong("multiblockSize", new BlockPos(multiblockSize).asLong());
        nbt.putString("type", type.toString());
        NbtCompound compound = new NbtCompound();
        component.writeNbt(compound);
        nbt.put("component", compound);
        nbt.putLong("anchor", anchor.asLong());
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        multiblockSize = BlockPos.fromLong(nbt.getLong("multiblockSize"));
        setType(Identifier.tryParse(nbt.getString("type")));
        component.readNbt(nbt.getCompound("component"));
        anchor = BlockPos.fromLong(nbt.getLong("anchor"));
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MultiblockEntity blockEntity) {
        if (component != null) component.tick(world, pos, imitateBlock);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos link, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (component != null) return component.onUse(world, pos, imitateBlock, player, hand, hit);
        return ActionResult.PASS;
    }
}
