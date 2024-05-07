package com.goodbird.cnpcgeckoaddon.data;

import net.minecraft.nbt.CompoundNBT;

public class CustomModelData {
    private String model = "geckolib3:geo/bike.geo.json";
    private String animFile = "geckolib3:animations/bike.animation.json";
    private String idleAnim = "animation.bike.idle";
    private String walkAnim = "";
    private String attackAnim = "";
    private String hurtAnim = "";
    private String headBoneName = "head";
    private int transitionLengthTicks = 10;

    public CompoundNBT writeToNBT(CompoundNBT nbttagcompound) {
        nbttagcompound.putString("Model", model);
        nbttagcompound.putString("AnimFile", animFile);
        nbttagcompound.putString("IdleAnim", idleAnim);
        nbttagcompound.putString("WalkAnim", walkAnim);
        nbttagcompound.putString("AttackAnim", attackAnim);
        nbttagcompound.putString("HurtAnim", hurtAnim);
        nbttagcompound.putString("HeadBoneName", headBoneName);
        nbttagcompound.putInt("TransitionLengthTicks", transitionLengthTicks);

        return nbttagcompound;
    }

    public void readFromNBT(CompoundNBT nbttagcompound) {
        if(nbttagcompound.contains("Model")) {
            model = nbttagcompound.getString("Model");
            animFile = nbttagcompound.getString("AnimFile");
            idleAnim = nbttagcompound.getString("IdleAnim");
            walkAnim = nbttagcompound.getString("WalkAnim");
            hurtAnim = nbttagcompound.getString("HurtAnim");
            attackAnim = nbttagcompound.getString("AttackAnim");
            headBoneName = nbttagcompound.getString("HeadBoneName");
            if(nbttagcompound.contains("TransitionLengthTicks")) {
                transitionLengthTicks = nbttagcompound.getInt("TransitionLengthTicks");
            }
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAnimFile() {
        return animFile;
    }

    public void setAnimFile(String animFile) {
        this.animFile = animFile;
    }

    public String getIdleAnim() {
        return idleAnim;
    }

    public void setIdleAnim(String idleAnim) {
        this.idleAnim = idleAnim;
    }

    public String getWalkAnim() {
        return walkAnim;
    }

    public void setWalkAnim(String walkAnim) {
        this.walkAnim = walkAnim;
    }

    public String getAttackAnim() {
        return attackAnim;
    }

    public void setAttackAnim(String attackAnim) {
        this.attackAnim = attackAnim;
    }

    public String getHurtAnim() {
        return hurtAnim;
    }

    public void setHurtAnim(String hurtAnim) {
        this.hurtAnim = hurtAnim;
    }

    public String getHeadBoneName() {
        return headBoneName;
    }

    public void setHeadBoneName(String headBoneName) {
        this.headBoneName = headBoneName;
    }

    public int getTransitionLengthTicks() {
        return transitionLengthTicks;
    }

    public void setTransitionLengthTicks(int transitionLengthTicks) {
        this.transitionLengthTicks = transitionLengthTicks;
    }
}
