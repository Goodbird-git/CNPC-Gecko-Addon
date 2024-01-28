package com.goodbird.cnpcgeckoaddon.registry;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CNPCGeckoAddon.MODID)
@ObjectHolder(CNPCGeckoAddon.MODID)
public class EntityRegistry {

    @ObjectHolder("custommodelentity")
    public static EntityType<? extends EntityCustomModel> entityCustomModel;

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        registerNewentity(event.getRegistry(), EntityCustomModel.class, "custommodelentity", EntityCustomModel::new, 64, 10, false, 0.7F, 2F);
    }

    private static <T extends Entity> void registerNewentity(final IForgeRegistry<EntityType<?>> registry, final Class<? extends Entity> c, final String name, final EntityType.IFactory<T> factoryIn, final int range, final int update, final boolean velocity, final float width, final float height) {
        final EntityType.Builder<?> builder = (EntityType.Builder<?>)EntityType.Builder.of((EntityType.IFactory)factoryIn, EntityClassification.MISC);
        builder.setTrackingRange(range);
        builder.setUpdateInterval(update);
        builder.setShouldReceiveVelocityUpdates(velocity);
        builder.sized(width, height);
        builder.clientTrackingRange(4);
        final ResourceLocation registryName = new ResourceLocation(CNPCGeckoAddon.MODID, name);
        registry.register(builder.build(registryName.toString()).setRegistryName(registryName));
    }


    @SubscribeEvent
    public static void attribute(final EntityAttributeCreationEvent event) {
        event.put(entityCustomModel, LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE).build());
    }
}
