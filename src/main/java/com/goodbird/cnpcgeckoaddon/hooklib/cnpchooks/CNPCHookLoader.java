package com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks;

import com.goodbird.cnpcgeckoaddon.hooklib.minecraft.HookLoader;
import com.goodbird.cnpcgeckoaddon.hooklib.minecraft.PrimaryClassTransformer;

public class CNPCHookLoader extends HookLoader {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    public void registerHooks() {
        registerHookContainer("com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks.AnnotationHooks");
    }
}
