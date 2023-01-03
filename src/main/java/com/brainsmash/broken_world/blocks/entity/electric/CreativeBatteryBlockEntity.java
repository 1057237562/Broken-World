package com.brainsmash.broken_world.blocks.entity.electric;

import com.brainsmash.broken_world.Main;
import com.brainsmash.broken_world.blocks.entity.electric.base.BatteryBlockEntity;
import com.brainsmash.broken_world.blocks.entity.electric.base.CableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;

public class CreativeBatteryBlockEntity extends BatteryBlockEntity {
    private Entity chargedCreeper;
    public CreativeBatteryBlockEntity(BlockPos pos, BlockState state) {
        super(Main.CREATIVE_BATTERY_ENTITY_TYPE, pos, state);
        setMaxCapacity(500000);
    }

    public Entity getCreeper(){
        if(chargedCreeper == null) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("id", "creeper");
            nbt.putBoolean("powered", true);
            chargedCreeper = EntityType.loadEntityWithPassengers(nbt, getWorld(), Function.identity());
        }
        return chargedCreeper;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);
        if(chargedCreeper != null)
            chargedCreeper.age++;
    }

}
