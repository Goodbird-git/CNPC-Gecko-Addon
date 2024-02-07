package com.goodbird.cnpcgeckoaddon.hooklib.minecraft;

import com.goodbird.cnpcgeckoaddon.hooklib.asm.ClassMetadataReader;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.HookClassTransformer;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.AsmHook;
import net.minecraftforge.fml.common.asm.transformers.DeobfuscationTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public abstract class HookLoader implements IFMLLoadingPlugin {

    private static DeobfuscationTransformer deobfuscationTransformer;

    private static ClassMetadataReader deobfuscationMetadataReader;

    static {
        deobfuscationMetadataReader = new DeobfuscationMetadataReader();
    }

    public static HookClassTransformer getTransformer() {
        return PrimaryClassTransformer.instance.registeredSecondTransformer ?
                MinecraftClassTransformer.instance : PrimaryClassTransformer.instance;
    }

    public static void registerHook(AsmHook hook) {
        getTransformer().registerHook(hook);
    }

    public static void registerHookContainer(String className) {
        getTransformer().registerHookContainer(className);
    }

    public static ClassMetadataReader getDeobfuscationMetadataReader() {
        return deobfuscationMetadataReader;
    }
	
	static DeobfuscationTransformer getDeobfuscationTransformer() {
        if (HookLibPlugin.getObfuscated() && deobfuscationTransformer == null) {
            deobfuscationTransformer = new DeobfuscationTransformer();
        }
        return deobfuscationTransformer;
    }

    // 1.6.x only
    public String[] getLibraryRequestClass() {
        return null;
    }

    // 1.7.x only
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        registerHooks();
    }

    protected abstract void registerHooks();
}
