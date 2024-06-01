package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
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

    @Shadow(remap = false)
    public ModelData modelData;

    public MixinEntityCustomNpc(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
    }

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
