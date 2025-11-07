package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.mixin.IDialog;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.controllers.data.Dialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Dialog.class)
public class MixinDialog implements IDialog {

    @Unique
    private String animationName = "";

    @Override
    @Unique
    public String getAnimation() {
        return animationName;
    }

    @Override
    @Unique
    public void setAnimation(String animation) {
        this.animationName = animation;
    }

    @Override
    @Unique
    public boolean hasAnimation() {
        return !animationName.isEmpty();
    }

    @Inject(method = "writeToNBTPartial", at = @At("TAIL"))
    public void writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir){
        compound.putString("dialogAnimation", animationName);
    }

    @Inject(method = "readNBTPartial", at = @At("TAIL"))
    public void readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag compound, CallbackInfo ci){
        if(compound.contains("dialogAnimation")){
            animationName = compound.getString("dialogAnimation");
        }else{
            animationName="";
        }
    }
}
