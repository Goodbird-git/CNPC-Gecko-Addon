package com.goodbird.cnpcgeckoaddon.data;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.entity.EntityCustomNpc;

/**
 * Capability handler
 *
 * This class is responsible for attaching our capabilities
 */
public class CapabilityHandler
{
    public static final ResourceLocation MANA_CAP = new ResourceLocation(CNPCGeckoAddon.MODID, "custommodeldata");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof EntityCustomNpc)) return;

        event.addCapability(MANA_CAP, new CustomModelDataProvider());
    }
}