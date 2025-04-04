package com.goodbird.cnpcgeckoaddon.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.util.ImageDownloadAlt;
import noppes.npcs.shared.client.util.ResourceDownloader;

import java.io.File;

import static noppes.npcs.client.renderer.RenderNPCInterface.LastTextureTick;

public class NpcTextureUtils {
    public static ResourceLocation getNpcTexture(EntityNPCInterface npc) {
        if(npc.textureLocation == null){
            if(npc.display.skinType == 0)// normal skin
                npc.textureLocation = ResourceLocation.tryParse(npc.display.getSkinTexture());
            else if(LastTextureTick < 5){ //fixes request flood somewhat
                return DefaultPlayerSkin.getDefaultTexture();
            }
            else if(npc.display.skinType == 1 && npc.display.playerProfile != null){ //player skin
                Minecraft minecraft = Minecraft.getInstance();
                PlayerSkin skin = minecraft.getSkinManager().getInsecureSkin(npc.display.playerProfile);
                npc.textureLocation = skin.texture();
            }
            else if(npc.display.skinType == 2 && !npc.display.getSkinUrl().isEmpty()){ // url skin
                try{
                    boolean fixSkin = npc instanceof EntityCustomNpc && ((EntityCustomNpc)npc).modelData.getEntity(npc) == null;
                    File file = ResourceDownloader.getUrlFile(npc.display.getSkinUrl(), fixSkin);
                    npc.textureLocation = ResourceDownloader.getUrlResourceLocation(npc.display.getSkinUrl(), fixSkin);
                    loadSkin(file, npc.textureLocation, npc.display.getSkinUrl(), fixSkin);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        if(npc.textureLocation == null)
            return DefaultPlayerSkin.getDefaultTexture();
        return npc.textureLocation;
    }
    private static void loadSkin(File file, ResourceLocation resource, String par1Str, boolean fix64) {
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        AbstractTexture object = texturemanager.getTexture(resource);
        if (object == null) {
            object = new ImageDownloadAlt(file, par1Str, resource, DefaultPlayerSkin.getDefaultTexture(), fix64, () -> {});
            texturemanager.register(resource, object);
        }

    }
}
