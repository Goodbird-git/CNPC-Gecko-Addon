package com.goodbird.cnpcgeckoaddon.client.gui;

import com.goodbird.cnpcgeckoaddon.data.CustomModelData;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;
import com.goodbird.cnpcgeckoaddon.utils.AnimationFileUtil;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class GuiModelAnimation extends GuiNPCInterface implements ITextfieldListener {

    @Override
    public void init() {
        super.init();
        int y = guiTop + 44;
        addSelectionBlock(1,y,"Animation File:",getModelData(npc).getAnimFile());
        addSelectionBlock(2,y+=23,"Idle:",getModelData(npc).getIdleAnim());
        addSelectionBlock(3,y+=23,"Walk:",getModelData(npc).getWalkAnim());
//        addSelectionBlock(4,y+=23,"Attack:",getModelData(npc).getAttackAnim()); COMMING SOON
//        addSelectionBlock(5,y+23,"Hurt:",getModelData(npc).getHurtAnim()); COMMING SOON
        this.addButton(new GuiButtonNop(this, 670, width - 22, 2, 20, 20, "X"));
    }

    public CustomModelData getModelData(EntityNPCInterface npc){
        return ((IDataDisplay)npc.display).getCustomModelData();
    }
    
    public void addSelectionBlock(int id, int y, String label, String value){
        this.addLabel(new GuiLabel(id,label, guiLeft - 85, y + 5,0xffffff));
        addTextField(new GuiTextFieldNop(id,this, guiLeft - 40, y, 200, 20, value));
        this.addButton(new GuiButtonNop(this,id, guiLeft + 163, y, 80, 20, "mco.template.button.select"));
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
        if(button.id == 670){
            close();
        }
        if(button.id==1){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib animation file:",
                    AnimationFileUtil.getAnimationFileList(), (name)-> getModelData(npc).setAnimFile(name)));
        }
        if(button.id==2){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib idle animation:",
                    AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                    (name)-> getModelData(npc).setIdleAnim(name)));
        }
        if(button.id==3){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib walk animation:",
                    AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                    (name)-> getModelData(npc).setWalkAnim(name)));
        }
        if(button.id==4){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib attack animation:",
                    AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                    (name)-> getModelData(npc).setAnimFile(name)));
        }
        if(button.id==5){
            setSubGui(new GuiStringSelection(this,"Selecting geckolib hurt animation:",
                    AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                    (name)-> getModelData(npc).setHurtAnim(name)));
        }
    }

    public boolean isValidAnimFile(String name){
        return GeckoLibCache.getInstance().getAnimations().containsKey(new ResourceLocation(name));
    }

    public boolean isValidAnimation(String name){
        return name.isEmpty() || AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()).contains(name);
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if(textfield.id == 1){
            if(isValidAnimFile(textfield.getValue()))
                getModelData(npc).setAnimFile(textfield.getValue());
            else
                textfield.setValue(getModelData(npc).getAnimFile());
        }
        if(textfield.id == 2){
            if(isValidAnimation(textfield.getValue()))
                getModelData(npc).setIdleAnim(textfield.getValue());
            else
                textfield.setValue(getModelData(npc).getIdleAnim());
        }
        if(textfield.id == 3){
            if(isValidAnimation(textfield.getValue()))
                getModelData(npc).setWalkAnim(textfield.getValue());
            else
                textfield.setValue(getModelData(npc).getWalkAnim());
        }
        if(textfield.id == 4){
            if(isValidAnimation(textfield.getValue()))
                getModelData(npc).setAttackAnim(textfield.getValue());
            else
                textfield.setValue(getModelData(npc).getAttackAnim());
        }
        if(textfield.id == 5){
            if(isValidAnimation(textfield.getValue()))
                getModelData(npc).setHurtAnim(textfield.getValue());
            else
                textfield.setValue(getModelData(npc).getHurtAnim());
        }
    }
}
