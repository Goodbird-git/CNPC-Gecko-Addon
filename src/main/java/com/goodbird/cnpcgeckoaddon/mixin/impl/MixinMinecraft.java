package com.goodbird.cnpcgeckoaddon.mixin.impl;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 999)
public class MixinMinecraft {
    @Inject(method = "<init>", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;createSearchTrees()V"))
    private void lateInit(CallbackInfo ci){

    }
}
