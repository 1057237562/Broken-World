package com.brainsmash.broken_world.worldgen;

import net.minecraft.world.gen.densityfunction.DensityFunction.NoisePos;

public class Pos implements NoisePos {
    public int blockX;
    public int blockY;
    public int blockZ;

    public Pos(NoisePos pos){
        blockX = pos.blockX();
        blockY = pos.blockY();
        blockZ = pos.blockZ();
    }

    public Pos(int x, int y, int z){
        blockX = x;
        blockY = y;
        blockZ = x;
    }

    public Pos offsetX(int offset){
        Pos pos = new Pos(this);
        pos.blockX += offset;
        return pos;
    }

    public Pos offsetZ(int offset){
        Pos pos = new Pos(this);
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
}
