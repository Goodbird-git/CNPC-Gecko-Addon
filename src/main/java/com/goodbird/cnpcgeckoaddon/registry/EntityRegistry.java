package com.goodbird.cnpcgeckoaddon.registry;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import noppes.npcs.CustomNpcs;

public class EntityRegistry {

    public static EntityType<? extends EntityCustomModel> entityCustomModel;

    public static void registerEntities() {
        entityCustomModel = registerNewentity(EntityCustomModel.class, "custommodelentity", EntityCustomModel::new, 64, 10, false, 0.7F, 2F);
    }

    private static <T extends Entity> EntityType<T> registerNewentity(Class<? extends Entity> c, String name, EntityType.EntityFactory<T> factoryIn, int range, int update, boolean velocity, float width, float height) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factoryIn, MobCategory.MISC);
        builder.updateInterval(update);
        builder.sized(width, height);
        builder.clientTrackingRange(4);
        ResourceLocation registryName = ResourceLocation.fromNamespaceAndPath(CustomNpcs.MODID, name);
        EntityType<T> type = builder.build(registryName.toString());
        Registry.register(BuiltInRegistries.ENTITY_TYPE, registryName, type);
        return type;
    }


    public static void attribute() {
        FabricDefaultAttributeRegistry.register(entityCustomModel, LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE).build());
    }
}
