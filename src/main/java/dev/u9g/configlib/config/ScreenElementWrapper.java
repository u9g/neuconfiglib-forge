package dev.u9g.configlib.config;

import dev.u9g.configlib.M;
import dev.u9g.configlib.config.elements.GuiElement;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class ScreenElementWrapper extends GuiScreen {

    public final GuiElement element;

    public ScreenElementWrapper(GuiElement element) {
        this.element = element;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        element.render();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventX() * this.width / M.C.displayWidth;
        int j = this.height - Mouse.getEventY() * this.height / M.C.displayHeight - 1;
        element.mouseInput(i, j);
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        element.keyboardInput();
    }
}
