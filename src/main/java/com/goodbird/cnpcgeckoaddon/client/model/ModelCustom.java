package com.goodbird.cnpcgeckoaddon.client.model;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ModelCustom extends GeoModel<EntityCustomModel> {

    @Override
    public ResourceLocation getAnimationResource(EntityCustomModel animatable) {
        if(!GeckoLibCache.getBakedAnimations().containsKey(animatable.animResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","animations/none.animations.json");
        }
        return animatable.animResLoc;
    }

    @Override
    public ResourceLocation getModelResource(EntityCustomModel animatable) {
        if(!GeckoLibCache.getBakedModels().containsKey(animatable.modelResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","geo/modelnotfound.geo.json");
        }
        if(!GeckoLibCache.getBakedAnimations().containsKey(animatable.animResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","geo/animfilenotfound.geo.json");
        }
        return animatable.modelResLoc;
    }

    @Override
    public ResourceLocation getTextureResource(EntityCustomModel animatable) {
        if(!GeckoLibCache.getBakedModels().containsKey(animatable.modelResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","textures/model/alphabet.png");
        }
        if(!GeckoLibCache.getBakedAnimations().containsKey(animatable.animResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","textures/model/alphabet.png");
        }
        return animatable.textureResLoc;
    }

    @Override
    public void setCustomAnimations(EntityCustomModel animatable, long instanceId, AnimationState<EntityCustomModel> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone head = getAnimationProcessor().getBone(animatable.headBoneName);

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}