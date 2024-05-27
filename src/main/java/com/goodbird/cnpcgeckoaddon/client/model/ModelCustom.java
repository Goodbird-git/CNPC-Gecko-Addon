package com.goodbird.cnpcgeckoaddon.client.model;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class ModelCustom extends AnimatedGeoModel<EntityCustomModel> {
    @Override
    public ResourceLocation getAnimationFileLocation(EntityCustomModel entity) {
        if(!GeckoLibCache.getInstance().getAnimations().containsKey(entity.animResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","animations/none.animations.json");
        }
        return entity.animResLoc;
    }

    @Override
    public ResourceLocation getModelLocation(EntityCustomModel entity) {
        if(!GeckoLibCache.getInstance().getGeoModels().containsKey(entity.modelResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","geo/modelnotfound.geo.json");
        }
        if(!GeckoLibCache.getInstance().getAnimations().containsKey(entity.animResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","geo/animfilenotfound.geo.json");
        }
        return entity.modelResLoc;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCustomModel entity) {
        if(!GeckoLibCache.getInstance().getGeoModels().containsKey(entity.modelResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","textures/model/alphabet.png");
        }
        if(!GeckoLibCache.getInstance().getAnimations().containsKey(entity.animResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","textures/model/alphabet.png");
        }
        return entity.textureResLoc;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setLivingAnimations(EntityCustomModel entity, Integer uniqueID, AnimationEvent animationEvent) {
        super.setLivingAnimations(entity, uniqueID, animationEvent); //We call the super-function
        IBone head = this.getAnimationProcessor().getBone(entity.headBoneName); //Then we take the head bone
        if(head!=null) {
            //We get the model data for an entity
            EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
            //And we set the head bone rotation to the interpolated pitch and yaw rotations of an entity
            head.setRotationX((extraData.headPitch) * ((float) Math.PI / 180F));
            head.setRotationY((extraData.netHeadYaw) * ((float) Math.PI / 180F));
        }
    }
}