package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.data.CustomModelData;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import com.goodbird.cnpcgeckoaddon.network.PacketSyncAnimation;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.EntityLivingWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

@Mixin(NPCWrapper.class)
public class MixinNpcWrapper<T extends EntityNPCInterface> extends EntityLivingWrapper<T> {
    public MixinNpcWrapper() {
        super(null);
    }

    @Unique
    public CustomModelData getModelData(){
        return ((IDataDisplay)getMCEntity().display).getCustomModelData();
    }

    @Unique
    public void setGeckoModel(String model) {
        getModelData().setModel(model);
        getMCEntity().updateClient();
    }

    @Unique
    public void setGeckoTexture(String texture) {
        getMCEntity().display.setSkinTexture(texture);
        getMCEntity().updateClient();
    }

    @Unique
    public void setGeckoAnimationFile(String animation) {
        getModelData().setAnimFile(animation);
        getMCEntity().updateClient();
    }

    @Unique
    public void setGeckoIdleAnimation(String animation) {
        getModelData().setIdleAnim(animation);
        getMCEntity().updateClient();
    }

    @Unique
    public void setGeckoWalkAnimation(String animation) {
        getModelData().setWalkAnim(animation);
        getMCEntity().updateClient();
    }

    @Unique
    public void syncAnimationsFor(IPlayer player, AnimationBuilder builder) {
        NetworkWrapper.sendToPlayer(new PacketSyncAnimation(entity.getId(),builder), player.getMCEntity());
    }
    @Unique
    public void syncAnimationsForAll(AnimationBuilder builder) {
        NetworkWrapper.sendToAll(new PacketSyncAnimation(entity.getId(),builder));
    }
}
