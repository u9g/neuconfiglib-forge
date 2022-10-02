package dev.u9g.configlib.config.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.u9g.configlib.config.struct.ConfigProcessor;
import dev.u9g.configlib.M;
import dev.u9g.configlib.util.render.RenderUtils;
import dev.u9g.configlib.util.render.TextRenderUtils;
import net.minecraft.client.font.TextRenderer;

public abstract class GuiOptionEditor {

    protected final ConfigProcessor.ProcessedOption option;
    private static final int HEIGHT = 45;

    public GuiOptionEditor(ConfigProcessor.ProcessedOption option) {
        this.option = option;
    }

    public void render(int x, int y, int width) {
        int height = getHeight();

        TextRenderer fr = M.C.textRenderer;
        RenderUtils.drawFloatingRectDark(x, y, width, height, true);
        TextRenderUtils.drawStringCenteredScaledMaxWidth(option.name, fr, x + width / 6, y + 13, true, width / 3 - 10, 0xc0c0c0);

        int maxLines = 5;
        float scale = 1;
        int lineCount = fr.wrapLines(option.desc, width * 2 / 3 - 10).size();

        if (lineCount <= 0) return;

        float paraHeight = 9 * lineCount - 1;

        while (paraHeight >= HEIGHT - 10) {
            scale -= 1 / 8f;
            lineCount = fr.wrapLines(option.desc, (int) (width * 2 / 3 / scale - 10)).size();
            paraHeight = (int) (9 * scale * lineCount - 1 * scale);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translatef(x + 5 + width / 3f, y + HEIGHT / 2f - paraHeight / 2, 0);
        GlStateManager.scalef(scale, scale, 1);

        fr.drawTrimmed(option.desc, 0, 0, (int) (width * 2 / 3 / scale - 10), 0xc0c0c0);

        GlStateManager.popMatrix();
    }

    public int getHeight() {
        return HEIGHT;
    }

    public abstract boolean mouseInput(int x, int y, int width, int mouseX, int mouseY);

    public abstract boolean keyboardInput();

    public boolean mouseInputOverlay(int x, int y, int width, int mouseX, int mouseY) {
        return false;
    }

    public void renderOverlay(int x, int y, int width) {}
}
