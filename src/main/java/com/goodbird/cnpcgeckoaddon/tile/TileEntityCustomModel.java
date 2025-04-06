package com.goodbird.cnpcgeckoaddon.tile;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.registry.TileEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TileEntityCustomModel extends BlockEntity implements GeoAnimatable, GeoBlockEntity {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    public ResourceLocation modelResLoc = ResourceLocation.fromNamespaceAndPath(GeckoLibConstants.MODID, "geo/block/botarium.geo.json");
    public ResourceLocation animResLoc = ResourceLocation.fromNamespaceAndPath(GeckoLibConstants.MODID, "animations/block/botarium.animation.json");
    public ResourceLocation textureResLoc = ResourceLocation.fromNamespaceAndPath(GeckoLibConstants.MODID, "textures/block/botarium.png");
    public String idleAnimName = "";
    public RawAnimation manualAnim = null;

    public TileEntityCustomModel(BlockPos pos, BlockState state) {
        super(TileEntityRegistry.tileEntityCustomModel, pos, state);
    }

    public TileEntityCustomModel(BlockEntity other){
        super(TileEntityRegistry.tileEntityCustomModel, other.getBlockPos(), other.getBlockState());
        setLevel(other.getLevel());
    }

    private PlayState predicate(AnimationState<TileEntityCustomModel> event) {
        if (manualAnim != null) {
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                manualAnim = null;
            } else {
                if (event.getController().getCurrentRawAnimation() != manualAnim) {
                    event.getController().forceAnimationReset();
                }
                event.getController().setAnimation(manualAnim);
                return PlayState.CONTINUE;
            }
        }
        if (!idleAnimName.isEmpty()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop(idleAnimName));
        } else {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putString("modelResLoc", modelResLoc.toString());
        compound.putString("animResLoc", animResLoc.toString());
        compound.putString("textureResLoc", textureResLoc.toString());
        compound.putString("idleAnimName", idleAnimName);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        modelResLoc = ResourceLocation.parse(compound.getString("modelResLoc"));
        animResLoc = ResourceLocation.parse(compound.getString("animResLoc"));
        textureResLoc = ResourceLocation.parse(compound.getString("textureResLoc"));
        idleAnimName = compound.getString("idleAnimName");
    }
}
