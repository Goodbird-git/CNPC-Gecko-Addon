package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import noppes.npcs.CustomNpcs;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@EventBusSubscriber(bus=EventBusSubscriber.Bus.MOD, modid=CNPCGeckoAddon.MODID)
public class NetworkWrapper {


    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registerPacket(registrar, PacketSyncAnimation.class,PacketSyncAnimation::encode,PacketSyncAnimation::decode,PacketSyncAnimation::handle);
        registerPacket(registrar, PacketSyncTileAnimation.class,PacketSyncTileAnimation::encode,PacketSyncTileAnimation::decode,PacketSyncTileAnimation::handle);
    }

    public static <MSG extends CustomPacketPayload> void registerPacket(PayloadRegistrar registrar , Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, TriConsumer<MSG, MinecraftServer, ServerPlayer> handle) {
        registrar.commonToServer(
                new CustomPacketPayload.Type<>(ResourceLocation.parse("cnpcgeckoaddon:"+messageType.getSimpleName().toLowerCase())),
                CustomPacketPayload.codec(encoder::accept, decoder::apply),
                (packet, context) -> handle.accept(packet, context.player().getServer(), (ServerPlayer) context.player())
        );
    }

    public static <MSG extends CustomPacketPayload> void registerPacket(PayloadRegistrar registrar , Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, Consumer<MSG> handle) {
        registrar.commonToClient(new CustomPacketPayload.Type<>(ResourceLocation.parse("cnpcgeckoaddon:"+messageType.getSimpleName().toLowerCase())), CustomPacketPayload.codec(encoder::accept, decoder::apply), (packet, context) -> handle.accept(packet));
    }

    public static <MSG extends CustomPacketPayload> void send(ServerPlayer player, MSG msg) {
        PacketDistributor.sendToPlayer(player, msg);
    }


    public static <MSG extends CustomPacketPayload> void sendAll(MSG msg) {
        for(ServerPlayer player: CustomNpcs.Server.getPlayerList().getPlayers()) {
            send(player, msg);
        }
    }
}
