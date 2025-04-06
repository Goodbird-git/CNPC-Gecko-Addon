package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import com.goodbird.cnpcgeckoaddon.network.PacketSyncTileAnimation;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.BlockWrapper;
import noppes.npcs.blocks.tiles.TileScripted;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animation.RawAnimation;

@Mixin(BlockScriptedWrapper.class)
public abstract class MixinBlockScriptedWrapper extends BlockWrapper {


    protected MixinBlockScriptedWrapper(Level level, Block block, BlockPos pos) {
        super(level, block, pos);
    }

    @Unique
    public TileEntityCustomModel getOrCreateTECM(){
        TileScripted tile = (TileScripted) getMCTileEntity();
        if(!(tile.renderTile instanceof TileEntityCustomModel)){
            tile.renderTile = new TileEntityCustomModel(tile);
        }
        return (TileEntityCustomModel) tile.renderTile;
    }

    @Unique
    public void setGeckoModel(String model) {
        TileEntityCustomModel geckoTile = getOrCreateTECM();
        geckoTile.modelResLoc = ResourceLocation.parse(model);
        ((TileScripted) getMCTileEntity()).needsClientUpdate = true;
    }

    @Unique
    public void setGeckoTexture(String texture) {
        TileEntityCustomModel geckoTile = getOrCreateTECM();
        geckoTile.textureResLoc = ResourceLocation.parse(texture);
        ((TileScripted) getMCTileEntity()).needsClientUpdate = true;
    }

    @Unique
    public void setGeckoAnimationFile(String animation) {
        TileEntityCustomModel geckoTile = getOrCreateTECM();
        geckoTile.animResLoc = ResourceLocation.parse(animation);
        ((TileScripted) getMCTileEntity()).needsClientUpdate = true;
    }

    @Unique
    public void setGeckoIdleAnimation(String animation) {
        TileEntityCustomModel geckoTile = getOrCreateTECM();
        geckoTile.idleAnimName = animation;
        ((TileScripted) getMCTileEntity()).needsClientUpdate = true;
    }

    @Unique
    public void syncAnimForPlayer(RawAnimation builder, IPlayer<ServerPlayer> player) {
        NetworkWrapper.send(player.getMCEntity(), new PacketSyncTileAnimation(getMCTileEntity().getBlockPos(), builder));
    }

    @Unique
    public void syncAnimForAll(RawAnimation builder) {
        NetworkWrapper.sendAll(new PacketSyncTileAnimation(getMCTileEntity().getBlockPos(), builder));
    }
}