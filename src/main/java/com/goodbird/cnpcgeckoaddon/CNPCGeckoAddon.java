package com.goodbird.cnpcgeckoaddon;

import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import com.goodbird.cnpcgeckoaddon.registry.EntityRegistry;
import com.goodbird.cnpcgeckoaddon.registry.TileEntityRegistry;
import net.fabricmc.api.ModInitializer;

public class CNPCGeckoAddon implements ModInitializer {
    public static final String MODID = "cnpcgeckoaddon";

    public CNPCGeckoAddon() {

    }

    @Override
    public void onInitialize() {
        EntityRegistry.registerEntities();
        EntityRegistry.attribute();
        TileEntityRegistry.registerBlocks();
        NetworkWrapper.init();
    }
}
