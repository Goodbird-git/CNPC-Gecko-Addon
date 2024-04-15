package com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks;

import com.goodbird.cnpcgeckoaddon.hooklib.minecraft.HookLoader;
import com.goodbird.cnpcgeckoaddon.hooklib.minecraft.PrimaryClassTransformer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class CNPCHookLoader extends HookLoader {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    boolean isServer(){
        try{
            Class.forName("com.goodbird.cnpcgeckoaddon.utils.ClientOnlyClass");
        } catch (ClassNotFoundException e) {
            return true;
        }
        return false;
    }

    @Override
    public void registerHooks() {
        if(!isServer()) {
            registerHookContainer("com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks.ClientHooks");
        }
        registerHookContainer("com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks.CommonHooks");
    }
}
