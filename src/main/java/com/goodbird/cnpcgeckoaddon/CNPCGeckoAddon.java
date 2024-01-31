package com.goodbird.cnpcgeckoaddon;

import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CNPCGeckoAddon.MODID)
public class CNPCGeckoAddon {
    public static final String MODID = "cnpcgeckoaddon";

    public CNPCGeckoAddon() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkWrapper.init();
    }
}
