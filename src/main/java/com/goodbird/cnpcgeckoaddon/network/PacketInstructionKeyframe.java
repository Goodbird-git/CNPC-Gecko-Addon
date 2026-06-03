package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.api.event.AnimationKeyframeEvent;
import com.goodbird.cnpcgeckoaddon.constants.GAEnumScriptType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.entity.EntityCustomNpc;

import java.util.function.Supplier;

public class PacketInstructionKeyframe {
    private int id;
    private String instruction;

    public PacketInstructionKeyframe(int entityId, String instruction) {
        this.id = entityId;
        this.instruction = instruction;
    }

    public PacketInstructionKeyframe(){

    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeUtf(instruction);
    }

    public static PacketInstructionKeyframe decode(FriendlyByteBuf buf) {
        return new PacketInstructionKeyframe(buf.readInt(), buf.readUtf());
    }

    public static void handle(PacketInstructionKeyframe packet, Supplier<NetworkEvent.Context> ctx) {
        Entity entity = ctx.get().getSender().level().getEntity(packet.id);
        if(!(entity instanceof EntityCustomNpc npc)) return;
        //((ServerChunkCache)ctx.get().getSender().level().getChunkSource()).chunkMap TODO CHECK;

        if("attack;".equals(packet.instruction)){
            if(npc.getTarget()!=null) {
                npc.doHurtTarget(npc.getTarget());
            }
        }

        AnimationKeyframeEvent event = new AnimationKeyframeEvent((ICustomNpc) NpcAPI.Instance().getIEntity(npc), ctx.get().getSender(), packet.instruction);
        npc.script.runScript(GAEnumScriptType.ANIMATION_INSTRUCTION, event);
    }
}
