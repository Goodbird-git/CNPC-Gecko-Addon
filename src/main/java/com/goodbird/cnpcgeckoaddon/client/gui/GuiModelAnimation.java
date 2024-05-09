package com.goodbird.cnpcgeckoaddon.client.gui;

import com.goodbird.cnpcgeckoaddon.data.CustomModelDataProvider;
import com.goodbird.cnpcgeckoaddon.data.ICustomModelData;
import com.goodbird.cnpcgeckoaddon.utils.AnimationFileUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.entity.EntityNPCInterface;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class GuiModelAnimation extends SubGuiInterface implements ITextfieldListener, ISubGuiListener {
    public GuiModelAnimation(GuiScreen parent, EntityNPCInterface npc){
        this.npc = npc;
        closeOnEsc = true;
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        int y = guiTop + 44;
        addSelectionBlock(1,y,"Animation File:",getModelData(npc).getAnimFile());
        addSelectionBlock(2,y+=23,"Idle:",getModelData(npc).getIdleAnim());
        addSelectionBlock(3,y+23,"Walk:",getModelData(npc).getWalkAnim());
//        addSelectionBlock(4,y+=23,"Melee Attack:",getModelData(npc).getMeleeAttackAnim());
//        addSelectionBlock(5,y+=23,"Ranged Attack:",getModelData(npc).getRangedAttackAnim());
//        addSelectionBlock(6,y+23,"Hurt:",getModelData(npc).getHurtAnim());
        addButton(new GuiNpcButton(670, width - 22, 2, 20, 20, "X"));
    }

    public void addSelectionBlock(int id, int y, String label, String value){
        addLabel(new GuiNpcLabel(id,label, guiLeft - 85, y + 5,0xffffff));
        addTextField(new GuiNpcTextField(id,this, fontRenderer, guiLeft - 10, y, 200, 20, value));
        this.addButton(new GuiNpcButton(id, guiLeft + 193, y, 80, 20, "mco.template.button.select"));
    }

    public ICustomModelData getModelData(EntityNPCInterface npc){
        return npc.getCapability(CustomModelDataProvider.DATA_CAP, null);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);

        if(button.id == 670){
            close();
        }
        if(button.id==1){
            NoppesUtil.openGUI(this.player, new GuiStringSelection(this,"Selecting geckolib animation file:",
                    AnimationFileUtil.getAnimationFileList(), (name)-> getModelData(npc).setAnimFile(name)));
        }
        if(button.id==2){
            NoppesUtil.openGUI(this.player, new GuiStringSelection(this,"Selecting geckolib idle animation:",
                    AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                    (name)-> getModelData(npc).setIdleAnim(name)));
        }
        if(button.id==3){
            NoppesUtil.openGUI(this.player, new GuiStringSelection(this,"Selecting geckolib walk animation:",
                    AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                    (name)-> getModelData(npc).setWalkAnim(name)));
        }
        if(button.id==4){
            NoppesUtil.openGUI(this.player, new GuiStringSelection(this,"Selecting geckolib melee attack animation:",
                    AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                    (name)-> getModelData(npc).setMeleeAttackAnim(name)));
        }
        if(button.id==5){
            NoppesUtil.openGUI(this.player, new GuiStringSelection(this,"Selecting geckolib ranged attack animation:",
                    AnimationFileUtil.getAnimationList(getModelData(npc).getAnimFile()),
                    (name)-> getModelData(npc).setRangedAttackAnim(name)));
        }
        if(button.id==6){
            NoppesUtil.openGUI(this.player, new GuiStringSelection(this,"Selecting geckolib hurt animation:",
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
    public void unFocused(GuiNpcTextField textfield) {
        if(textfield.getId() == 1){
            if(isValidAnimFile(textfield.getText()))
                getModelData(npc).setAnimFile(textfield.getText());
            else
                textfield.setText(getModelData(npc).getAnimFile());
        }
        if(textfield.getId() == 2){
            if(isValidAnimation(textfield.getText()))
                getModelData(npc).setIdleAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getIdleAnim());
        }
        if(textfield.getId() == 3){
            if(isValidAnimation(textfield.getText()))
                getModelData(npc).setWalkAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getWalkAnim());
        }
        if(textfield.getId() == 4){
            if(isValidAnimation(textfield.getText()))
                getModelData(npc).setMeleeAttackAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getMeleeAttackAnim());
        }
        if(textfield.getId() == 5){
            if(isValidAnimation(textfield.getText()))
                getModelData(npc).setRangedAttackAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getRangedAttackAnim());
        }
        if(textfield.getId() == 6){
            if(isValidAnimation(textfield.getText()))
                getModelData(npc).setHurtAnim(textfield.getText());
            else
                textfield.setText(getModelData(npc).getHurtAnim());
        }
    }

    @Override
    public void subGuiClosed(SubGuiInterface subGuiInterface) {
        initGui();
    }

    @Override
    public void close() {
        super.close();
        NoppesUtil.openGUI(this.player,parent);
    }
}
