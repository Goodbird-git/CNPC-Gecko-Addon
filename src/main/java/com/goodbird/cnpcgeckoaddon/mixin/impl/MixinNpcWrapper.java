package com.goodbird.cnpcgeckoaddon.mixin.impl;

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
    public void syncAnimationsFor(IPlayer player, AnimationBuilder builder) {
        NetworkWrapper.sendToPlayer(new PacketSyncAnimation(entity.getId(),builder), player.getMCEntity());
    }
    @Unique
    public void syncAnimationsForAll(AnimationBuilder builder) {
        NetworkWrapper.sendToAll(new PacketSyncAnimation(entity.getId(),builder));
    }
}
