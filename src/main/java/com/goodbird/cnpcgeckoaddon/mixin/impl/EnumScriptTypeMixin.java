package com.goodbird.cnpcgeckoaddon.mixin.impl;

import noppes.npcs.constants.EnumScriptType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnumScriptType.class)
public class EnumScriptTypeMixin {
    @Shadow(remap=false)
    @Final
    @Mutable
    private static EnumScriptType[] $VALUES;

    private static final EnumScriptType ANIMATION_INSTRUCTION_EVENT = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("ANIMATION_INSTRUCTION", "animationInstruction");

    @Invoker("<init>")
    private static EnumScriptType cnpcCobblemonAddon$invokeInit(String internalName, int internalId, String name) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at=@At("TAIL"))
    private static void onClInit(CallbackInfo ci){
        EnumScriptType.playerScripts = addToArray(EnumScriptType.playerScripts, ANIMATION_INSTRUCTION_EVENT);
    }

    private static EnumScriptType cnpcCobblemonAddoncnpcCobblemonAddon$addVariant(String internalName, String name) {
        EnumScriptType instrument = cnpcCobblemonAddon$invokeInit(internalName, EnumScriptTypeMixin.$VALUES[EnumScriptTypeMixin.$VALUES.length - 1].ordinal() + 1, name);
        EnumScriptTypeMixin.$VALUES = addToArray(EnumScriptTypeMixin.$VALUES, instrument);
        return instrument;
    }

    private static EnumScriptType[] addToArray(EnumScriptType[] array, EnumScriptType type){
        EnumScriptType[] newArray = new EnumScriptType[array.length+1];
        newArray[array.length] = type;
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }
}