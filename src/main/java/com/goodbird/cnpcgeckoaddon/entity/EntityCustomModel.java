package com.goodbird.cnpcgeckoaddon.entity;

import com.goodbird.cnpcgeckoaddon.mixin.IAnimationController;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class EntityCustomModel extends Animal implements IAnimatable, IAnimationTickable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public ResourceLocation modelResLoc=new ResourceLocation("geckolib3", "geo/bike.geo.json");
    public ResourceLocation animResLoc=new ResourceLocation("geckolib3", "bike.animation.json");
    public ResourceLocation textureResLoc = new ResourceLocation("geckolib3", "textures/model/entity/bike.png");
    public String idleAnim = "";
    public String walkAnim = "";
    public String hurtAnim = "";
    public String attackAnim = "";
    public AnimationBuilder dialogAnim = null;
    public AnimationBuilder manualAnim = null;
    public ItemStack leftHeldItem;
    public String headBoneName = "head";
    private EntityDimensions dims;

    private <E extends IAnimatable> PlayState predicateMovement(AnimationEvent<E> event) {
        if (manualAnim != null) {
            if (((IAnimationController)event.getController()).getCurrentAnimationBuilder() == manualAnim && event.getController().getAnimationState() == AnimationState.Stopped) {
                manualAnim = null;
            } else {
                if (((IAnimationController)event.getController()).getCurrentAnimationBuilder() != manualAnim) {
                    event.getController().markNeedsReload();
                }
                event.getController().setAnimation(manualAnim);
                return PlayState.CONTINUE;
            }
        }
        if (dialogAnim != null) {
            if (((IAnimationController)event.getController()).getCurrentAnimationBuilder() == dialogAnim &&event.getController().getAnimationState() == AnimationState.Stopped) {
                dialogAnim = null;
            } else {
                if (((IAnimationController)event.getController()).getCurrentAnimationBuilder() != dialogAnim) {
                    event.getController().markNeedsReload();
                }
                event.getController().setAnimation(dialogAnim);
                return PlayState.CONTINUE;
            }
        }
        if (!event.isMoving() || walkAnim.isEmpty()) {
            if (!idleAnim.isEmpty()) {
                event.getController().setAnimation(new AnimationBuilder().loop(idleAnim));
            } else {
                return PlayState.STOP;
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop(walkAnim));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    public EntityCustomModel(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
        this.noCulling = true;
    }

    public void setSize(float width, float height) {
        dims = EntityDimensions.scalable(width, height);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_213305_1_) {
        if(dims==null){
            dims = EntityDimensions.scalable(0.7F, 2F);
        }
        return dims;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "movement", 10, this::predicateMovement));
        data.addAnimationController(new AnimationController<>(this, "attack", 10, this::predicateAttack));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }
}