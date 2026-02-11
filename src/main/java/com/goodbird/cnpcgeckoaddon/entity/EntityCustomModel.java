package com.goodbird.cnpcgeckoaddon.entity;

import com.goodbird.cnpcgeckoaddon.CNPCGeckoAddon;
import com.goodbird.cnpcgeckoaddon.mixin.impl.AnimControllerAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EntityCustomModel extends Animal implements GeoAnimatable, GeoEntity {
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    public ResourceLocation modelResLoc=new ResourceLocation(CNPCGeckoAddon.MODID, "geo/geo_npc.geo.json");
    public ResourceLocation animResLoc=new ResourceLocation(CNPCGeckoAddon.MODID , "animations/geo_npc.animation.json");
    public ResourceLocation textureResLoc = new ResourceLocation("customnpcs","textures/entity/humanmale/steve.png");
    public String idleAnim = "";
    public String walkAnim = "";
    public String hurtAnim = "";
    public String attackAnim = "";
    public RawAnimation dialogAnim = null;
    public RawAnimation manualAnim = null;
    public boolean manualAnimInstant = false;
    public ItemStack leftHeldItem;
    public String headBoneName = "head";
    private EntityDimensions dims;
    public int size = 5;
    public EntityNPCInterface owner;

    private PlayState predicateMovement(AnimationState<EntityCustomModel> event) {
        if (manualAnim != null) {
            if (event.getController().getCurrentRawAnimation() == manualAnim && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                manualAnim = null;
            } else {
                if (event.getController().getCurrentRawAnimation() != manualAnim) {
                    event.getController().forceAnimationReset();
                }
                event.getController().setAnimation(manualAnim);
                if(((AnimControllerAccessor)event.getController()).getAnimationState()==AnimationController.State.TRANSITIONING &&
                        !((AnimControllerAccessor) event.getController()).getJustStartedTransition() && manualAnimInstant) {
                    ((AnimControllerAccessor) event.getController()).setAnimationState(AnimationController.State.RUNNING);
                }
                return PlayState.CONTINUE;
            }
        }
        if (dialogAnim != null) {
            if (event.getController().getCurrentRawAnimation() == dialogAnim &&event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                dialogAnim = null;
            } else {
                if (event.getController().getCurrentRawAnimation() != dialogAnim) {
                    event.getController().forceAnimationReset();
                }
                event.getController().setAnimation(dialogAnim);
                return PlayState.CONTINUE;
            }
        }
        if ((event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) || walkAnim.isEmpty()) {
            if (!idleAnim.isEmpty()) {
                event.getController().setAnimation(RawAnimation.begin().thenLoop(idleAnim));
            } else {
                return PlayState.STOP;
            }
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop(walkAnim));
        }
        return PlayState.CONTINUE;
    }

//    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
//        return PlayState.CONTINUE;
//    }

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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 10, this::predicateMovement));
        //controllers.add(new AnimationController<>(this, "attack", 10, this::predicateAttack));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public double getTick(Object entity) {
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

    public double getAttributeValue(Attribute p_233637_1_) {
        try {
            return this.getAttributes().getValue(p_233637_1_);
        }catch (Exception e){
            return 1.0;
        }
    }
}