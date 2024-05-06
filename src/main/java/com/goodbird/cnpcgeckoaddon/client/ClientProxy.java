package com.goodbird.cnpcgeckoaddon.client;

import com.goodbird.cnpcgeckoaddon.CommonProxy;
import com.goodbird.cnpcgeckoaddon.client.renderer.RenderCustomModel;
import com.goodbird.cnpcgeckoaddon.client.renderer.RenderTileCustomModel;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import software.bernie.geckolib3.GeckoLib;

public class ClientProxy  extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {
        super.preInit(ev);
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomModel.class, RenderCustomModel::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCustomModel.class, new RenderTileCustomModel());
    }

    @Override
    public void init(FMLInitializationEvent ev) {
        super.init(ev);
        GeckoLib.initialize();
    }
}
