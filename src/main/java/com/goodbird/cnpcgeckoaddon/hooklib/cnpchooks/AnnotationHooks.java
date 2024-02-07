package com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks;

import com.goodbird.cnpcgeckoaddon.client.gui.GuiModelAnimation;
import com.goodbird.cnpcgeckoaddon.client.gui.GuiStringSelection;
import com.goodbird.cnpcgeckoaddon.data.CustomModelDataProvider;
import com.goodbird.cnpcgeckoaddon.data.ICustomModelData;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.Hook;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.ReturnCondition;
import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import com.goodbird.cnpcgeckoaddon.network.PacketSyncAnimation;
import com.goodbird.cnpcgeckoaddon.utils.NpcTextureUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.model.GuiCreationEntities;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataDisplay;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.Vector;

public class AnnotationHooks {

    @Hook(returnCondition = ReturnCondition.ON_TRUE)
    public static boolean renderModel(RenderCustomNpc thiss, EntityCustomNpc npc, float par2, float par3, float par4, float par5, float par6, float par7) {
        if(npc.modelData.getEntity(npc) instanceof IAnimatable){
            GL11.glRotatef(180, 1,0,0);
            GL11.glTranslated(0, -1.5,0);
            renderGeoModel(npc, npc.rotationYaw, Minecraft.getMinecraft().getRenderPartialTicks());
            return true;
        }
        return false;
    }

    private static void renderGeoModel(EntityCustomNpc npc, float rot, float partial)
    {
        npc.modelData.getEntity(npc).renderYawOffset = npc.modelData.getEntity(npc).prevRenderYawOffset = 0;
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

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static AnimationBuilder createAnimBuilder(WrapperNpcAPI api){
        return new AnimationBuilder();
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void syncAnimationsFor(NPCWrapper<EntityNPCInterface> wrapper, IPlayer player, AnimationBuilder builder) {
        NetworkWrapper.sendToPlayer(new PacketSyncAnimation(wrapper.getMCEntity(), builder), player.getMCEntity());
    }
    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void syncAnimationsForAll(NPCWrapper<EntityNPCInterface> wrapper, AnimationBuilder builder) {
        NetworkWrapper.sendToAll(new PacketSyncAnimation(wrapper.getMCEntity(), builder));
    }

    @Hook(injectOnExit = true)
    public static void Copy(EntityUtil cl, EntityLivingBase copied, EntityLivingBase entity) {
        if (entity instanceof EntityCustomModel && copied instanceof EntityNPCInterface) {
            EntityCustomModel modelEntity = (EntityCustomModel) entity;
            EntityNPCInterface npc = (EntityNPCInterface) copied;
            ICustomModelData data = npc.getCapability(CustomModelDataProvider.DATA_CAP, null);
            modelEntity.textureResLoc = NpcTextureUtils.getNpcTexture((EntityNPCInterface) copied);
            modelEntity.modelResLoc = new ResourceLocation(data.getModel());
            modelEntity.animResLoc = new ResourceLocation(data.getAnimFile());
            modelEntity.idleAnimName = data.getIdleAnim();
            modelEntity.walkAnimName = data.getWalkAnim();
            if(npc.inventory.getLeftHand()!=null) {
                modelEntity.leftHeldItem = npc.inventory.getLeftHand().getMCItemStack();
            }
        }
    }

    @Hook(injectOnExit = true)
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
        }
    }

    @Hook(injectOnExit = true)
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
    }

    @Hook(injectOnExit = true)
    public static void writeToNBT(DataDisplay data, NBTTagCompound nbttagcompound) {
        ICustomModelData modeldata = data.npc.getCapability(CustomModelDataProvider.DATA_CAP, null);
        if(modeldata!=null)
            modeldata.writeToNBT(nbttagcompound);
    }

    @Hook(injectOnExit = true)
    public static void readToNBT(DataDisplay data, NBTTagCompound nbttagcompound){
        ICustomModelData modeldata = data.npc.getCapability(CustomModelDataProvider.DATA_CAP, null);
        if(modeldata!=null)
            modeldata.readFromNBT(nbttagcompound);
    }
}
