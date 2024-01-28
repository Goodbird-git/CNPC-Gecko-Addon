package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
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
public abstract class MixinRenderNPCInterface <T extends EntityNPCInterface, M extends EntityModel<T>> extends LivingRenderer<T, M> {


    @Shadow(remap = false)
    public abstract void renderNameTag(T p2256231, ITextComponent content, MatrixStack p2256234, IRenderTypeBuffer p2256235, int p2256236);

    public MixinRenderNPCInterface() {
        super(null,null,0);
    }

    @Inject(method = "render(Lnoppes/npcs/entity/EntityNPCInterface;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/entity/LivingRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V"), cancellable = true)
    public void render(T npc, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, CallbackInfo ci) {
        if(npc instanceof EntityCustomNpc && ((EntityCustomNpc)npc).modelData.getEntity(npc) instanceof EntityCustomModel){
            cnpcgeckoaddon$renderGeoModel((EntityCustomNpc) npc,matrixStack,buffer,packedLight);
            cnpcgeckoaddon$drawNameStandalone(npc, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            ci.cancel();
        }
    }

    @Unique
    public void cnpcgeckoaddon$drawNameStandalone(T p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_){
        net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(p_225623_1_, p_225623_1_.getDisplayName(), this, p_225623_4_, p_225623_5_, p_225623_6_, p_225623_3_);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(p_225623_1_))) {
            this.renderNameTag(p_225623_1_, renderNameplateEvent.getContent(), p_225623_4_, p_225623_5_, p_225623_6_);
        }
    }


    @Unique
    private void cnpcgeckoaddon$renderGeoModel(EntityCustomNpc npc, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        Entity entity = npc.modelData.getEntity(npc);
        entity.yRot = entity.yRotO = 0;
        if (!npc.isInvisible())
        {
            EntityRendererManager lvt_16_1_ = Minecraft.getInstance().getEntityRenderDispatcher();
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
            EntityRendererManager lvt_16_1_ = Minecraft.getInstance().getEntityRenderDispatcher();
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
