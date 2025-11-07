package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.client.gui.GuiModelAnimation;
import com.goodbird.cnpcgeckoaddon.client.gui.GuiStringSelection;
import com.goodbird.cnpcgeckoaddon.data.CustomModelData;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import com.goodbird.cnpcgeckoaddon.mixin.IDialog;
import com.goodbird.cnpcgeckoaddon.utils.AnimationFileUtil;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiDialogEdit;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.GuiTextAreaScreen;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDialogEdit.class)
public class MixinGuiDialogEdit extends GuiBasic {
    @Shadow(remap = false)
    private Dialog dialog;

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci){
        addButton(new GuiButtonNop(this, 77, guiLeft + 214, guiTop + 114, 84, 20, "dialog.selectanimation"));
        addButton(new GuiButtonNop(this, 78, guiLeft + 300, guiTop + 114, 58, 20, new String[]{"looptype.once","looptype.loop"}, ((IDialog)dialog).getLoopType()));
        addButton(new GuiButtonNop(this, 79, guiLeft + 360, guiTop + 114, 20, 20, "X"));
        if(((IDialog)dialog).hasAnimation()) {
            getButton(77).setDisplayText(((IDialog)dialog).getAnimation());
        }
    }

    @Unique
    private CustomModelData getModelData(EntityNPCInterface npc){
        return ((IDataDisplay)npc.display).getCustomModelData();
    }

    @Inject(method = "buttonEvent", at = @At("TAIL"), remap = false)
    public void buttonEvent(GuiButtonNop guibutton, CallbackInfo ci){
        int id = guibutton.id;
        if(id == 77){
            setSubGui(new GuiStringSelection(this,"Selecting dialog animation:",
                    AnimationFileUtil.getAnimationList(getModelData(NoppesUtil.getLastNpc()).getAnimFile()),
                    (name)-> ((IDialog)dialog).setAnimation(name)));
        }
        if(id == 78){
            ((IDialog)dialog).setLoopType(guibutton.getValue());
            init();
        }
        if(id == 79){
            ((IDialog)dialog).setAnimation("");
            init();
        }
    }
}
