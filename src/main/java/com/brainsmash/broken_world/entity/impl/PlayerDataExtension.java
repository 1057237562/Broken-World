package com.brainsmash.broken_world.entity.impl;

import com.brainsmash.broken_world.entity.SpellEntity;

public interface PlayerDataExtension {

    void forceSetFlag(int index, boolean flag);

    void addPitchSpeed(float speed);

    void addYawSpeed(float speed);

    void setSpellEntity(SpellEntity spellEntity);

    SpellEntity getSpellEntity();
}
