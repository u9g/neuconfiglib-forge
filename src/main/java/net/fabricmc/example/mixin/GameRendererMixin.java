package net.fabricmc.example.mixin;

import net.fabricmc.example.util.render.BackgroundBlur;
import net.fabricmc.example.util.structs.FogColors;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyArgs(method = "updateFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;clearColor(FFFF)V"))
    private void onSetFogColor(Args args) {
        FogColors colors = new FogColors(args.get(0), args.get(1), args.get(2));
        BackgroundBlur.onFogColour(colors);
        args.set(0, colors.red);
        args.set(1, colors.green);
        args.set(2, colors.blue);
    }
}
