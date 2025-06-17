package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.CustomNpcs;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class NetworkWrapper {


    public static void init() {
        registerPacket(PacketSyncAnimation.class,PacketSyncAnimation::encode,PacketSyncAnimation::decode,PacketSyncAnimation::handle);
        registerPacket(PacketSyncTileAnimation.class,PacketSyncTileAnimation::encode,PacketSyncTileAnimation::decode,PacketSyncTileAnimation::handle);
    }

    public static <MSG extends CustomPacketPayload> void registerPacket(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, TriConsumer<MSG, MinecraftServer, ServerPlayer> handle) {
        PayloadTypeRegistry.playC2S().register(CustomPacketPayload.createType("cnpcgeckoaddon"+messageType.getSimpleName().toLowerCase()), CustomPacketPayload.codec(encoder::accept, decoder::apply));
        ServerPlayNetworking.registerGlobalReceiver(CustomPacketPayload.<MSG>createType("cnpcgeckoaddon"+messageType.getSimpleName().toLowerCase()), (packet, context) -> handle.accept(packet, context.player().server, context.player()));
    }

    public static <MSG extends CustomPacketPayload> void registerPacket(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, Consumer<MSG> handle) {
        PayloadTypeRegistry.playS2C().register(CustomPacketPayload.createType("cnpcgeckoaddon"+messageType.getSimpleName().toLowerCase()), CustomPacketPayload.codec(encoder::accept, decoder::apply));
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(CustomPacketPayload.<MSG>createType("cnpcgeckoaddon"+messageType.getSimpleName().toLowerCase()), (packet, context) -> handle.accept(packet));
        }
    }

    public static <MSG extends CustomPacketPayload> void send(ServerPlayer player, MSG msg) {
        ServerPlayNetworking.send(player, msg);
    }


    public static <MSG extends CustomPacketPayload> void sendAll(MSG msg) {
        for(ServerPlayer player: CustomNpcs.Server.getPlayerList().getPlayers()) {
            send(player, msg);
        }
    }
}
