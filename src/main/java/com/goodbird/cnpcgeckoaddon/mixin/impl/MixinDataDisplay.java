package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.data.CustomModelData;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.nbt.CompoundNBT;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataDisplay.class)
public class MixinDataDisplay implements IDataDisplay {

    @Shadow(remap = false)
    EntityNPCInterface npc;
    @Unique
    private final CustomModelData customNPC_Gecko_Addon$customModelData = new CustomModelData();

    @Inject(method = "save", at = @At("HEAD"), remap = false)
    public void writeToNBT(CompoundNBT nbttagcompound, CallbackInfoReturnable<CompoundNBT> cir) {
        if(hasCustomModel())
            customNPC_Gecko_Addon$customModelData.writeToNBT(nbttagcompound);
    }

    @Inject(method = "readToNBT", at = @At("HEAD"), remap = false)
    public void readFromNBT(CompoundNBT nbttagcompound, CallbackInfo ci){
        customNPC_Gecko_Addon$customModelData.readFromNBT(nbttagcompound);
    }

    @Unique
    public CustomModelData getCustomModelData(){
        return customNPC_Gecko_Addon$customModelData;
    }

    @Unique
    public boolean hasCustomModel() {
        return (npc instanceof EntityCustomNpc) && ((EntityCustomNpc) npc).modelData.getEntity(npc) != null &&
                ((EntityCustomNpc) npc).modelData.getEntity(npc) instanceof EntityCustomModel;
    }
}
