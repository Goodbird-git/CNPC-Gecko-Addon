package com.goodbird.cnpcgeckoaddon.mixin.impl;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.ModelData;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataDisplay.class)
public class ClientMixinDataDisplay {
    @Shadow(remap = false)
    EntityNPCInterface npc;


    @Inject(method = "readToNBT", at = @At("TAIL"), remap = false)
    public void readFromNBTEnd(CompoundTag nbttagcompound, CallbackInfo ci){
        if(npc instanceof EntityCustomNpc customNpc) {
            if(ModelData.get(customNpc).getEntity(npc)!=null)
                EntityUtil.Copy(npc, ModelData.get(customNpc).getEntity(npc));
        }
    }
}
