package com.goodbird.cnpcgeckoaddon.tile;

import net.minecraft.nbt.NBTTagCompound;
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

    }
    public TileEntityCustomModel(TileEntity other){
        setWorld(other.getWorld());
        setPos(other.getPos());
    }

    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (manualAnim != null) {
            if (event.getController().currentAnimationBuilder == manualAnim && event.getController().getAnimationState() == AnimationState.Stopped) {
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
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("modelResLoc", modelResLoc.toString());
        compound.setString("animResLoc", animResLoc.toString());
        compound.setString("textureResLoc", textureResLoc.toString());
        compound.setString("idleAnimName", idleAnimName);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        modelResLoc = new ResourceLocation(compound.getString("modelResLoc"));
        animResLoc = new ResourceLocation(compound.getString("animResLoc"));
        textureResLoc = new ResourceLocation(compound.getString("textureResLoc"));
        idleAnimName = compound.getString("idleAnimName");
    }
}
