package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityCustomNpc.class)
public class MixinEntityCustomNpc extends EntityNPCInterface {
    public MixinEntityCustomNpc(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
    }

    @Shadow(remap = false)
    public ModelData modelData;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        IDataDisplay display = (IDataDisplay) this.display;
        Entity entity = this.modelData.getEntity(this);
        if (!(entity instanceof EntityCustomModel)) return;
        EntityCustomModel modelEntity = (EntityCustomModel) entity;
        if (display.getCustomModelData().getHeight() != modelEntity.getBbHeight() || display.getCustomModelData().getWidth() != modelEntity.getBbWidth()) {
            modelEntity.setSize(display.getCustomModelData().getWidth(), display.getCustomModelData().getHeight());
            this.refreshDimensions();
        }
    }
}
