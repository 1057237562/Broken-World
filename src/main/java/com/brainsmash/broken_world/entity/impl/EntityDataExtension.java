package com.brainsmash.broken_world.entity.impl;

import net.minecraft.nbt.NbtElement;

public interface EntityDataExtension {

    NbtElement getData();

    void setData(NbtElement ele);
}
