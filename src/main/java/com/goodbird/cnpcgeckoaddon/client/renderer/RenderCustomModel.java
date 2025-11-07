package com.goodbird.cnpcgeckoaddon.client.renderer;

import com.goodbird.cnpcgeckoaddon.client.model.ModelCustom;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.ArrayList;

public class RenderCustomModel extends GeoEntityRenderer<EntityCustomModel> {

    public RenderCustomModel(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCustom());
    }

    @Override
    public RenderType getRenderType(EntityCustomModel animatable, ResourceLocation texture, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void defaultRender(PoseStack poseStack, EntityCustomModel animatable, MultiBufferSource bufferSource, @org.jetbrains.annotations.Nullable RenderType renderType, @org.jetbrains.annotations.Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        BakedGeoModel bakedModel = getGeoModel().getBakedModel(getGeoModel().getModelResource(animatable));
        if (bakedModel.getBone("held_item").isPresent()) {
            GeoBone bone = bakedModel.getBone("held_item").get();
            bone.setHidden(true);
            if(!animatable.getMainHandItem().isEmpty()) {
                this.renderItem(bone, animatable, animatable.getMainHandItem(), poseStack, bufferSource, packedLight);
            }
        }
        if (bakedModel.getBone("left_held_item").isPresent()) {
            GeoBone bone = bakedModel.getBone("left_held_item").get();
            bone.setHidden(true);
            if(animatable.leftHeldItem!=null && !animatable.leftHeldItem.isEmpty()) {
                this.renderItem(bone, animatable, animatable.leftHeldItem, poseStack, bufferSource, packedLight);
            }
        }
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }

    public GeoBone[] getPathFromRoot(GeoBone bone) {
        ArrayList<GeoBone> bones;
        for (bones = new ArrayList<>(); bone != null; bone = bone.getParent()) {
            bones.add(0, bone);
        }

        return bones.toArray(new GeoBone[0]);
    }

    public void renderItem(GeoBone locator, EntityCustomModel entity, ItemStack stack, PoseStack poseStack, MultiBufferSource buf, int light) {
        poseStack.pushPose();
        float scale = 0.7F;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(180-entity.yBodyRot));
        GeoBone[] bonePath = getPathFromRoot(locator);

        for (GeoBone b : bonePath) {
            poseStack.translate(b.getPosX() / (16.0F * scale), b.getPosY() / (16.0F * scale), b.getPosZ() / (16.0F * scale));
            poseStack.translate(b.getPivotX() / (16.0F * scale), b.getPivotY() / (16.0F * scale), b.getPivotZ() / (16.0F * scale));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) ((double) b.getRotZ() / Math.PI * 180.0)));
            poseStack.mulPose(Axis.YP.rotationDegrees((float) ((double) b.getRotY() / Math.PI * 180.0)));
            poseStack.mulPose(Axis.XP.rotationDegrees((float) ((double) b.getRotX() / Math.PI * 180.0)));
            poseStack.scale(b.getScaleX(), b.getScaleY(), b.getScaleZ());
            poseStack.translate(-b.getPivotX() / (16.0F * scale), -b.getPivotY() / (16.0F * scale), -b.getPivotZ() / (16.0F * scale));
        }
        poseStack.translate(0,0,-0.4);
        poseStack.translate(locator.getPivotX()/10f*5f/6f,locator.getPivotY()/10f*12f/14f,0);
        poseStack.mulPose(Axis.XP.rotationDegrees(215f));
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED,light, OverlayTexture.NO_OVERLAY, poseStack, buf,null, 0);
        poseStack.popPose();
    }
}
