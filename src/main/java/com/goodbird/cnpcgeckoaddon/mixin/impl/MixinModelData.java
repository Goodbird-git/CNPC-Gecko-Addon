package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.ModelData;
import noppes.npcs.ModelDataShared;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelData.class)
public abstract class MixinModelData extends ModelDataShared {

    @Inject(method = "getEntity", at = @At(value = "FIELD", target = "Lnoppes/npcs/ModelData;entity:Lnet/minecraft/world/entity/LivingEntity;"), remap = false)
    public void onEntityFieldAssigned(EntityNPCInterface npc, CallbackInfoReturnable<LivingEntity> cir){
        if(!(entity instanceof EntityCustomModel modelEntity)) return;
        modelEntity.modelResLoc = new ResourceLocation(((IDataDisplay)npc.display).getCustomModelData().getModel());
        modelEntity.animResLoc = new ResourceLocation(((IDataDisplay)npc.display).getCustomModelData().getAnimFile());
        modelEntity.idleAnim = ((IDataDisplay)npc.display).getCustomModelData().getIdleAnim();
    }
}
