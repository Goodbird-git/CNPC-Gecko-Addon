package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.player.GuiDialogInteract;
import noppes.npcs.client.gui.player.moderngui.GuiDialogModern;
import noppes.npcs.client.gui.player.moderngui.GuiQuestModern;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    @Nullable
    public Screen screen;

    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreen(Screen guiScreen, CallbackInfo ci){
        if(!isDialog(guiScreen) && isDialog(screen)){
            EntityNPCInterface npc = ((GuiNPCInterface)screen).npc;
            if(npc instanceof EntityCustomNpc && ((EntityCustomNpc)npc).modelData.getEntity(npc) instanceof EntityCustomModel customModel){
                customModel.dialogAnim = null;
            }
        }
    }

    private boolean isDialog(Screen gui){
        return gui instanceof GuiDialogInteract || gui instanceof GuiDialogModern || gui instanceof GuiQuestModern;
    }
}
