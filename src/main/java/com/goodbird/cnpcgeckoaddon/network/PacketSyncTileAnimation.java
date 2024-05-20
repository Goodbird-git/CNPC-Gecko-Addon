package com.goodbird.cnpcgeckoaddon.network;

import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
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

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);

        CompoundTag compound = new CompoundTag();
        ListTag animList = new ListTag();
        for(RawAnimation anim: builder.getRawAnimationList()){
            CompoundTag animTag = new CompoundTag();
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

    public static PacketSyncTileAnimation decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        AnimationBuilder builder = new AnimationBuilder();
        CompoundTag compound = buf.readNbt();
        ListTag animList = compound.getList("anims",10);
        for (Tag inbt : animList) {
            CompoundTag animTag = (CompoundTag) inbt;
            builder.addAnimation(animTag.getString("name"),
                    ILoopType.EDefaultLoopTypes.values()[animTag.getInt("loop")]);
        }
        return new PacketSyncTileAnimation(pos,builder);
    }

    public static void handle(PacketSyncTileAnimation packet, Supplier<NetworkEvent.Context> ctx) {
        BlockEntity entity = Minecraft.getInstance().player.getCommandSenderWorld().getBlockEntity(packet.pos);
        if(!(entity instanceof TileScripted)) return;
        TileScripted tile = (TileScripted) entity;
        if(tile.renderTile==null){
            tile.renderTile = new TileEntityCustomModel(tile);
        }
        TileEntityCustomModel geckoTile = (TileEntityCustomModel) tile.renderTile;
        geckoTile.manualAnim = packet.builder;
    }
}

