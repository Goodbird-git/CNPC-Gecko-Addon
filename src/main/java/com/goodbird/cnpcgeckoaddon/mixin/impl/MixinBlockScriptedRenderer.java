package com.goodbird.cnpcgeckoaddon.mixin.impl;

import com.goodbird.cnpcgeckoaddon.tile.TileEntityCustomModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3f;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.client.renderer.blocks.BlockScriptedRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockScriptedRenderer.class)
public abstract class MixinBlockScriptedRenderer {

    @Shadow(remap = false)
    protected abstract boolean overrideModel();


    @Inject(method = "render(Lnoppes/npcs/blocks/tiles/TileScripted;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;II)V", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void customGeckoModelRendering(final TileScripted tileScripted, final float partialTicks, final MatrixStack matrixStack, final IRenderTypeBuffer buffer, final int light, final int overlay, CallbackInfo ci) {
        if(overrideModel()) return;
        if(!(tileScripted.renderTile instanceof TileEntityCustomModel)) return;
        matrixStack.pushPose();
        matrixStack.translate(0.5,0.5, 0.5);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float)tileScripted.rotationY));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees((float)tileScripted.rotationX));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float)tileScripted.rotationZ));
        matrixStack.scale(tileScripted.scaleX, tileScripted.scaleY, tileScripted.scaleZ);
        matrixStack.translate(-0.5,-0.5/tileScripted.scaleY, -0.5);
        TileEntityRendererDispatcher.instance.getRenderer(tileScripted.renderTile).render(tileScripted.renderTile, partialTicks, matrixStack, buffer, light, overlay);
        matrixStack.popPose();
        ci.cancel();
    }
}
