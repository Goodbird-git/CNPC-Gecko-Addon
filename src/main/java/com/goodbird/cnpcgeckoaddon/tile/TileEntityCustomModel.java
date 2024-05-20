package com.goodbird.cnpcgeckoaddon.tile;

import com.goodbird.cnpcgeckoaddon.mixin.IAnimationController;
import com.goodbird.cnpcgeckoaddon.registry.TileEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.example.registry.TileRegistry;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class TileEntityCustomModel extends BlockEntity implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);;
    public ResourceLocation modelResLoc = new ResourceLocation(GeckoLib.ModID, "geo/botarium.geo.json");
    public ResourceLocation animResLoc = new ResourceLocation(GeckoLib.ModID, "animations/botarium.animation.json");
    public ResourceLocation textureResLoc = new ResourceLocation(GeckoLib.ModID, "textures/block/botarium.png");
    public String idleAnimName = "";
    public AnimationBuilder manualAnim = null;

    public TileEntityCustomModel(BlockPos pos, BlockState state) {
        super(TileEntityRegistry.tileEntityCustomModel, pos, state);
    }

    public TileEntityCustomModel(BlockEntity other){
        super(TileEntityRegistry.tileEntityCustomModel, other.getBlockPos(), other.getBlockState());
        setLevel(other.getLevel());
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (manualAnim != null) {
            if (event.getController().getAnimationState() == AnimationState.Stopped) {
                manualAnim = null;
            } else {
                if (((IAnimationController)event.getController()).getCurrentAnimationBuilder() != manualAnim) {
                    event.getController().markNeedsReload();
                }
                event.getController().setAnimation(manualAnim);
                return PlayState.CONTINUE;
            }
        }
        if (!idleAnimName.isEmpty()) {
            event.getController().setAnimation(new AnimationBuilder().loop(idleAnimName));
        } else {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putString("modelResLoc", modelResLoc.toString());
        compound.putString("animResLoc", animResLoc.toString());
        compound.putString("textureResLoc", textureResLoc.toString());
        compound.putString("idleAnimName", idleAnimName);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        modelResLoc = new ResourceLocation(compound.getString("modelResLoc"));
        animResLoc = new ResourceLocation(compound.getString("animResLoc"));
        textureResLoc = new ResourceLocation(compound.getString("textureResLoc"));
        idleAnimName = compound.getString("idleAnimName");
    }
}
