package com.goodbird.cnpcgeckoaddon.mixin.impl;

import noppes.npcs.api.wrapper.WrapperNpcAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

@Mixin(WrapperNpcAPI.class)
public class MixinWrapperNpcApi {
    @Unique
    public AnimationBuilder createAnimBuilder(){
        return new AnimationBuilder();
    }
}
