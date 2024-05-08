package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import noppes.npcs.blocks.tiles.TileScripted;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileScripted.class)
public abstract class TileScriptedMixin extends TileEntity {

    @Shadow(remap = false)
    public TileEntity renderTile;

    public TileScriptedMixin() {
        super(null);
    }

    @Inject(method = "setDisplayNBT", at = @At("TAIL"), remap = false)
    public void setDisplayNBT(CompoundNBT compound, CallbackInfo ci) {
        if(compound.contains("renderTileTag")){
            renderTile = new TileEntityCustomModel();
            CompoundNBT saveTag = compound.getCompound("renderTileTag");
            renderTile.setLevelAndPosition(getLevel(), getBlockPos());
            renderTile.load(null, saveTag);
        }
    }

    @Inject(method = "getDisplayNBT", at = @At("TAIL"), remap = false)
    public void getDisplayNBT(CompoundNBT compound, CallbackInfoReturnable<CompoundNBT> cir) {
        if(renderTile!=null) {
            CompoundNBT saveTag = new CompoundNBT();
            renderTile.save(saveTag);
            compound.put("renderTileTag", saveTag);
        }
    }
}