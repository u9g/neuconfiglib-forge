package dev.u9g.configlib.config;

import dev.u9g.configlib.M;
import dev.u9g.configlib.config.elements.GuiElement;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.input.Mouse;

public class ScreenElementWrapper extends Screen {

    public final GuiElement element;

    public ScreenElementWrapper(GuiElement element) {
        this.element = element;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        element.render();
    }

    @Override
    public void handleMouse() {
        super.handleMouse();
        int i = Mouse.getEventX() * this.width / M.C.width;
        int j = this.height - Mouse.getEventY() * this.height / M.C.height - 1;
        element.mouseInput(i, j);
    }

    @Override
    public void handleKeyboard() {
        super.handleKeyboard();
        element.keyboardInput();
    }
}
