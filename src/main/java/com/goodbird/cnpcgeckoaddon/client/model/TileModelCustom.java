package com.goodbird.cnpcgeckoaddon.client.model;


import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.model.GeoModel;

public class TileModelCustom extends GeoModel<TileEntityCustomModel> {
    @Override
    public ResourceLocation getAnimationResource(TileEntityCustomModel animatable) {
        if(!GeckoLibCache.getBakedAnimations().containsKey(animatable.animResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","animations/none.animations.json");
        }
        return animatable.animResLoc;
    }

    @Override
    public ResourceLocation getModelResource(TileEntityCustomModel animatable) {
        if(!GeckoLibCache.getBakedModels().containsKey(animatable.modelResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","geo/modelnotfound.geo.json");
        }
        if(!GeckoLibCache.getBakedAnimations().containsKey(animatable.animResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","geo/animfilenotfound.geo.json");
        }
        return animatable.modelResLoc;
    }

    @Override
    public ResourceLocation getTextureResource(TileEntityCustomModel animatable) {
        if(!GeckoLibCache.getBakedModels().containsKey(animatable.modelResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","textures/model/alphabet.png");
        }
        if(!GeckoLibCache.getBakedAnimations().containsKey(animatable.animResLoc)){
            return ResourceLocation.fromNamespaceAndPath("cnpcgeckoaddon","textures/model/alphabet.png");
        }
        return animatable.textureResLoc;
    }
}
