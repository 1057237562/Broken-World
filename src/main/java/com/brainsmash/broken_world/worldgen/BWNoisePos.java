package com.brainsmash.broken_world.worldgen;

import net.minecraft.world.gen.densityfunction.DensityFunction.NoisePos;

public class BWNoisePos implements NoisePos {
    public int blockX;
    public int blockY;
    public int blockZ;

    public BWNoisePos(NoisePos pos){
        blockX = pos.blockX();
        blockY = pos.blockY();
        blockZ = pos.blockZ();
    }

    public BWNoisePos(int x, int y, int z){
        blockX = x;
        blockY = y;
        blockZ = z;
    }

    public BWNoisePos offsetX(int offset){
        BWNoisePos pos = new BWNoisePos(this);
        pos.blockX += offset;
        return pos;
    }

    public BWNoisePos offsetZ(int offset){
        BWNoisePos pos = new BWNoisePos(this);
        pos.blockZ += offset;
        return pos;
    }

    @Override
    public int blockX() {
        return blockX;
    }

    @Override
    public int blockY() {
        return blockY;
    }

    @Override
    public int blockZ() {
        return blockZ;
    }

    @Override
    public String toString() {
        return "BWNoisePos " + "x:" + blockX + " y:" + blockY + " z:" + blockZ;
    }
}
