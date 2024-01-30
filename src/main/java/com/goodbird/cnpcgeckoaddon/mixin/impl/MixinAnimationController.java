package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.mixin.IAnimationController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;

@Mixin(AnimationController.class)
public class MixinAnimationController implements IAnimationController {
    @Shadow(remap = false)
    protected AnimationBuilder currentAnimationBuilder;

    @Unique
    public AnimationBuilder getCurrentAnimationBuilder(){
        return currentAnimationBuilder;
    }
}
