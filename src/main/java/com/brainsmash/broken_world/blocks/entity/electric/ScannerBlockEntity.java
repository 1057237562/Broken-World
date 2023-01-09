package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ScannerBlockEntity extends ConsumerBlockEntity  {

    public BlockPos pointer = new BlockPos(16,-1,16);
    private final int speed = 3;
    private final int maxScanned = 24;
    public DefaultedList<BlockPos> scanned = DefaultedList.of();
    public ScannerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.SCANNER_ENTITY_TYPE, pos, state);
        setMaxCapacity(3000);
        maxProgression = 0;
        powerConsumption = 20;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(!world.isClient){
            for(int i = 0; i < scanned.size();i++) {
                BlockPos pos1 = scanned.get(i);
                boolean flag = false;
                for (RegistryEntry<Block> blockRegistryEntry : Registry.BLOCK.iterateEntries(ConventionalBlockTags.ORES)) {
                    if (world.getBlockState(pos.add(pos1.getX(), pos1.getY(), pos1.getZ())).getBlock().equals(blockRegistryEntry.value())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    scanned.remove(i);
                    i--;
                    world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                    markDirty();
                }
            }
            if(canRun()){
                running = true;

                for(int i = 0; i < speed;i++)
                    if(!world.isOutOfHeightLimit(pos.getY() + pointer.getY())) {
                        for(RegistryEntry<Block> blockRegistryEntry : Registry.BLOCK.iterateEntries(ConventionalBlockTags.ORES)){
                            if(world.getBlockState(pos.add(pointer.getX(),pointer.getY(),pointer.getZ())).getBlock().equals(blockRegistryEntry.value())){
                                if(scanned.size() >= maxScanned) {
                                    running = false;
                                    super.tick(world, pos, state, blockEntity);
                                }
                                scanned.add(pointer);
                                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
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
        }
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putLong("pointer",pointer.asLong());
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
        pointer = BlockPos.fromLong(nbt.getLong("pointer"));
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
