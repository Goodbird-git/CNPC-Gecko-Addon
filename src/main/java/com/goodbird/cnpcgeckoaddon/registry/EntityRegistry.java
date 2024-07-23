package com.goodbird.cnpcgeckoaddon.registry;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CNPCGeckoAddon.MODID)
public class EntityRegistry {

    @ObjectHolder(registryName ="entity_type", value = CNPCGeckoAddon.MODID+":custommodelentity")
    public static EntityType<? extends EntityCustomModel> entityCustomModel;

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        if (event.getRegistryKey() == ForgeRegistries.Keys.ENTITY_TYPES) {
            registerNewentity(event.getForgeRegistry(), EntityCustomModel.class, "custommodelentity", EntityCustomModel::new, 64, 10, false, 0.7F, 2F);
        }
    }

    private static <T extends Entity> void registerNewentity(final IForgeRegistry<EntityType<?>> registry, final Class<? extends Entity> c, final String name, final EntityType.EntityFactory<T> factoryIn, final int range, final int update, final boolean velocity, final float width, final float height) {
        final EntityType.Builder<?> builder = EntityType.Builder.of(factoryIn, MobCategory.MISC);
        builder.setTrackingRange(range);
        builder.setUpdateInterval(update);
        builder.setShouldReceiveVelocityUpdates(velocity);
        builder.sized(width, height);
        builder.clientTrackingRange(4);
        final ResourceLocation registryName = new ResourceLocation(CNPCGeckoAddon.MODID, name);
        registry.register(registryName, builder.build(registryName.toString()));
    }


    @SubscribeEvent
    public static void attribute(final EntityAttributeCreationEvent event) {
        event.put(entityCustomModel, LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE).build());
    }
}
