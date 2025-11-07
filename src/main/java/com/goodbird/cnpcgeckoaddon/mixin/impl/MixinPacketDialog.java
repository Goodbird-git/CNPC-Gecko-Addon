package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.entity.EntityCustomModel;
import com.goodbird.cnpcgeckoaddon.mixin.IDialog;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.client.PacketDialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.RawAnimation;

@Mixin(PacketDialog.class)
public class MixinPacketDialog {

    @Inject(method = "openDialog", at=@At("TAIL"))
    private static void openDialog(Dialog dialog, EntityNPCInterface npc, Player player, CallbackInfo ci){
        if(npc instanceof EntityCustomNpc && ((EntityCustomNpc)npc).modelData.getEntity(npc) instanceof EntityCustomModel customModel && ((IDialog)dialog).hasAnimation()){
            Animation.LoopType type = switch (((IDialog)dialog).getLoopType()) {
                case 1 -> Animation.LoopType.LOOP;
                case 2 -> Animation.LoopType.HOLD_ON_LAST_FRAME;
                default -> Animation.LoopType.PLAY_ONCE;
            };
            customModel.dialogAnim = RawAnimation.begin().then(((IDialog)dialog).getAnimation(), type);
        }
    }
}
