package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
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
    private static final String PROTOCOL = "10";
    public static HashMap<Class, Integer> indexes = new HashMap<>();
    public static HashMap<Class, BiConsumer> encoders = new HashMap<>();


    public static void init() {
        registerPacket(0,PacketSyncAnimation.class,PacketSyncAnimation::encode,PacketSyncAnimation::decode,PacketSyncAnimation::handle);
        registerPacket(1,PacketSyncTileAnimation.class,PacketSyncTileAnimation::encode,PacketSyncTileAnimation::decode,PacketSyncTileAnimation::handle);
    }

    public static <MSG> void registerPacket(int index, Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, TriConsumer<MSG, MinecraftServer, ServerPlayer> handle) {
        indexes.put(messageType, index);
        encoders.put(messageType, encoder);
        ServerPlayNetworking.registerGlobalReceiver(new ResourceLocation("customnpcs", ""+index), (server, player, _handler, buf, _responseSender) -> handle.accept(decoder.apply(buf), server, player));
    }
    public static <MSG> void registerPacket(int index, Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, Consumer<MSG> handle) {
        indexes.put(messageType, index);
        encoders.put(messageType, encoder);
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation("customnpcs", "" + index), (client, _handler, buf, _responseSender) -> handle.accept(decoder.apply(buf)));
        }
    }

    public static <MSG> void send(ServerPlayer player, MSG msg) {
        FriendlyByteBuf ret = new FriendlyByteBuf(Unpooled.buffer());
        encoders.get(msg.getClass()).accept(msg, ret);
        ServerPlayNetworking.send(player, new ResourceLocation("customnpcs", ""+indexes.get(msg.getClass())), ret);
    }


    public static <MSG> void sendAll(MSG msg) {
        for(ServerPlayer player: CustomNpcs.Server.getPlayerList().getPlayers()) {
            send(player, msg);
        }
    }
}
