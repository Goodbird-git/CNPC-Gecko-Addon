package com.goodbird.cnpcgeckoaddon.utils;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.loading.object.BakedAnimations;

import java.util.List;
import java.util.Vector;

public class AnimationFileUtil {
    public static List<String> getAnimationList(String animFileName) {
        Vector<String> list = new Vector<>();
        BakedAnimations file = GeckoLibCache.getBakedAnimations().get(ResourceLocation.parse(animFileName));
        if (file != null) {
            for (Animation anim : file.animations().values()) {
                list.add(anim.name());
            }
        }
        return list;
    }

    public static List<String> getAnimationFileList() {
        Vector<String> list = new Vector<>();
        for (ResourceLocation resLoc : GeckoLibCache.getBakedAnimations().keySet()) {
            list.add(resLoc.toString());
        }
        return list;
    }
}

