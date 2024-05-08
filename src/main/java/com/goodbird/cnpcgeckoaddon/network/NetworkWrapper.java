package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class NetworkWrapper {
    private static final String PROTOCOL = "10";
    public static final SimpleChannel wrapper = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CNPCGeckoAddon.MODID,"chan"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );


    public static void init() {
        wrapper.registerMessage(0,PacketSyncAnimation.class,PacketSyncAnimation::encode,PacketSyncAnimation::decode,PacketSyncAnimation::handle);
        wrapper.registerMessage(1,PacketSyncTileAnimation.class,PacketSyncTileAnimation::encode,PacketSyncTileAnimation::decode,PacketSyncTileAnimation::handle);
    }


    public static void sendToPlayer(Object message, ServerPlayerEntity player) {
        wrapper.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }


    public static void sendToAll(Object message) {
        for(ServerPlayerEntity player:ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
            sendToPlayer(message,player);
    }
}
