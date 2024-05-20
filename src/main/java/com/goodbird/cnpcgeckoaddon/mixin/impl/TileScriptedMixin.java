package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import noppes.npcs.blocks.tiles.TileScripted;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileScripted.class)
public abstract class TileScriptedMixin extends BlockEntity {

    @Shadow(remap = false)
    public BlockEntity renderTile;

    @Shadow public abstract void saveAdditional(CompoundTag compound);

    public TileScriptedMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }


    @Inject(method = "setDisplayNBT", at = @At("TAIL"), remap = false)
    public void setDisplayNBT(CompoundTag compound, CallbackInfo ci) {
        if(compound.contains("renderTileTag")){
            renderTile = new TileEntityCustomModel(this);
            CompoundTag saveTag = compound.getCompound("renderTileTag");
            renderTile.setLevel(getLevel());
            renderTile.load(saveTag);
        }
    }

    @Inject(method = "getDisplayNBT", at = @At("TAIL"), remap = false)
    public void getDisplayNBT(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        if(renderTile!=null && renderTile instanceof TileEntityCustomModel) {
            CompoundTag saveTag = new CompoundTag();
            ((TileEntityCustomModel)renderTile).saveAdditional(saveTag);
            compound.put("renderTileTag", saveTag);
        }
    }
}