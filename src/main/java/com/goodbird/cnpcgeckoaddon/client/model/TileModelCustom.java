package com.goodbird.cnpcgeckoaddon.client.model;


import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TileModelCustom extends AnimatedGeoModel<TileEntityCustomModel> {
    @Override
    public ResourceLocation getAnimationFileLocation(TileEntityCustomModel entity) {
        return entity.animResLoc;
    }

    @Override
    public ResourceLocation getModelLocation(TileEntityCustomModel animatable) {
        return animatable.modelResLoc;
    }

    @Override
    public ResourceLocation getTextureLocation(TileEntityCustomModel entity) {
        return entity.textureResLoc;
    }
}
