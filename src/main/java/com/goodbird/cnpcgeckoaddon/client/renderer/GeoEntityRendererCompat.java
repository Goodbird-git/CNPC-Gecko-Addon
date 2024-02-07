package com.goodbird.cnpcgeckoaddon.client.renderer;

import com.goodbird.cnpcgeckoaddon.client.model.DummyVanilaModel;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.renderers.geo.RenderHurtColor;
import software.bernie.geckolib3.util.AnimationUtils;
import software.bernie.shadowed.eliotlash.mclib.utils.Interpolations;

public abstract class GeoEntityRendererCompat<T extends EntityLivingBase & IAnimatable> extends RenderLivingBase<T> implements IGeoRenderer<T> {
    protected final AnimatedGeoModel<T> modelProvider;
    protected final List<GeoLayerRenderer<T>> layerRenderers = Lists.newArrayList();

    public GeoEntityRendererCompat(RenderManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, new DummyVanilaModel(),0);
        this.modelProvider = modelProvider;
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        boolean shouldSit = entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit();
        EntityModelData entityModelData = new EntityModelData();
        entityModelData.isSitting = shouldSit;
        entityModelData.isChild = entity.isChild();
        float f = Interpolations.lerpYaw(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float f1 = Interpolations.lerpYaw(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float netHeadYaw = f1 - f;
        float f3;
        if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
            EntityLivingBase livingentity = (EntityLivingBase)entity.getRidingEntity();
            f = Interpolations.lerpYaw(livingentity.prevRenderYawOffset, livingentity.renderYawOffset, partialTicks);
            netHeadYaw = f1 - f;
            f3 = MathHelper.wrapDegrees(netHeadYaw);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            netHeadYaw = f1 - f;
        }

        float headPitch = Interpolations.lerp(entity.prevRotationPitch, entity.rotationPitch, partialTicks);
        f3 = this.handleRotationFloat(entity, partialTicks);
        this.applyRotations(entity, f3, f, partialTicks);
        float limbSwingAmount = 0.0F;
        float limbSwing = 0.0F;
        if (!shouldSit && entity.isEntityAlive()) {
            limbSwingAmount = Interpolations.lerp(entity.prevLimbSwingAmount, entity.limbSwingAmount, partialTicks);
            limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
            if (entity.isChild()) {
                limbSwing *= 3.0F;
            }

            if (limbSwingAmount > 1.0F) {
                limbSwingAmount = 1.0F;
            }
        }

        entityModelData.headPitch = -headPitch;
        entityModelData.netHeadYaw = -netHeadYaw;
        AnimationEvent predicate = new AnimationEvent((IAnimatable)entity, limbSwing, limbSwingAmount, partialTicks, !(limbSwingAmount > -0.15F) || !(limbSwingAmount < 0.15F), Collections.singletonList(entityModelData));
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(entity));
        if (this.modelProvider instanceof IAnimatableModel) {
            this.modelProvider.setLivingAnimations(entity, this.getUniqueID(entity), predicate);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.01F, 0.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(this.getEntityTexture(entity));
        Color renderColor = this.getRenderColor(entity, partialTicks);
        boolean flag = this.setDoRenderBrightness(entity, partialTicks);
        if (!entity.isInvisibleToPlayer(Minecraft.getMinecraft().player)) {
            this.render(model, entity, partialTicks, (float)renderColor.getRed() / 255.0F, (float)renderColor.getBlue() / 255.0F, (float)renderColor.getGreen() / 255.0F, (float)renderColor.getAlpha() / 255.0F);
        }

        if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
            Iterator var23 = this.layerRenderers.iterator();

            while(var23.hasNext()) {
                GeoLayerRenderer<T> layerRenderer = (GeoLayerRenderer)var23.next();
                layerRenderer.render(entity, limbSwing, limbSwingAmount, partialTicks, limbSwing, netHeadYaw, headPitch, renderColor);
            }
        }

        if (entity instanceof EntityLiving) {
            Entity leashHolder = ((EntityLiving)entity).getLeashHolder();
            if (leashHolder != null) {
                this.renderLeash((EntityLiving)entity, x, y, z, entityYaw, partialTicks);
            }
        }

        if (flag) {
            RenderHurtColor.unset();
        }

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    public ResourceLocation getEntityTexture(T entity) {
        return this.getTextureLocation(entity);
    }

    public GeoModelProvider getGeoModelProvider() {
        return this.modelProvider;
    }

