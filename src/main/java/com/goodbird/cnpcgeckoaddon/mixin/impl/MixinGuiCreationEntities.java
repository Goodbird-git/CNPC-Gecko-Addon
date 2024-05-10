package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.client.gui.GuiModelAnimation;
import com.goodbird.cnpcgeckoaddon.client.gui.GuiStringSelection;
import com.goodbird.cnpcgeckoaddon.client.gui.SubGuiModelExtras;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.model.GuiCreationEntities;
import noppes.npcs.client.gui.model.GuiCreationScreenInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.entity.EntityCustomNpc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.Vector;

@Mixin(GuiCreationEntities.class)
public class MixinGuiCreationEntities extends GuiCreationScreenInterface {

    public MixinGuiCreationEntities() {
        super(null);
    }

    @Inject(method = "init",at = @At("TAIL"))
    public void init(CallbackInfo ci){
        if(npc instanceof EntityCustomNpc && ((EntityCustomNpc)npc).modelData.getEntity(npc) instanceof EntityCustomModel) {
            EntityCustomModel customModel = (EntityCustomModel) ((EntityCustomNpc)npc).modelData.getEntity(npc);
            Vector<String> list = new Vector<>();
            for(ResourceLocation resLoc : GeckoLibCache.getInstance().getGeoModels().keySet()){
                list.add(resLoc.toString());
            }
            addLabel(new GuiNpcLabel(212,"Model:", this.guiLeft + 124, this.guiTop + 26,0xffffff));
            this.addButton(new GuiNpcButton(this, 202, this.guiLeft + 160, this.guiTop + 20, 150, 20, customModel.modelResLoc.getPath(), (b) -> {
                setSubGui(new GuiStringSelection(this, "Selecting geckolib model:", list, name -> {
                    ((IDataDisplay)npc.display).getCustomModelData().setModel(name);
                    getButton(202).setDisplayText(name);
                }));
            }));
            addLabel(new GuiNpcLabel(213,"Model Animation:", this.guiLeft + 124, this.guiTop + 46,0xffffff));
            this.addButton(new GuiNpcButton(this,212,this.guiLeft + 210, this.guiTop + 40, 100, 20, "selectServer.edit",(b)->{
                setSubGui(new GuiModelAnimation());
            }));
            this.addButton(new GuiNpcButton(this, 213, this.guiLeft + 124, this.guiTop + 60, 187, 20, "Extras", (b)->{
                setSubGui(new SubGuiModelExtras(this.npc));
            }));
        }
    }

    @Override
    public void drawNpc(LivingEntity entity, int x, int y, float zoomed, int rotation) {
        if(wrapper.subgui==null) {
            super.drawNpc(entity, x, y, zoomed, rotation);
        }
    }
}
