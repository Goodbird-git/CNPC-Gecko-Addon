package com.goodbird.cnpcgeckoaddon.data;

import net.minecraft.nbt.NBTTagCompound;

public class CustomModelData implements ICustomModelData {
    private String model = "geckolib3:geo/bike.geo.json";
    private String animFile = "geckolib3:animations/bike.animation.json";
    private String idleAnim = "animation.bike.idle";
    private String walkAnim = "";
    private String meleeAttackAnim = "";
    private String hurtAnim = "";
    private String rangedAttackAnim = "";
    private String headBoneName = "head";
    private int transitionLengthTicks = 10;

    private float width = 0.7f;

    private float height = 2f;

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Model", model);
        nbttagcompound.setString("AnimFile", animFile);
        nbttagcompound.setString("IdleAnim", idleAnim);
        nbttagcompound.setString("WalkAnim", walkAnim);
        nbttagcompound.setString("MeleeAttackAnim", meleeAttackAnim);
        nbttagcompound.setString("RangedAttackAnim", rangedAttackAnim);
        nbttagcompound.setString("HurtAnim", hurtAnim);
        nbttagcompound.setString("HeadBoneName", headBoneName);
        nbttagcompound.setInteger("TransitionLengthTicks", transitionLengthTicks);
        nbttagcompound.setFloat("Width",width);
        nbttagcompound.setFloat("Height",height);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        if(nbttagcompound.hasKey("Model")) {
            model = nbttagcompound.getString("Model");
            animFile = nbttagcompound.getString("AnimFile");
            idleAnim = nbttagcompound.getString("IdleAnim");
            walkAnim = nbttagcompound.getString("WalkAnim");
            hurtAnim = nbttagcompound.getString("HurtAnim");
            meleeAttackAnim = nbttagcompound.getString("MeleeAttackAnim");
            rangedAttackAnim = nbttagcompound.getString("RangedAttackAnim");

            if(nbttagcompound.hasKey("HeadBoneName"))
                headBoneName = nbttagcompound.getString("HeadBoneName");

            if(nbttagcompound.hasKey("Width"))
                width = nbttagcompound.getFloat("Width");

            if(nbttagcompound.hasKey("Height"))
                height = nbttagcompound.getFloat("Height");

            if(nbttagcompound.hasKey("TransitionLengthTicks"))
                transitionLengthTicks = nbttagcompound.getInteger("TransitionLengthTicks");
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

    public String getMeleeAttackAnim() {
        return meleeAttackAnim;
    }

    public void setMeleeAttackAnim(String meleeAttackAnim) {
        this.meleeAttackAnim = meleeAttackAnim;
    }

    public String getHurtAnim() {
        return hurtAnim;
    }

    public void setHurtAnim(String hurtAnim) {
        this.hurtAnim = hurtAnim;
    }

    public String getRangedAttackAnim() {
        return rangedAttackAnim;
    }

    public void setRangedAttackAnim(String rangedAttackAnim) {
        this.rangedAttackAnim = rangedAttackAnim;
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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
