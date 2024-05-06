package com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks;

import com.goodbird.cnpcgeckoaddon.client.gui.GuiModelAnimation;
import com.goodbird.cnpcgeckoaddon.client.gui.GuiStringSelection;
import com.goodbird.cnpcgeckoaddon.client.gui.SubGuiModelExtras;
import com.goodbird.cnpcgeckoaddon.data.CustomModelDataProvider;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.Hook;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.ReturnCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.model.GuiCreationEntities;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.Vector;
@SideOnly(Side.CLIENT)
public class ClientHooks {
    @Hook(returnCondition = ReturnCondition.ON_TRUE)
    @SideOnly(Side.CLIENT)
    public static boolean renderModel(RenderCustomNpc thiss, EntityCustomNpc npc, float par2, float par3, float par4, float par5, float par6, float par7) {
        if(npc.modelData.getEntity(npc) instanceof IAnimatable){
            GL11.glRotatef(180, 1,0,0);
            GL11.glTranslated(0, -1.5,0);
            renderGeoModel(npc, npc.rotationYaw, Minecraft.getMinecraft().getRenderPartialTicks());
            return true;
        }
        return false;
    }
    @SideOnly(Side.CLIENT)
    private static void renderGeoModel(EntityCustomNpc npc, float rot, float partial)
    {
        //npc.modelData.getEntity(npc).renderYawOffset = npc.modelData.getEntity(npc).prevRenderYawOffset = 0;
        if (!npc.isInvisible())
        {
            Minecraft.getMinecraft().getRenderManager().renderEntity(npc.modelData.getEntity(npc), 0,0,0,rot,partial,false);
        }
        else if (!npc.isInvisibleToPlayer(Minecraft.getMinecraft().player))
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            Minecraft.getMinecraft().getRenderManager().renderEntity(npc.modelData.getEntity(npc), 0,0,0,rot,partial,false);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
        }
    }

    @Hook(injectOnExit = true)
    @SideOnly(Side.CLIENT)
    public static void initGui(GuiCreationEntities gui) {
        EntityNPCInterface npc = gui.npc;
        if (npc instanceof EntityCustomNpc && ((EntityCustomNpc) npc).modelData.getEntity(npc) instanceof EntityCustomModel) {
            EntityCustomModel customModel = (EntityCustomModel) ((EntityCustomNpc) npc).modelData.getEntity(npc);
            Vector<String> list = new Vector<>();
            for (ResourceLocation resLoc : GeckoLibCache.getInstance().getGeoModels().keySet()) {
                list.add(resLoc.toString());
            }
            gui.addLabel(new GuiNpcLabel(212, "Model:", gui.guiLeft + 124, gui.guiTop + 26, 0xffffff));
            gui.addButton(new GuiNpcButton(202, gui.guiLeft + 160, gui.guiTop + 20, 150, 20, customModel.modelResLoc.getResourcePath()));
            gui.addLabel(new GuiNpcLabel(213, "Model Animation:", gui.guiLeft + 124, gui.guiTop + 46, 0xffffff));
            gui.addButton(new GuiNpcButton(212, gui.guiLeft + 210, gui.guiTop + 40, 100, 20, "selectServer.edit"));
            gui.addButton(new GuiNpcButton(213, gui.guiLeft + 124, gui.guiTop + 60, 187, 20, "Extras"));
        }
    }

    @Hook(injectOnExit = true)
    @SideOnly(Side.CLIENT)
    public static void actionPerformed(GuiCreationEntities gui, GuiButton btn) {
        if (btn.id == 202) {
            Vector<String> list = new Vector<>();
            for (ResourceLocation resLoc : GeckoLibCache.getInstance().getGeoModels().keySet()) {
                list.add(resLoc.toString());
            }
            NoppesUtil.openGUI(Minecraft.getMinecraft().player, new GuiStringSelection(gui, "Selecting geckolib model:", list, name -> {
                gui.npc.getCapability(CustomModelDataProvider.DATA_CAP, null).setModel(name);
                gui.getButton(202).setDisplayText(name);
            }));
        }
        if (btn.id == 212) {
            NoppesUtil.openGUI(Minecraft.getMinecraft().player, new GuiModelAnimation(gui, gui.npc));
        }
        if(btn.id == 213){
            NoppesUtil.openGUI(Minecraft.getMinecraft().player, new SubGuiModelExtras(gui, gui.npc));
        }
    }


}