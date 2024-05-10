package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import com.goodbird.cnpcgeckoaddon.network.PacketSyncTileAnimation;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.BlockWrapper;
import noppes.npcs.blocks.tiles.TileScripted;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

@Mixin(BlockScriptedWrapper.class)
public abstract class MixinBlockScriptedWrapper extends BlockWrapper {

    protected MixinBlockScriptedWrapper(World level, Block block, BlockPos pos) {
        super(level, block, pos);
    }

    @Unique
    public TileEntityCustomModel getOrCreateTECM(){
        TileScripted tile = (TileScripted) getMCTileEntity();
        if(!(tile.renderTile instanceof TileEntityCustomModel)){
            tile.renderTile = new TileEntityCustomModel();
        }
        return (TileEntityCustomModel) tile.renderTile;
    }

    @Unique
    public void setGeckoModel(String model) {
        TileEntityCustomModel geckoTile = getOrCreateTECM();
        geckoTile.modelResLoc = new ResourceLocation(model);
        ((TileScripted) getMCTileEntity()).needsClientUpdate = true;
    }

    @Unique
    public void setGeckoTexture(String texture) {
        TileEntityCustomModel geckoTile = getOrCreateTECM();
        geckoTile.textureResLoc = new ResourceLocation(texture);
        ((TileScripted) getMCTileEntity()).needsClientUpdate = true;
    }

    @Unique
    public void setGeckoAnimationFile(String animation) {
        TileEntityCustomModel geckoTile = getOrCreateTECM();
        geckoTile.animResLoc = new ResourceLocation(animation);
        ((TileScripted) getMCTileEntity()).needsClientUpdate = true;
    }

    @Unique
    public void setGeckoIdleAnimation(String animation) {
        TileEntityCustomModel geckoTile = getOrCreateTECM();
        geckoTile.idleAnimName = animation;
        ((TileScripted) getMCTileEntity()).needsClientUpdate = true;
    }

    @Unique
    public void syncAnimForPlayer(AnimationBuilder builder, IPlayer<ServerPlayerEntity> player) {
        NetworkWrapper.sendToPlayer(new PacketSyncTileAnimation(getMCTileEntity().getBlockPos(), builder), player.getMCEntity());
    }

    @Unique
    public void syncAnimForAll(AnimationBuilder builder) {
        NetworkWrapper.sendToAll(new PacketSyncTileAnimation(getMCTileEntity().getBlockPos(), builder));
    }
}