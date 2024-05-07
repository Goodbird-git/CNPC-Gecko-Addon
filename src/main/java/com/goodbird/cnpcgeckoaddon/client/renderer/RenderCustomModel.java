package com.goodbird.cnpcgeckoaddon.client.renderer;

import com.goodbird.cnpcgeckoaddon.client.model.ModelCustom;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class RenderCustomModel extends GeoEntityRendererCompat<EntityCustomModel> {

    public RenderCustomModel(EntityRendererManager renderManager) {
        super(renderManager, new ModelCustom());
    }

    @Override
    public RenderType getRenderType(EntityCustomModel animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    protected void applyRotations(EntityCustomModel entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw,
                                  float partialTicks) {
        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        }

        if (entityLiving.deathTime > 0) {
            float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(f * this.getDeathMaxRotation(entityLiving)));
        } else if (entityLiving.isAutoSpinAttack()) {
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.xRot));
            matrixStackIn
                    .mulPose(Vector3f.YP.rotationDegrees(((float) entityLiving.tickCount + partialTicks) * -75.0F));
        } else if (pose == Pose.SLEEPING) {
            Direction direction = entityLiving.getBedOrientation();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f1));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(this.getDeathMaxRotation(entityLiving)));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270.0F));
        } else if (entityLiving.hasCustomName()) {
            String s = TextFormatting.stripFormatting(entityLiving.getName().getString());
            if ("Dinnerbone".equals(s) || "Grumm".equals(s)) {
                matrixStackIn.translate(0.0D, entityLiving.getBbHeight() + 0.1F, 0.0D);
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            }
        }

    }


    @Override
    public void render(GeoModel model, EntityCustomModel animatable, float partialTick, RenderType type, MatrixStack matrixStackIn,
                       @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn,
                       int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (model.getBone("held_item").isPresent()) {
            GeoBone bone = model.getBone("held_item").get();
            bone.isHidden = true;
            if(!animatable.getMainHandItem().isEmpty()) {
                this.renderItem(bone, animatable.getMainHandItem(), matrixStackIn, renderTypeBuffer, packedLightIn);
            }
        }
        if (model.getBone("left_held_item").isPresent()) {
            GeoBone bone = model.getBone("left_held_item").get();
            bone.isHidden = true;
            if(animatable.leftHeldItem!=null && !animatable.leftHeldItem.isEmpty()) {
                this.renderItem(bone, animatable.leftHeldItem, matrixStackIn, renderTypeBuffer, packedLightIn);
            }
        }
        super.render(model, animatable, partialTick, type,matrixStackIn,renderTypeBuffer,vertexBuilder,packedLightIn,packedOverlayIn,red,green,blue,alpha);
    }

    public GeoBone[] getPathFromRoot(GeoBone bone) {
        ArrayList<GeoBone> bones;
        for (bones = new ArrayList<>(); bone != null; bone = bone.parent) {
            bones.add(0, bone);
        }

        return bones.toArray(new GeoBone[0]);
    }

    public void renderItem(GeoBone locator, ItemStack stack, MatrixStack poseStack, IRenderTypeBuffer buf, int light) {
        poseStack.pushPose();
        float scale = 0.7F;
        poseStack.scale(scale, scale, scale);
        GeoBone[] bonePath = getPathFromRoot(locator);

        for (GeoBone b : bonePath) {
            poseStack.translate(b.getPositionX() / (16.0F * scale), b.getPositionY() / (16.0F * scale), b.getPositionZ() / (16.0F * scale));
            poseStack.translate(b.getPivotX() / (16.0F * scale), b.getPivotY() / (16.0F * scale), b.getPivotZ() / (16.0F * scale));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) ((double) b.getRotationZ() / Math.PI * 180.0)));
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) ((double) b.getRotationY() / Math.PI * 180.0)));
            poseStack.mulPose(Vector3f.XP.rotationDegrees((float) ((double) b.getRotationX() / Math.PI * 180.0)));
            poseStack.scale(b.getScaleX(), b.getScaleY(), b.getScaleZ());
            poseStack.translate(-b.getPivotX() / (16.0F * scale), -b.getPivotY() / (16.0F * scale), -b.getPivotZ() / (16.0F * scale));
        }
        poseStack.translate(0,0,-0.4);
        poseStack.translate(locator.getPivotX()/10f*5f/6f,locator.getPivotY()/10f*12f/14f,0);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(215f));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED,light, OverlayTexture.NO_OVERLAY, poseStack, buf);
        poseStack.popPose();
    }
}
