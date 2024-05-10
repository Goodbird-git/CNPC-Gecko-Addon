package com.goodbird.cnpcgeckoaddon.client;

import com.goodbird.cnpcgeckoaddon.client.renderer.RenderTileCustomModel;
import com.goodbird.cnpcgeckoaddon.registry.EntityRegistry;
import com.goodbird.cnpcgeckoaddon.registry.TileEntityRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import com.goodbird.cnpcgeckoaddon.client.renderer.RenderCustomModel;
import software.bernie.example.client.renderer.tile.HabitatTileRenderer;
import software.bernie.example.registry.TileRegistry;

public class ClientProxy {
    public static void load(){
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.entityCustomModel, RenderCustomModel::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.tileEntityCustomModel, RenderTileCustomModel::new);
    }
}
