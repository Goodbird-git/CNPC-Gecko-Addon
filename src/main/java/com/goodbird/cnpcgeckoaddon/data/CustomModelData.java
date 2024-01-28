package com.goodbird.cnpcgeckoaddon.data;

import net.minecraft.nbt.CompoundNBT;

public class CustomModelData {
    private String model = "geckolib3:geo/bike.geo.json";
    private String animFile = "geckolib3:animations/bike.animation.json";
    private String idleAnim = "animation.bike.idle";
    private String walkAnim = "";
    private String attackAnim = "";
    private String hurtAnim = "";

    public CompoundNBT writeToNBT(CompoundNBT CompoundNBT) {
        CompoundNBT.putString("Model", model);
        CompoundNBT.putString("AnimFile", animFile);
        CompoundNBT.putString("IdleAnim", idleAnim);
        CompoundNBT.putString("WalkAnim", walkAnim);
        CompoundNBT.putString("AttackAnim", attackAnim);
        CompoundNBT.putString("HurtAnim", hurtAnim);
        return CompoundNBT;
    }

    public void readFromNBT(CompoundNBT CompoundNBT) {
        if(CompoundNBT.contains("Model")) {
            model = CompoundNBT.getString("Model");
            animFile = CompoundNBT.getString("AnimFile");
            idleAnim = CompoundNBT.getString("IdleAnim");
            walkAnim = CompoundNBT.getString("WalkAnim");
            hurtAnim = CompoundNBT.getString("HurtAnim");
            attackAnim = CompoundNBT.getString("AttackAnim");
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
}
