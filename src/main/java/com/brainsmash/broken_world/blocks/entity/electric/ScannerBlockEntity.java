package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ScannerBlockEntity extends ConsumerBlockEntity {

    public DefaultedList<BlockPos> scanned = DefaultedList.of();
    public ScannerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.SCANNER_ENTITY_TYPE, pos, state);
        scanned.add(new BlockPos(0,-2,0));

    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        NbtList nbtList = new NbtList();
        for(BlockPos pos : scanned){
            NbtCompound compound = new NbtCompound();
            compound.putLong("pos",pos.asLong());
            nbtList.add(compound);
        }
        if(!nbtList.isEmpty())
            nbt.put("scanned",nbtList);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        NbtList nbtList = nbt.getList("scanned", NbtElement.COMPOUND_TYPE);
        for(int i = 0;i<nbtList.size();i++){
            NbtCompound compound = nbtList.getCompound(i);
            scanned.add(BlockPos.fromLong(compound.getLong("pos")));
        }
    }
}
