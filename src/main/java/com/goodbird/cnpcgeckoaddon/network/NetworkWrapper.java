package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

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
    }


    public static void sendToPlayer(Object message, ServerPlayer player) {
        wrapper.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }


    public static void sendToAll(Object message) {
        for(ServerPlayer player: ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
            sendToPlayer(message,player);
    }
}
