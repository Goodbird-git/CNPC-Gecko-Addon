package com.goodbird.cnpcgeckoaddon.client.gui;

import com.goodbird.cnpcgeckoaddon.data.CustomModelData;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiModelExtras extends GuiNPCInterface implements ITextfieldListener {
    public SubGuiModelExtras(EntityNPCInterface npc){
        this.npc = npc;
        closeOnEsc = true;
    }

    @Override
    public void init() {
        super.init();
        int y = guiTop + 44;
        addLabel(new GuiLabel(1,"Head Bone Name", guiLeft - 85, y + 5,0xffffff));
        addTextField(new GuiTextFieldNop(1,this, guiLeft + 50, y, 200, 20, getModelData(npc).getHeadBoneName()));
        y+=23;
        addLabel(new GuiLabel(2,"Transition Length (ticks)", guiLeft - 85, y + 5,0xffffff));
        GuiTextFieldNop transitionLength = new GuiTextFieldNop(2,this, guiLeft + 50, y,
                200, 20, ""+getModelData(npc).getTransitionLengthTicks());
        transitionLength.setNumbersOnly();
        transitionLength.setMinMaxDefault(0, Integer.MAX_VALUE, getModelData(npc).getTransitionLengthTicks());
        addTextField(transitionLength);
        addButton(new GuiButtonNop(this, 670, width - 22, 2, 20, 20, "X"));
    }

    public CustomModelData getModelData(EntityNPCInterface npc){
        return ((IDataDisplay)npc.display).getCustomModelData();
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
        if(button.id == 670){
            close();
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if(textfield.id == 1){
            getModelData(npc).setHeadBoneName(textfield.getValue());
        }
        if(textfield.id == 2){
            getModelData(npc).setTransitionLengthTicks(textfield.getInteger());
        }
    }
}