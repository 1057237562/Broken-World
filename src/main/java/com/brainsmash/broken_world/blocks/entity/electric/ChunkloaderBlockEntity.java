package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.ConsumerBlockEntity;
import com.brainsmash.broken_world.registry.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import static com.brainsmash.broken_world.blocks.electric.ChunkloaderBlock.radius;

public class ChunkloaderBlockEntity extends ConsumerBlockEntity {

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
                ChunkPos chunkPos = new ChunkPos(pos);
                for(int i = -radius;i<=radius;i++){
                    for(int j = -radius;j<=radius;j++){
                        ChunkPos currentChunkPos = new ChunkPos(chunkPos.x + i,chunkPos.z + j);
                        if(!serverWorld.getForcedChunks().contains(currentChunkPos.toLong())) {
                            serverWorld.setChunkForced(currentChunkPos.x,currentChunkPos.z,true);
                        }
                    }
                }

            }else{
                running = false;
                ServerWorld serverWorld = (ServerWorld) world;
                ChunkPos chunkPos = new ChunkPos(pos);
                for(int i = -radius;i<=radius;i++){
                    for(int j = -radius;j<=radius;j++){
                        ChunkPos currentChunkPos = new ChunkPos(chunkPos.x + i,chunkPos.z + j);
                        if(serverWorld.getForcedChunks().contains(currentChunkPos.toLong())) {
                            serverWorld.setChunkForced(currentChunkPos.x,currentChunkPos.z,true);
                        }
                    }
                }
            }
            state = state.with(Properties.LIT, isRunning());
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
        super.tick(world, pos, state, blockEntity);
    }
}
