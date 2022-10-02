package dev.u9g.configlib.mixin;

import dev.u9g.configlib.util.render.BackgroundBlur;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(
            method = "render",
            at = @At(value = "HEAD")
    )
    private void onScreenRender(float tickDelta, CallbackInfo ci) {
        BackgroundBlur.onScreenRender();
//        NotificationHandler.renderNotification();
    }
}
