package com.goodbird.cnpcgeckoaddon.mixin.impl;

import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttributeModifierManager.class)
public interface IAttributeModifierManager {

    @Accessor
    void setSupplier(AttributeModifierMap supplier);

    @Accessor
    AttributeModifierMap getSupplier();
}
