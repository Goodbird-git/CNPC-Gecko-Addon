package com.goodbird.cnpcgeckoaddon.tile;

import com.goodbird.cnpcgeckoaddon.registry.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class TileEntityCustomModel extends TileEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    public ResourceLocation modelResLoc = new ResourceLocation(GeckoLib.ModID, "geo/botarium.geo.json");
    public ResourceLocation animResLoc = new ResourceLocation(GeckoLib.ModID, "animations/botarium.animation.json");
    public ResourceLocation textureResLoc = new ResourceLocation(GeckoLib.ModID, "textures/block/botarium.png");
    public String idleAnimName = "";
    public AnimationBuilder manualAnim = null;

    public TileEntityCustomModel(){
        super(TileEntityRegistry.tileEntityCustomModel);
    }
    public TileEntityCustomModel(TileEntity other){
        super(TileEntityRegistry.tileEntityCustomModel);
        setLevelAndPosition(other.getLevel(), other.getBlockPos());
    }

    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (manualAnim != null) {
            if (event.getController().getAnimationState() == AnimationState.Stopped) {
                manualAnim = null;
            } else {
                if (event.getController().currentAnimationBuilder != manualAnim) {
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
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putString("modelResLoc", modelResLoc.toString());
        compound.putString("animResLoc", animResLoc.toString());
        compound.putString("textureResLoc", textureResLoc.toString());
        compound.putString("idleAnimName", idleAnimName);
        return compound;
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT compound) {
        super.load(p_230337_1_, compound);
        modelResLoc = new ResourceLocation(compound.getString("modelResLoc"));
        animResLoc = new ResourceLocation(compound.getString("animResLoc"));
        textureResLoc = new ResourceLocation(compound.getString("textureResLoc"));
        idleAnimName = compound.getString("idleAnimName");
    }
}
