package com.goodbird.cnpcgeckoaddon.client.renderer;

import com.goodbird.cnpcgeckoaddon.client.model.ModelCustom;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class RenderCustomModel extends GeoEntityRendererCompat<EntityCustomModel> {

    public RenderCustomModel(RenderManager renderManager) {
        super(renderManager, new ModelCustom());
    }

    @Override
    public void render(GeoModel model, EntityCustomModel animatable,  float partialTicks, float red, float green, float blue, float alpha) {
        if (model.getBone("held_item").isPresent()) {
            GeoBone bone = model.getBone("held_item").get();
            bone.isHidden = true;
            if(!animatable.getHeldItemMainhand().isEmpty()) {
                this.renderItem(animatable, bone, animatable.getHeldItemMainhand());
            }
        }
        if (model.getBone("left_held_item").isPresent()) {
            GeoBone bone = model.getBone("left_held_item").get();
            bone.isHidden = true;
            if(animatable.leftHeldItem!=null && !animatable.leftHeldItem.isEmpty()) {
                this.renderItem(animatable, bone, animatable.leftHeldItem);
            }
        }
        super.render(model, animatable, partialTicks,red,green,blue,alpha);
    }

    public GeoBone[] getPathFromRoot(GeoBone bone) {
        ArrayList<GeoBone> bones;
        for (bones = new ArrayList<>(); bone != null; bone = bone.parent) {
            bones.add(0, bone);
        }

        return bones.toArray(new GeoBone[0]);
    }

    public void renderItem(EntityCustomModel animatable, GeoBone locator, ItemStack stack) {
        GL11.glPushMatrix();
        float scale = 0.7F;
        GL11.glScaled(scale, scale, scale);
        GeoBone[] bonePath = this.getPathFromRoot(locator);

        for (GeoBone b : bonePath) {
            GL11.glTranslatef(b.getPositionX() / (16.0F * scale), b.getPositionY() / (16.0F * scale), b.getPositionZ() / (16.0F * scale));
            GL11.glTranslatef(b.getPivotX() / (16.0F * scale), b.getPivotY() / (16.0F * scale), b.getPivotZ() / (16.0F * scale));
            GL11.glRotated((double) b.getRotationZ() / Math.PI * 180.0, 0.0, 0.0, 1.0);
            GL11.glRotated((double) b.getRotationY() / Math.PI * 180.0, 0.0, 1.0, 0.0);
            GL11.glRotated((double) b.getRotationX() / Math.PI * 180.0, 1.0, 0.0, 0.0);
            GL11.glScalef(b.getScaleX(), b.getScaleY(), b.getScaleZ());
            GL11.glTranslatef(-b.getPivotX() / (16.0F * scale), -b.getPivotY() / (16.0F * scale), -b.getPivotZ() / (16.0F * scale));
        }
        GL11.glTranslated(0,0,-0.4);
        GL11.glTranslated(locator.getPivotX()/10f*5f/6f,locator.getPivotY()/10f*12f/14f,0);
        GL11.glRotatef(215,1,0,0);
        GL11.glRotatef(90,0,1,0);
        Minecraft.getMinecraft().getItemRenderer().renderItem(animatable, stack, ItemCameraTransforms.TransformType.FIXED);
        GL11.glPopMatrix();
        ResourceLocation entityTexture = getEntityTexture(animatable);
        if(entityTexture!=null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(entityTexture);
        }
    }
}
