package com.goodbird.cnpcgeckoaddon.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class NetworkWrapper {
    private static final SimpleNetworkWrapper wrapper = new SimpleNetworkWrapper("npcgecko");

    public static void init() {
        wrapper.registerMessage(PacketSyncAnimation.class, PacketSyncAnimation.class, 0, Side.CLIENT);
        wrapper.registerMessage(CPacketSyncTileManualAnim.class, CPacketSyncTileManualAnim.class, 1, Side.CLIENT);
    }

    public static void sendToPlayer(IMessage message, EntityPlayer player) {
        wrapper.sendTo(message, (EntityPlayerMP) player);
    }

    public static void sendToAll(IMessage message) {
        wrapper.sendToAll(message);
    }

    public static void sendToServer(IMessage message) {
        wrapper.sendToServer(message);
    }
}
