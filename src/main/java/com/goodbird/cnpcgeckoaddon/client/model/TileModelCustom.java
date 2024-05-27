package com.goodbird.cnpcgeckoaddon.client.model;

import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class TileModelCustom extends AnimatedGeoModel<TileEntityCustomModel> {
    @Override
    public ResourceLocation getAnimationFileLocation(TileEntityCustomModel entity) {
        if(!GeckoLibCache.getInstance().getAnimations().containsKey(entity.animResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","animations/none.animations.json");
        }
        return entity.animResLoc;
    }

    @Override
    public ResourceLocation getModelLocation(TileEntityCustomModel animatable) {
        if(!GeckoLibCache.getInstance().getGeoModels().containsKey(animatable.modelResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","geo/modelnotfound.geo.json");
        }
        if(!GeckoLibCache.getInstance().getAnimations().containsKey(animatable.animResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","geo/animfilenotfound.geo.json");
        }
        return animatable.modelResLoc;
    }

    @Override
    public ResourceLocation getTextureLocation(TileEntityCustomModel entity) {
        if(!GeckoLibCache.getInstance().getGeoModels().containsKey(entity.modelResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","textures/model/alphabet.png");
        }
        if(!GeckoLibCache.getInstance().getAnimations().containsKey(entity.animResLoc)){
            return new ResourceLocation("cnpcgeckoaddon","textures/model/alphabet.png");
        }
        return entity.textureResLoc;
    }
}
