package com.goodbird.cnpcgeckoaddon.hooklib.cnpchooks;

import com.goodbird.cnpcgeckoaddon.data.CustomModelDataProvider;
import com.goodbird.cnpcgeckoaddon.data.ICustomModelData;
import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.Hook;
import com.goodbird.cnpcgeckoaddon.hooklib.asm.ReturnCondition;
import com.goodbird.cnpcgeckoaddon.network.CPacketSyncTileManualAnim;
import com.goodbird.cnpcgeckoaddon.network.NetworkWrapper;
import com.goodbird.cnpcgeckoaddon.network.PacketSyncAnimation;
import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import com.goodbird.cnpcgeckoaddon.utils.NpcTextureUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import noppes.npcs.api.block.IBlockScripted;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.blocks.tiles.TileScripted;
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

    private static ICustomModelData getModelData(EntityNPCInterface npc){
        return npc.getCapability(CustomModelDataProvider.DATA_CAP, null);
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoModel(NPCWrapper<EntityNPCInterface> npc, String model) {
        getModelData(npc.getMCEntity()).setModel(model);
        npc.updateClient();
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoTexture(NPCWrapper<EntityNPCInterface> npc, String texture) {
        npc.getMCEntity().display.setSkinTexture(texture);
        npc.updateClient();
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoAnimationFile(NPCWrapper<EntityNPCInterface> npc, String animation) {
        getModelData(npc.getMCEntity()).setAnimFile(animation);
        npc.updateClient();
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoIdleAnimation(NPCWrapper<EntityNPCInterface> npc, String animation) {
        getModelData(npc.getMCEntity()).setIdleAnim(animation);
        npc.updateClient();
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoWalkAnimation(NPCWrapper<EntityNPCInterface> npc, String animation) {
        getModelData(npc.getMCEntity()).setWalkAnim(animation);
        npc.updateClient();
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

            if(!data.isHurtTintEnabled()){
                modelEntity.hurtTime = npc.hurtTime = 0;
                modelEntity.deathTime = npc.deathTime = 0;
            }

            if(npc.inventory.getLeftHand()!=null) {
                modelEntity.leftHeldItem = npc.inventory.getLeftHand().getMCItemStack();
            }
            modelEntity.headBoneName = data.getHeadBoneName();
            AnimationData animationData = modelEntity.getFactory().getOrCreateAnimationData(modelEntity.getUniqueID().hashCode());
            for(AnimationController controller : animationData.getAnimationControllers().values()){
                controller.transitionLengthTicks = data.getTransitionLengthTicks();
            }
            if(data.getHeight()!=modelEntity.height || data.getWidth() != modelEntity.width){
                modelEntity.setSize(data.getWidth(), data.getHeight());
                npc.updateHitbox();
            }
        }
    }

    @Hook(injectOnExit = true)
    public static void setDisplayNBT(TileScripted tile, NBTTagCompound compound) {
        if(compound.hasKey("renderTileTag")){
            tile.renderTile = new TileEntityCustomModel();
            NBTTagCompound saveTag = compound.getCompoundTag("renderTileTag");
            World[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
            if(FMLCommonHandler.instance().getMinecraftServerInstance()!=null && saveTag.hasKey("dimID")){
                tile.renderTile.setWorld(worlds[saveTag.getInteger("dimID")]);
            }else{
                tile.renderTile.setWorld(worlds[0]);
            }
            tile.renderTile.readFromNBT(saveTag);
        }
    }

    @Hook(injectOnExit = true)
    public static void getDisplayNBT(TileScripted tile, NBTTagCompound compound) {
        if(tile.renderTile!=null) {
            NBTTagCompound saveTag = new NBTTagCompound();
            tile.renderTile.writeToNBT(saveTag);
            if(tile.renderTile.getWorld()!= null && tile.renderTile.getWorld().provider != null)
                saveTag.setInteger("dimID", tile.renderTile.getWorld().provider.getDimension());
            compound.setTag("renderTileTag", saveTag);
        }
    }

    private static TileEntityCustomModel getOrCreateTECM(IBlockScripted scriptedBlock){
        TileScripted tile = (TileScripted) scriptedBlock.getMCTileEntity();
        if(!(tile.renderTile instanceof TileEntityCustomModel)){
            tile.renderTile = new TileEntityCustomModel(tile);
        }
        return (TileEntityCustomModel) tile.renderTile;
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoModel(BlockScriptedWrapper scriptedBlock, String model) {
        TileEntityCustomModel geckoTile = getOrCreateTECM(scriptedBlock);
        geckoTile.modelResLoc = new ResourceLocation(model);
        ((TileScripted) scriptedBlock.getMCTileEntity()).needsClientUpdate = true;
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoTexture(BlockScriptedWrapper scriptedBlock, String texture) {
        TileEntityCustomModel geckoTile = getOrCreateTECM(scriptedBlock);
        geckoTile.textureResLoc = new ResourceLocation(texture);
        ((TileScripted) scriptedBlock.getMCTileEntity()).needsClientUpdate = true;
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoAnimationFile(BlockScriptedWrapper scriptedBlock, String animation) {
        TileEntityCustomModel geckoTile = getOrCreateTECM(scriptedBlock);
        geckoTile.animResLoc = new ResourceLocation(animation);
        ((TileScripted) scriptedBlock.getMCTileEntity()).needsClientUpdate = true;
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void setGeckoIdleAnimation(BlockScriptedWrapper scriptedBlock, String animation) {
        TileEntityCustomModel geckoTile = getOrCreateTECM(scriptedBlock);
        geckoTile.idleAnimName = animation;
        ((TileScripted) scriptedBlock.getMCTileEntity()).needsClientUpdate = true;
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void syncAnimForPlayer(BlockScriptedWrapper scriptedBlock, AnimationBuilder builder, IPlayer<EntityPlayerMP> player) {
        NetworkWrapper.sendToPlayer(new CPacketSyncTileManualAnim(scriptedBlock.getMCTileEntity(), builder), player.getMCEntity());
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static void syncAnimForAll(BlockScriptedWrapper scriptedBlock, AnimationBuilder builder) {
        NetworkWrapper.sendToAll(new CPacketSyncTileManualAnim(scriptedBlock.getMCTileEntity(), builder));
    }
}
