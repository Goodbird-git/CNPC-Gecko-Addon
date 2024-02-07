package com.goodbird.cnpcgeckoaddon.utils;


import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ImageDownloadAlt;
import noppes.npcs.client.renderer.ImageBufferDownloadAlt;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.entity.EntityNPCInterface;

import java.io.File;
import java.security.MessageDigest;
import java.util.Map;

public class NpcTextureUtils {
    public static ResourceLocation getNpcTexture(EntityNPCInterface npc){
        if (npc.textureLocation == null) {
            if (npc.display.skinType == 0) {
                npc.textureLocation = new ResourceLocation(npc.display.getSkinTexture());
            } else {
                if (RenderNPCInterface.LastTextureTick < 5) {
                    return DefaultPlayerSkin.getDefaultSkinLegacy();
                }

                if (npc.display.skinType == 1 && npc.display.playerProfile != null) {
                    Minecraft minecraft = Minecraft.getMinecraft();
                    Map map = minecraft.getSkinManager().loadSkinFromCache(npc.display.playerProfile);
                    if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                        npc.textureLocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                    }
                } else if (npc.display.skinType == 2) {
                    try {
                        MessageDigest digest = MessageDigest.getInstance("MD5");
                        byte[] hash = digest.digest(npc.display.getSkinUrl().getBytes("UTF-8"));
                        StringBuilder sb = new StringBuilder(2 * hash.length);
                        byte[] var5 = hash;
                        int var6 = hash.length;

                        for(int var7 = 0; var7 < var6; ++var7) {
                            byte b = var5[var7];
                            sb.append(String.format("%02x", b & 255));
                        }

                        npc.textureLocation = new ResourceLocation("skins/" + sb.toString());
                        loadSkin(null, npc.textureLocation, npc.display.getSkinUrl());
                    } catch (Exception var9) {
                    }
                }
            }
        }

        return npc.textureLocation == null ? DefaultPlayerSkin.getDefaultSkinLegacy() : npc.textureLocation;
    }

    private static void loadSkin(File file, ResourceLocation resource, String par1Str) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        if (texturemanager.getTexture(resource) == null) {
            ITextureObject object = new ImageDownloadAlt(file, par1Str, DefaultPlayerSkin.getDefaultSkinLegacy(), new ImageBufferDownloadAlt());
            texturemanager.loadTexture(resource, object);
        }
    }
}
