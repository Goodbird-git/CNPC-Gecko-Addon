package com.goodbird.cnpcgeckoaddon.mixin.impl;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {

    @Invoker
    void invokeLoadAdditional(CompoundTag tag, HolderLookup.Provider registries);
}
