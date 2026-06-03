package com.goodbird.cnpcgeckoaddon.api.event;

import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.event.NpcEvent;

public class AnimationKeyframeEvent extends NpcEvent {
    public final IPlayer player;
    public String instruction;

    public AnimationKeyframeEvent(ICustomNpc npc, Player player, String instruction) {
        super(npc);
        this.player = (IPlayer) NpcAPI.Instance().getIEntity(player);
        this.instruction = instruction;
    }
}
