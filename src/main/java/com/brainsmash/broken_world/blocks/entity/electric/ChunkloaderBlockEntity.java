package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class ChunkloaderBlockEntity extends ConsumerBlockEntity {

    private int radius = 2;
    public ChunkloaderBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.LOADER_ENTITY_TYPE,pos, state);
        setMaxCapacity(2000);
        powerConsumption = 2;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        if(!world.isClient){
            if(canRun()) {
                running = true;
                ServerWorld serverWorld = (ServerWorld) world;
                ChunkPos currentChunkPos = new ChunkPos(pos);
                if(!serverWorld.getForcedChunks().contains(new ChunkPos(pos).toLong())) {
                    serverWorld.setChunkForced(currentChunkPos.x,currentChunkPos.z,true);
                }
            }else{
                running = false;
                ServerWorld serverWorld = (ServerWorld) world;
                ChunkPos currentChunkPos = new ChunkPos(pos);
                if(!serverWorld.getForcedChunks().contains(new ChunkPos(pos).toLong())) {
                    serverWorld.setChunkForced(currentChunkPos.x,currentChunkPos.z,false);
                }
            }
        }
        super.tick(world, pos, state, blockEntity);
    }
}
