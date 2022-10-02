package dev.u9g.configlib.config.elements;

import net.minecraft.client.gui.DrawableHelper;

public abstract class GuiElement extends DrawableHelper {

    public abstract void render();

    public abstract boolean mouseInput(int mouseX, int mouseY);

    public abstract boolean keyboardInput();
}
