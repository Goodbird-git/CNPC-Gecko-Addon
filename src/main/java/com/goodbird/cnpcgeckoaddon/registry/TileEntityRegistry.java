package com.goodbird.cnpcgeckoaddon.registry;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CNPCGeckoAddon.MODID)
public class TileEntityRegistry {

    @ObjectHolder(registryName ="block_entity_type", value = CNPCGeckoAddon.MODID+":custommodeltileentity")
    public static BlockEntityType<? extends TileEntityCustomModel> tileEntityCustomModel;

    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        if (event.getRegistryKey() == ForgeRegistries.Keys.BLOCK_ENTITY_TYPES) {
            event.getForgeRegistry().register(CNPCGeckoAddon.MODID+":custommodeltileentity", createTile("custommodeltileentity",TileEntityCustomModel::new));
        }
    }

    private static BlockEntityType<?> createTile(String key, BlockEntityType.BlockEntitySupplier factoryIn, Block... blocks){
        BlockEntityType.Builder<BlockEntity> builder = BlockEntityType.Builder.of(factoryIn, blocks);
        return builder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, key));
    }
}
