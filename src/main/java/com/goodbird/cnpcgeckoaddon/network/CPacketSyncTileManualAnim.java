package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noppes.npcs.Server;
import noppes.npcs.blocks.tiles.TileScripted;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.builder.RawAnimation;

import java.io.IOException;

public class CPacketSyncTileManualAnim implements IMessage, IMessageHandler<CPacketSyncTileManualAnim, IMessage> {
    public AnimationBuilder builder;
    public BlockPos pos;
    public CPacketSyncTileManualAnim(){

    }
    public CPacketSyncTileManualAnim(TileEntity tile, AnimationBuilder builder) {
        this.builder = builder;
        this.pos = tile.getPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            writeAnimBuilder(buf, builder);
        }catch (Exception ignored){ }
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            builder = readAnimBuilder(buf);
        }catch (Exception ignored){ }
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
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
    public IMessage onMessage(CPacketSyncTileManualAnim message, MessageContext ctx) {
        TileEntity entity = Minecraft.getMinecraft().world.getTileEntity(message.pos);
        if(!(entity instanceof TileScripted)) return null;
        TileScripted tile = (TileScripted) entity;
        if(tile.renderTile==null){
            tile.renderTile = new TileEntityCustomModel();
        }
        TileEntityCustomModel geckoTile = (TileEntityCustomModel) tile.renderTile;
        geckoTile.manualAnim = message.builder;
        return null;
    }
}
