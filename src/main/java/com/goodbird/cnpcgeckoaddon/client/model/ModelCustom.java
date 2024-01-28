package com.goodbird.cnpcgeckoaddon.client.model;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelCustom extends AnimatedGeoModel<EntityCustomModel> {
    @Override
    public ResourceLocation getAnimationFileLocation(EntityCustomModel entity) {
        return entity.animResLoc;
    }

    @Override
    public ResourceLocation getModelLocation(EntityCustomModel entity) {
        return entity.modelResLoc;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCustomModel entity) {
        return entity.textureResLoc;
    }
}