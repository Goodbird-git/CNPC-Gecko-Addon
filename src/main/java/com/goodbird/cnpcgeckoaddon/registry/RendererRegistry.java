package com.goodbird.cnpcgeckoaddon.registry;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import com.goodbird.cnpcgeckoaddon.client.renderer.RenderCustomModel;
import com.goodbird.cnpcgeckoaddon.client.renderer.RenderTileCustomModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class RendererRegistry {

    public static void registerRenderers() {
        EntityRendererRegistry.register(EntityRegistry.entityCustomModel, RenderCustomModel::new);
        BlockEntityRenderers.register(TileEntityRegistry.tileEntityCustomModel, context -> new RenderTileCustomModel());
    }
}
