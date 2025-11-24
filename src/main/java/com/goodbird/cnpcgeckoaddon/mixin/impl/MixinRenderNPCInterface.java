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
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForge;
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
    public abstract void renderNameTag(T npc, Component text, PoseStack matrixStack, MultiBufferSource buffer, int light, float partialTicks);

    public MixinRenderNPCInterface() {
        super(null,null,0);
    }

    @Inject(method = "render(Lnoppes/npcs/entity/EntityNPCInterface;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), cancellable = true)
    public void render(T npc, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if(npc instanceof EntityCustomNpc && ((EntityCustomNpc)npc).modelData.getEntity(npc) instanceof EntityCustomModel){
            cnpcgeckoaddon$renderGeoModel((EntityCustomNpc) npc,matrixStack,buffer,packedLight, partialTicks);
            cnpcgeckoaddon$drawNameStandalone(npc, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            ci.cancel();
        }
    }

    @Unique
    public void cnpcgeckoaddon$drawNameStandalone(T p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_){
        RenderNameTagEvent renderNameplateEvent = new RenderNameTagEvent(p_225623_1_, p_225623_1_.getDisplayName(), this, p_225623_4_, p_225623_5_, p_225623_6_, p_225623_3_);
        NeoForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.canRender().isTrue() || renderNameplateEvent.canRender().isDefault() && this.shouldShowName(p_225623_1_)) {
            renderNameTag(p_225623_1_, renderNameplateEvent.getContent(), p_225623_4_, p_225623_5_, p_225623_6_, p_225623_2_);
            //this.renderNameTag(p_225623_1_, renderNameplateEvent.getContent(), p_225623_4_, p_225623_5_, p_225623_6_, p_225623_2_);
        }
    }


    @Unique
    private void cnpcgeckoaddon$renderGeoModel(EntityCustomNpc npc, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, float partialTicks)
    {
        Entity entity = npc.modelData.getEntity(npc);
        //entity.setYRot(entity.yRotO = 0);
        if (!npc.isInvisible())
        {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            EntityRenderDispatcher lvt_16_1_ = Minecraft.getInstance().getEntityRenderDispatcher();
            lvt_16_1_.setRenderShadow(false);
            RenderSystem.runAsFancy(() -> {
                lvt_16_1_.render(entity, 0.0, 0.0, 0.0, 0.0F, partialTicks, matrixStack, buffer,packedLight);
            });
        }
        else if (!npc.isInvisibleTo(Minecraft.getInstance().player))
        {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.15F);
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            EntityRenderDispatcher lvt_16_1_ = Minecraft.getInstance().getEntityRenderDispatcher();
            lvt_16_1_.setRenderShadow(false);
            RenderSystem.runAsFancy(() -> {
                lvt_16_1_.render(entity, 0.0, 0.0, 0.0, 0.0F, partialTicks, matrixStack, buffer,packedLight);
            });
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    }
}
