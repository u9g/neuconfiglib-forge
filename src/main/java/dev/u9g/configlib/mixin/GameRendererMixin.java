package dev.u9g.configlib.mixin;

import dev.u9g.configlib.util.render.BackgroundBlur;
import dev.u9g.configlib.util.structs.FogColors;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityRenderer.class)
public class GameRendererMixin {
    @ModifyArgs(method = "updateFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;clearColor(FFFF)V"))
    private void onSetFogColor(Args args) {
        FogColors colors = new FogColors(args.get(0), args.get(1), args.get(2));
        BackgroundBlur.onFogColour(colors);
        args.set(0, colors.red);
        args.set(1, colors.green);
        args.set(2, colors.blue);
    }
}
