package com.goodbird.cnpcgeckoaddon.client.renderer;

import com.goodbird.cnpcgeckoaddon.client.model.TileModelCustom;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class RenderTileCustomModel extends GeoBlockRenderer<TileEntityCustomModel> {
    public RenderTileCustomModel() {
        super(new TileModelCustom());
    }
}
