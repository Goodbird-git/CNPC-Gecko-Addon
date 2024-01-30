package com.goodbird.cnpcgeckoaddon.utils;


import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.renderer.ImageDownloadAlt;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

import java.io.File;
import java.security.MessageDigest;
import java.util.Map;

public class NpcTextureUtils {
    public static ResourceLocation getNpcTexture(EntityNPCInterface npc) {
        if (npc.textureLocation == null) {
            if (npc.display.skinType == 0) {
                npc.textureLocation = new ResourceLocation(npc.display.getSkinTexture());
            } else {
                if (RenderNPCInterface.LastTextureTick < 5) {
                    return DefaultPlayerSkin.getDefaultSkin();
                }

                if (npc.display.skinType == 1 && npc.display.playerProfile != null) {
                    Minecraft minecraft = Minecraft.getInstance();
                    Map map = minecraft.getSkinManager().getInsecureSkinInformation(npc.display.playerProfile);
                    if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                        npc.textureLocation = minecraft.getSkinManager().registerTexture((MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                    }
                } else if (npc.display.skinType == 2) {
                    try {
                        String size = "";
                        if (npc instanceof EntityCustomNpc && ((EntityCustomNpc) npc).modelData.getEntity(npc) != null) {
                            size = "32";
                        }

                        MessageDigest digest = MessageDigest.getInstance("MD5");
                        byte[] hash = digest.digest((npc.display.getSkinUrl() + size).getBytes("UTF-8"));
                        StringBuilder sb = new StringBuilder(2 * hash.length);
                        byte[] var6 = hash;
                        int var7 = hash.length;

                        for (int var8 = 0; var8 < var7; ++var8) {
                            byte b = var6[var8];
                            sb.append(String.format("%02x", b & 255));
                        }

                        npc.textureLocation = new ResourceLocation("customnpcs", "skins/" + sb + size);
                        loadSkin(null, npc.textureLocation, npc.display.getSkinUrl(), !size.isEmpty());
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            }
        }

        return npc.textureLocation == null ? DefaultPlayerSkin.getDefaultSkin() : npc.textureLocation;
    }
    private static void loadSkin(File file, ResourceLocation resource, String par1Str, boolean fix64) {
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        Texture object = texturemanager.getTexture(resource);
        if (object == null) {
            object = new ImageDownloadAlt(file, par1Str, DefaultPlayerSkin.getDefaultSkin(), fix64);
            texturemanager.register(resource, object);
        }

    }
}
