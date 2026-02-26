package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.data.CustomModelData;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import com.goodbird.cnpcgeckoaddon.network.PacketSyncTexture;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataDisplay;
import noppes.npcs.shared.client.util.NoppesStringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataDisplay.class)
public class MixinDataDisplay implements IDataDisplay {

    @Shadow(remap = false)
    EntityNPCInterface npc;
    @Shadow(remap = false)
    private String texture;
    @Shadow(remap = false)
    public byte skinType;
    @Unique
    private final CustomModelData customNPC_Gecko_Addon$customModelData = new CustomModelData();

    @Inject(method = "save", at = @At("HEAD"), remap = false)
    public void writeToNBT(CompoundTag nbttagcompound, CallbackInfoReturnable<CompoundTag> cir) {
        if(hasCustomModel())
            customNPC_Gecko_Addon$customModelData.writeToNBT(nbttagcompound);
    }

    @Inject(method = "readToNBT", at = @At("HEAD"), remap = false)
    public void readFromNBT(CompoundTag nbttagcompound, CallbackInfo ci){
        customNPC_Gecko_Addon$customModelData.readFromNBT(nbttagcompound);
    }

    @Unique
    public CustomModelData getCustomModelData(){
        return customNPC_Gecko_Addon$customModelData;
    }

    @Unique
    public void setSkinTextureSeamless(String texture){
        if(texture == null || this.texture.equals(texture))
            return;
        this.texture = NoppesStringUtils.cleanResource(texture);
        npc.textureLocation = null;
        skinType = 0;
        NetworkWrapper.sendToAll(new PacketSyncTexture(npc.getId(), texture));
    }

    @Unique
    public boolean hasCustomModel() {
        return (npc instanceof EntityCustomNpc) && ((EntityCustomNpc) npc).modelData.getEntity(npc) != null &&
                ((EntityCustomNpc) npc).modelData.getEntity(npc) instanceof EntityCustomModel;
    }
}
