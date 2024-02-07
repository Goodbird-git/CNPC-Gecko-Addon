package com.goodbird.cnpcgeckoaddon;


import com.goodbird.cnpcgeckoaddon.data.CapabilityHandler;
import com.goodbird.cnpcgeckoaddon.data.CustomModelData;
import com.goodbird.cnpcgeckoaddon.data.CustomModelDataStorage;
import com.goodbird.cnpcgeckoaddon.data.ICustomModelData;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent ev){
        EntityRegistry.registerModEntity(new ResourceLocation(CNPCGeckoAddon.MODID,"CustomModelEntity"), EntityCustomModel.class, "Geckolib Model", 0, CNPCGeckoAddon.instance, 64, 10, false);
    }

    public void init(FMLInitializationEvent ev){
        NetworkWrapper.init();
        CapabilityManager.INSTANCE.register(ICustomModelData.class, new CustomModelDataStorage(), CustomModelData.class);
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
    }
}
