package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import noppes.npcs.blocks.tiles.TileScripted;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.builder.RawAnimation;

import java.util.function.Supplier;

public class PacketSyncTileAnimation {
    private BlockPos pos;
    private AnimationBuilder builder;

    public PacketSyncTileAnimation(BlockPos pos, AnimationBuilder builder) {
        this.pos = pos;
        this.builder = builder;
    }

    public PacketSyncTileAnimation(){

    }

    public void encode(PacketBuffer buf) {
        buf.writeBlockPos(pos);

        CompoundNBT compound = new CompoundNBT();
        ListNBT animList = new ListNBT();
        for(RawAnimation anim: builder.getRawAnimationList()){
            CompoundNBT animTag = new CompoundNBT();
            animTag.putString("name", anim.animationName);
            if(anim.loopType!=null) {
                animTag.putInt("loop", ((ILoopType.EDefaultLoopTypes) anim.loopType).ordinal());
            }else{
                animTag.putInt("loop",1);
            }
            animList.add(animTag);
        }
        compound.put("anims",animList);
        buf.writeNbt(compound);
    }

    public static PacketSyncTileAnimation decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        AnimationBuilder builder = new AnimationBuilder();
        CompoundNBT compound = buf.readNbt();
        ListNBT animList = compound.getList("anims",10);
        for (net.minecraft.nbt.INBT inbt : animList) {
            CompoundNBT animTag = (CompoundNBT) inbt;
            builder.addAnimation(animTag.getString("name"),
                    ILoopType.EDefaultLoopTypes.values()[animTag.getInt("loop")]);
        }
        return new PacketSyncTileAnimation(pos,builder);
    }

    public static void handle(PacketSyncTileAnimation packet, Supplier<NetworkEvent.Context> ctx) {
        TileEntity entity = Minecraft.getInstance().player.getCommandSenderWorld().getBlockEntity(packet.pos);
        if(!(entity instanceof TileScripted)) return;
        TileScripted tile = (TileScripted) entity;
        if(tile.renderTile==null){
            tile.renderTile = new TileEntityCustomModel();
        }
        TileEntityCustomModel geckoTile = (TileEntityCustomModel) tile.renderTile;
        geckoTile.manualAnim = packet.builder;
    }
}