    protected void applyRotations(T entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        if (!entityLiving.isPlayerSleeping()) {
            GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        }

        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            GlStateManager.rotate(f * this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
        } else if (entityLiving.hasCustomName() || entityLiving instanceof EntityPlayer) {
            String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0, (double)(entityLiving.height + 0.1F), 0.0);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }

    }

    protected boolean isVisible(T livingEntityIn) {
        return !livingEntityIn.isInvisible();
    }

    private static float getFacingAngle(EnumFacing facingIn) {
        switch (facingIn) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    public Integer getUniqueID(T animatable) {
        return animatable.getUniqueID().hashCode();
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 90.0F;
    }

    protected float getSwingProgress(T livingBase, float partialTickTime) {
        return livingBase.getSwingProgress(partialTickTime);
    }

    protected float handleRotationFloat(T livingBase, float partialTicks) {
        return (float)livingBase.ticksExisted + partialTicks;
    }

    public ResourceLocation getTextureLocation(T instance) {
        return this.modelProvider.getTextureLocation(instance);
    }

    public final boolean addLayer(GeoLayerRenderer<T> layer) {
        return this.layerRenderers.add(layer);
    }

    protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
        return RenderHurtColor.set(entityLivingBaseIn, partialTicks);
    }

    protected void renderLeash(EntityLiving entityLivingIn, double x, double y, double z, float entityYaw, float partialTicks) {
        Entity entity = entityLivingIn.getLeashHolder();
        if (entity != null) {
            y -= (1.6 - (double)entityLivingIn.height) * 0.5;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            double d0 = this.interpolateValue((double)entity.prevRotationYaw, (double)entity.rotationYaw, (double)(partialTicks * 0.5F)) * 0.01745329238474369;
            double d1 = this.interpolateValue((double)entity.prevRotationPitch, (double)entity.rotationPitch, (double)(partialTicks * 0.5F)) * 0.01745329238474369;
            double d2 = Math.cos(d0);
            double d3 = Math.sin(d0);
            double d4 = Math.sin(d1);
            if (entity instanceof EntityHanging) {
                d2 = 0.0;
                d3 = 0.0;
                d4 = -1.0;
            }

            double d5 = Math.cos(d1);
            double d6 = this.interpolateValue(entity.prevPosX, entity.posX, (double)partialTicks) - d2 * 0.7 - d3 * 0.5 * d5;
            double d7 = this.interpolateValue(entity.prevPosY + (double)entity.getEyeHeight() * 0.7, entity.posY + (double)entity.getEyeHeight() * 0.7, (double)partialTicks) - d4 * 0.5 - 0.25;
            double d8 = this.interpolateValue(entity.prevPosZ, entity.posZ, (double)partialTicks) - d3 * 0.7 + d2 * 0.5 * d5;
            double d9 = this.interpolateValue((double)entityLivingIn.prevRenderYawOffset, (double)entityLivingIn.renderYawOffset, (double)partialTicks) * 0.01745329238474369 + 1.5707963267948966;
            d2 = Math.cos(d9) * (double)entityLivingIn.width * 0.4;
            d3 = Math.sin(d9) * (double)entityLivingIn.width * 0.4;
            double d10 = this.interpolateValue(entityLivingIn.prevPosX, entityLivingIn.posX, (double)partialTicks) + d2;
            double d11 = this.interpolateValue(entityLivingIn.prevPosY, entityLivingIn.posY, (double)partialTicks);
            double d12 = this.interpolateValue(entityLivingIn.prevPosZ, entityLivingIn.posZ, (double)partialTicks) + d3;
            x += d2;
            z += d3;
            double d13 = (double)((float)(d6 - d10));
            double d14 = (double)((float)(d7 - d11));
            double d15 = (double)((float)(d8 - d12));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

            int k;
            float f4;
            float f5;
            float f6;
            float f7;
            for(k = 0; k <= 24; ++k) {
                f4 = 0.5F;
                f5 = 0.4F;
                f6 = 0.3F;
                if (k % 2 == 0) {
                    f4 *= 0.7F;
                    f5 *= 0.7F;
                    f6 *= 0.7F;
                }

                f7 = (float)k / 24.0F;
                bufferbuilder.pos(x + d13 * (double)f7 + 0.0, y + d14 * (double)(f7 * f7 + f7) * 0.5 + (double)((24.0F - (float)k) / 18.0F + 0.125F), z + d15 * (double)f7).color(f4, f5, f6, 1.0F).endVertex();
                bufferbuilder.pos(x + d13 * (double)f7 + 0.025, y + d14 * (double)(f7 * f7 + f7) * 0.5 + (double)((24.0F - (float)k) / 18.0F + 0.125F) + 0.025, z + d15 * (double)f7).color(f4, f5, f6, 1.0F).endVertex();
            }

            tessellator.draw();
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

            for(k = 0; k <= 24; ++k) {
                f4 = 0.5F;
                f5 = 0.4F;
                f6 = 0.3F;
                if (k % 2 == 0) {
                    f4 *= 0.7F;
                    f5 *= 0.7F;
                    f6 *= 0.7F;
                }

                f7 = (float)k / 24.0F;
                bufferbuilder.pos(x + d13 * (double)f7 + 0.0, y + d14 * (double)(f7 * f7 + f7) * 0.5 + (double)((24.0F - (float)k) / 18.0F + 0.125F) + 0.025, z + d15 * (double)f7).color(f4, f5, f6, 1.0F).endVertex();
                bufferbuilder.pos(x + d13 * (double)f7 + 0.025, y + d14 * (double)(f7 * f7 + f7) * 0.5 + (double)((24.0F - (float)k) / 18.0F + 0.125F), z + d15 * (double)f7 + 0.025).color(f4, f5, f6, 1.0F).endVertex();
            }

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }

    }

    private double interpolateValue(double start, double end, double pct) {
        return start + (end - start) * pct;
    }

    static {
        AnimationController.addModelFetcher((object) -> {
            return object instanceof Entity ? (IAnimatableModel)AnimationUtils.getGeoModelForEntity((Entity)object) : null;
        });
    }
}
