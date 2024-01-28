package com.goodbird.cnpcgeckoaddon.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class DummyVanilaModel<T extends Entity>  extends SegmentedModel<T> {

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of();
    }

    public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }
}
