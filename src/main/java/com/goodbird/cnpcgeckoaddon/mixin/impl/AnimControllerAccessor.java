package com.goodbird.cnpcgeckoaddon.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.core.animation.AnimationController;

@Mixin(value = AnimationController.class, remap = false)
public interface AnimControllerAccessor {

    @Accessor
    void setAnimationState(AnimationController.State state);

    @Accessor
    boolean getJustStartedTransition();

    @Accessor
    AnimationController.State getAnimationState();
}
