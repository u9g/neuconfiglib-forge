package net.fabricmc.example.mixin;

import net.fabricmc.example.util.render.BackgroundBlur;
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
    }
}
