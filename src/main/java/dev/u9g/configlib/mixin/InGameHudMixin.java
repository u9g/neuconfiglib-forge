package dev.u9g.configlib.mixin;

import dev.u9g.configlib.util.render.BackgroundBlur;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class InGameHudMixin {
    @Inject(
            method = "renderGameOverlay",
            at = @At(value = "HEAD")
    )
    private void pre_RenderGameOverlayEvent(float tickDelta, CallbackInfo ci) {
        BackgroundBlur.onScreenRender();
    }
}
