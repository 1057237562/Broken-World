package com.brainsmash.broken_world.entity.impl;

import java.util.List;

public interface LivingEntityDataExtension {

    void setSpellSequence(List<Integer> spellSequence);

    boolean canApplySpell();
}
