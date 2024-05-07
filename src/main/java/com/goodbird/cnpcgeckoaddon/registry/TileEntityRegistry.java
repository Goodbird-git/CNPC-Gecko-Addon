package com.goodbird.cnpcgeckoaddon.registry;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CNPCGeckoAddon.MODID)
@ObjectHolder(CNPCGeckoAddon.MODID)
public class TileEntityRegistry {

    @ObjectHolder("custommodeltileentity")
    public static TileEntityType<? extends TileEntityCustomModel> tileEntityCustomModel;

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(createTile("custommodeltileentity", TileEntityCustomModel::new));
    }

    private static TileEntityType<?> createTile(String key, Supplier<? extends TileEntity> factoryIn) {
        TileEntityType.Builder<TileEntity> builder = TileEntityType.Builder.of(factoryIn, new Block[]{});
        return builder.build(Util.fetchChoiceType(TypeReferences.BLOCK_ENTITY, key)).setRegistryName(key);
    }
}
