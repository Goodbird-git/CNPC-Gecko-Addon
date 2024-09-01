package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderNPCInterface.class)
public abstract class MixinRenderNPCInterface <T extends EntityNPCInterface, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {

    @Shadow(remap = false)
    public abstract void renderNameTag(T npc, Component text, PoseStack matrixStack, MultiBufferSource buffer, int light);

    public MixinRenderNPCInterface() {
        super(null,null,0);
    }

    @Inject(method = "render(Lnoppes/npcs/entity/EntityNPCInterface;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), cancellable = true)
    public void render(T npc, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if(npc instanceof EntityCustomNpc && ((EntityCustomNpc)npc).modelData.getEntity(npc) instanceof EntityCustomModel){
            cnpcgeckoaddon$renderGeoModel((EntityCustomNpc) npc,matrixStack,buffer,packedLight);
            cnpcgeckoaddon$drawNameStandalone(npc, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            ci.cancel();
        }
    }

    @Unique
    public void cnpcgeckoaddon$drawNameStandalone(T p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_){
        var renderNameTagEvent = new net.minecraftforge.client.event.RenderNameTagEvent(p_114485_, p_114485_.getDisplayName(), this, p_114488_, p_114489_, p_114490_, p_114487_);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
        if (renderNameTagEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameTagEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(p_114485_))) {
            this.renderNameTag(p_114485_, renderNameTagEvent.getContent(), p_114488_, p_114489_, p_114490_);
        }
    }


    @Unique
    private void cnpcgeckoaddon$renderGeoModel(EntityCustomNpc npc, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        Entity entity = npc.modelData.getEntity(npc);
        //entity.setYRot(entity.yRotO = 0);
        if (!npc.isInvisible())
        {
            EntityRenderDispatcher lvt_16_1_ = Minecraft.getInstance().getEntityRenderDispatcher();
            lvt_16_1_.setRenderShadow(false);
            RenderSystem.runAsFancy(() -> {
                lvt_16_1_.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack, buffer,packedLight);
            });
        }
        else if (!npc.isInvisibleTo(Minecraft.getInstance().player))
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            EntityRenderDispatcher lvt_16_1_ = Minecraft.getInstance().getEntityRenderDispatcher();
            lvt_16_1_.setRenderShadow(false);
            RenderSystem.runAsFancy(() -> {
                lvt_16_1_.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack, buffer,packedLight);
            });
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
        }
    }
}
