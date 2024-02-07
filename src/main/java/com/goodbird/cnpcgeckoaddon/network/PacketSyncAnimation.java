package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noppes.npcs.Server;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.builder.RawAnimation;

import java.io.IOException;

public class PacketSyncAnimation implements IMessage, IMessageHandler<PacketSyncAnimation, IMessage> {
    public AnimationBuilder builder;
    public int entityId;
    public PacketSyncAnimation(){

    }
    public PacketSyncAnimation(EntityNPCInterface npc, AnimationBuilder builder) {
        this.builder = builder;
        this.entityId = npc.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            writeAnimBuilder(buf, builder);
        }catch (Exception ignored){ }
        buf.writeInt(entityId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            builder = readAnimBuilder(buf);
        }catch (Exception ignored){ }
        entityId = buf.readInt();
    }

    public static void writeAnimBuilder(ByteBuf buffer, AnimationBuilder builder) throws IOException {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList animList = new NBTTagList();
        for(RawAnimation anim: builder.getRawAnimationList()){
            NBTTagCompound animTag = new NBTTagCompound();
            animTag.setString("name", anim.animationName);
            if(anim.loopType!=null) {
                animTag.setInteger("loop", ((ILoopType.EDefaultLoopTypes) anim.loopType).ordinal());
            }else{
                animTag.setInteger("loop",1);
            }
            animList.appendTag(animTag);
        }
        compound.setTag("anims",animList);
        Server.writeNBT(buffer,compound);
    }

    public static AnimationBuilder readAnimBuilder(ByteBuf buffer) throws IOException {
        AnimationBuilder builder = new AnimationBuilder();
        NBTTagCompound compound = Server.readNBT(buffer);
        NBTTagList animList = compound.getTagList("anims",10);
        for(int i=0;i<animList.tagCount();i++){
            NBTTagCompound animTag = animList.getCompoundTagAt(i);
            builder.addAnimation(animTag.getString("name"),
                    ILoopType.EDefaultLoopTypes.values()[animTag.getInteger("loop")]);
        }
        return builder;
    }

    @Override
    public IMessage onMessage(PacketSyncAnimation message, MessageContext ctx) {
        Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
        if(!(entity instanceof EntityCustomNpc)) return null;
        EntityCustomNpc npc = (EntityCustomNpc) entity;
        if(npc.modelData==null || !(npc.modelData.getEntity(npc) instanceof EntityCustomModel)) return null;
        EntityCustomModel entityCustomModel = (EntityCustomModel) npc.modelData.getEntity(npc);
        entityCustomModel.manualAnim = message.builder;
        return null;
    }
}
