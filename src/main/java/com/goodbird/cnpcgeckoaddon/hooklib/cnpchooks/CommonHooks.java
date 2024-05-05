package com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks;

import com.goodbird.cnpcgeckoaddon.data.CustomModelDataProvider;
import com.goodbird.cnpcgeckoaddon.data.ICustomModelData;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.Hook;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.ReturnCondition;
import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import com.goodbird.cnpcgeckoaddon.network.PacketSyncAnimation;
import com.goodbird.cnpcgeckoaddon.utils.NpcTextureUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataDisplay;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

public class CommonHooks {
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
            modelEntity.headBoneName = data.getHeadBoneName();
            AnimationData animationData = modelEntity.getFactory().getOrCreateAnimationData(modelEntity.getUniqueID().hashCode());
            for(AnimationController controller : animationData.getAnimationControllers().values()){
                controller.transitionLengthTicks = data.getTransitionLengthTicks();
            }
        }
    }
}
