package com.goodbird.cnpcgeckoaddon;

import com.goodbird.cnpcgeckoaddon.registry.RendererRegistry;
import net.fabricmc.api.ClientModInitializer;

public class CNPCGeckoAddonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RendererRegistry.registerRenderers();
    }
}
