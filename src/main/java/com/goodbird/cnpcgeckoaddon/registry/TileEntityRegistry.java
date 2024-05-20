package com.goodbird.cnpcgeckoaddon.registry;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CNPCGeckoAddon.MODID)
@ObjectHolder(CNPCGeckoAddon.MODID)
public class TileEntityRegistry {

    @ObjectHolder("custommodeltileentity")
    public static BlockEntityType<? extends TileEntityCustomModel> tileEntityCustomModel;

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().register(createTile("custommodeltileentity", TileEntityCustomModel::new));
    }

    private static BlockEntityType<?> createTile(String key, BlockEntityType.BlockEntitySupplier<? extends BlockEntity> factoryIn) {
        BlockEntityType.Builder<BlockEntity> builder = BlockEntityType.Builder.of(factoryIn, new Block[]{});
        return builder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, key)).setRegistryName(key);
    }
}
