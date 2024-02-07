package com.goodbird.cnpcgeckoaddon;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CNPCGeckoAddon.MODID,
        name = "CNPC-Gecko-Addon",
        version = "1.0",
        dependencies = "required-after:customnpcs;required-after:geckolib3")

public class CNPCGeckoAddon {
    public static final String MODID = "cnpcgeckoaddon";

    @SidedProxy(clientSide = "com.goodbird.cnpcgeckoaddon.client.ClientProxy", serverSide = "com.goodbird.cnpcgeckoaddon.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static CNPCGeckoAddon instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        proxy.preInit(ev);
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent ev) {
        proxy.init(ev);
    }
}
