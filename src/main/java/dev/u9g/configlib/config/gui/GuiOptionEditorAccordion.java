package dev.u9g.configlib.config.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.u9g.configlib.config.struct.ConfigProcessor;
import dev.u9g.configlib.M;
import dev.u9g.configlib.util.render.RenderUtils;
import dev.u9g.configlib.util.render.TextRenderUtils;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiOptionEditorAccordion extends GuiOptionEditor {

    private int accordionId;
    private boolean accordionToggled;

    public GuiOptionEditorAccordion(ConfigProcessor.ProcessedOption option, int accordionId) {
        super(option);
        this.accordionToggled = (boolean) option.get();
        this.accordionId = accordionId;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    public int getAccordionId() {
        return accordionId;
    }

    public boolean getToggled() {
        return accordionToggled;
    }

    @Override
    public void render(int x, int y, int width) {
        int height = getHeight();
        RenderUtils.drawFloatingRectDark(x, y, width, height, true);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color4f(1, 1, 1, 1);
        worldrenderer.begin(GL11.GL_TRIANGLES, VertexFormats.POSITION);
        if (accordionToggled) {
            worldrenderer.vertex((double) x + 6, (double) y + 6, 0.0D).next();
            worldrenderer.vertex((double) x + 9.75f, (double) y + 13.5f, 0.0D).next();
            worldrenderer.vertex((double) x + 13.5f, (double) y + 6, 0.0D).next();
        } else {
            worldrenderer.vertex((double) x + 6, (double) y + 13.5f, 0.0D).next();
            worldrenderer.vertex((double) x + 13.5f, (double) y + 9.75f, 0.0D).next();
            worldrenderer.vertex((double) x + 6, (double) y + 6, 0.0D).next();
        }
        tessellator.draw();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();

        TextRenderUtils.drawStringScaledMaxWidth(option.name, M.C.textRenderer, x + 18, y + 6, false, width - 10, 0xc0c0c0);
    }

    @Override
    public boolean mouseInput(int x, int y, int width, int mouseX, int mouseY) {
        if (Mouse.getEventButtonState() && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + getHeight()) {
            accordionToggled = !accordionToggled;
            return true;
        }

        return false;
    }

    @Override
    public boolean keyboardInput() {
        return false;
    }
}
