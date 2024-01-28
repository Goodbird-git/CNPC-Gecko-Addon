package com.goodbird.cnpcgeckoaddon.mixin;

import com.goodbird.cnpcgeckoaddon.data.CustomModelData;

public interface IDataDisplay {
    CustomModelData getCustomModelData();

    boolean hasCustomModel();
}
