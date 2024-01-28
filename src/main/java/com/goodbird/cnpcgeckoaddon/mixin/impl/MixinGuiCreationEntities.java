package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.client.gui.GuiModelAnimation;
import com.goodbird.cnpcgeckoaddon.client.gui.GuiStringSelection;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDataDisplay;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.model.GuiCreationEntities;
import noppes.npcs.client.gui.model.GuiCreationScreenInterface;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
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
            this.addButton(new GuiButtonNop(this, 202, this.guiLeft + 122, this.guiTop + 18, 120, 20, customModel.modelResLoc.getPath(), (b) -> {
                setSubGui(new GuiStringSelection(this, "Selecting geckolib model:", list, name -> {
                    ((IDataDisplay)npc.display).getCustomModelData().setModel(name);
                    getButton(202).setDisplayText(name);
                }));
            }));
            addLabel(new GuiLabel(12,"Model Animation:", this.guiLeft + 124, this.guiTop + 46,0xffffff));
            this.addButton(new GuiButtonNop(this,12,this.guiLeft + 210, this.guiTop + 40, 100, 20, "selectServer.edit",(b)->{
                setSubGui(new GuiModelAnimation());
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
