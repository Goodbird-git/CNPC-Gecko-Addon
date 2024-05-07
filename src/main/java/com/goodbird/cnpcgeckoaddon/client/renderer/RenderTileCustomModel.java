package com.goodbird.cnpcgeckoaddon.client.renderer;

import com.goodbird.cnpcgeckoaddon.client.model.TileModelCustom;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class RenderTileCustomModel extends GeoBlockRenderer<TileEntityCustomModel> {
    public RenderTileCustomModel(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new TileModelCustom());
    }
}
