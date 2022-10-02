package dev.u9g.configlib.config.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.u9g.configlib.M;
import dev.u9g.configlib.config.GuiTextures;
import dev.u9g.configlib.config.struct.ConfigProcessor;
import dev.u9g.configlib.util.render.RenderUtils;
import dev.u9g.configlib.config.Config;
import dev.u9g.configlib.util.render.TextRenderUtils;
import org.lwjgl.input.Mouse;

public class GuiOptionEditorButton extends GuiOptionEditor {

    private String runnableId;
    private String buttonText;
    private Config config;

    public GuiOptionEditorButton(ConfigProcessor.ProcessedOption option, String runnableId, String buttonText, Config config) {
        super(option);
        this.runnableId = runnableId;
        this.config = config;

        this.buttonText = buttonText;
        if (this.buttonText != null && this.buttonText.isEmpty()) this.buttonText = null;
    }

    @Override
    public void render(int x, int y, int width) {
        super.render(x, y, width);

        int height = getHeight();

        GlStateManager.color4f(1, 1, 1, 1);
        M.C.getTextureManager().bindTexture(GuiTextures.button_tex);
        RenderUtils.drawTexturedRect(x + width / 6 - 24, y + height - 7 - 14, 48, 16);

        if (buttonText != null) {
            TextRenderUtils.drawStringCenteredScaledMaxWidth(buttonText, M.C.textRenderer, x + width / 6, y + height - 7 - 6, false, 44, 0xFF303030);
        }
    }

    @Override
    public boolean mouseInput(int x, int y, int width, int mouseX, int mouseY) {
        if (Mouse.getEventButtonState()) {
            int height = getHeight();
            if (mouseX > x + width / 6 - 24 && mouseX < x + width / 6 + 24 && mouseY > y + height - 7 - 14 && mouseY < y + height - 7 + 2) {
                config.executeRunnable(runnableId);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyboardInput() {
        return false;
    }
}
