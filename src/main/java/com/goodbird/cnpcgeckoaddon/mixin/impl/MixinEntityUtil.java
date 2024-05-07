package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import com.goodbird.cnpcgeckoaddon.utils.NpcTextureUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

@Mixin(EntityUtil.class)
public class MixinEntityUtil {

    @Inject(method = "Copy", at = @At("TAIL"), remap = false)
    private static void copy(LivingEntity copied, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof EntityCustomModel && copied instanceof EntityNPCInterface) {
            EntityCustomModel modelEntity = (EntityCustomModel) entity;
            EntityNPCInterface npc = (EntityNPCInterface) copied;
            IDataDisplay display = (IDataDisplay) npc.display;
            modelEntity.textureResLoc = NpcTextureUtils.getNpcTexture((EntityNPCInterface) copied);
            modelEntity.modelResLoc = new ResourceLocation(display.getCustomModelData().getModel());
            modelEntity.animResLoc = new ResourceLocation(display.getCustomModelData().getAnimFile());
            modelEntity.idleAnim = display.getCustomModelData().getIdleAnim();
            modelEntity.walkAnim = display.getCustomModelData().getWalkAnim();
            modelEntity.attackAnim = display.getCustomModelData().getAttackAnim();
            modelEntity.hurtAnim = display.getCustomModelData().getHurtAnim();
            if(display.getCustomModelData().isHurtTintEnabled()){
                modelEntity.hurtTime = npc.hurtTime;
                modelEntity.deathTime = npc.deathTime;
            }
            if(npc.inventory.getLeftHand()!=null) {
                modelEntity.leftHeldItem = npc.inventory.getLeftHand().getMCItemStack();
            }
            modelEntity.headBoneName = display.getCustomModelData().getHeadBoneName();
            AnimationData animationData = modelEntity.getFactory().getOrCreateAnimationData(modelEntity.getUUID().hashCode());
            for(AnimationController controller : animationData.getAnimationControllers().values()){
                controller.transitionLengthTicks = display.getCustomModelData().getTransitionLengthTicks();
            }
            if(display.getCustomModelData().getHeight()!=modelEntity.getBbHeight() || display.getCustomModelData().getWidth() != modelEntity.getBbWidth()){
                modelEntity.setSize(display.getCustomModelData().getWidth(), display.getCustomModelData().getHeight());
                npc.refreshDimensions();
            }
        }
    }
}
