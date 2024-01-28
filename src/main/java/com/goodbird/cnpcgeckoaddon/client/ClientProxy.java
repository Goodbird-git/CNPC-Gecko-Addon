package com.goodbird.cnpcgeckoaddon.client;

import com.goodbird.cnpcgeckoaddon.registry.EntityRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import com.goodbird.cnpcgeckoaddon.client.renderer.RenderCustomModel;

public class ClientProxy {
    public static void load(){
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.entityCustomModel, RenderCustomModel::new);
    }
}
