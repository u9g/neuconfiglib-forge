package dev.u9g.configlib.config.gui;

import dev.u9g.configlib.M;
import dev.u9g.configlib.config.Position;
import dev.u9g.configlib.util.Utils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiPositionEditor extends Screen {

    private final Position position;
    private Position originalPosition;
    private final int elementWidth;
    private final int elementHeight;
    private final Runnable renderCallback;
    private final Runnable positionChangedCallback;
    private final Runnable closedCallback;
    private boolean clicked = false;
    private int grabbedX = 0;
    private int grabbedY = 0;

    private int guiScaleOverride = -1;

    public GuiPositionEditor(Position position, int elementWidth, int elementHeight, Runnable renderCallback, Runnable positionChangedCallback, Runnable closedCallback) {
        this.position = position;
        this.originalPosition = position.clone();
        this.elementWidth = elementWidth;
        this.elementHeight = elementHeight;
        this.renderCallback = renderCallback;
        this.positionChangedCallback = positionChangedCallback;
        this.closedCallback = closedCallback;
    }

    public GuiPositionEditor withScale(int scale) {
        this.guiScaleOverride = scale;
        return this;
    }

    @Override
    public void removed() {
        super.removed();
        closedCallback.run();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        Window scaledResolution;
        if (guiScaleOverride >= 0) {
            scaledResolution = Utils.pushGuiScale(guiScaleOverride);
        } else {
            scaledResolution = new Window(M.C);
        }

        this.width = scaledResolution.getWidth();
        this.height = scaledResolution.getHeight();
        mouseX = Mouse.getX() * width / M.C.width;
        mouseY = height - Mouse.getY() * height / M.C.height - 1;

        renderBackground();

        if (clicked) {
            grabbedX += position.moveX(mouseX - grabbedX, elementWidth, scaledResolution);
            grabbedY += position.moveY(mouseY - grabbedY, elementHeight, scaledResolution);
        }

        renderCallback.run();

        int x = position.getAbsX(scaledResolution, elementWidth);
        int y = position.getAbsY(scaledResolution, elementHeight);

        if (position.isCenterX()) x -= elementWidth / 2;
        if (position.isCenterY()) y -= elementHeight / 2;
        DrawableHelper.fill(x, y, x + elementWidth, y + elementHeight, 0x80404040);

        if (guiScaleOverride >= 0) {
            Utils.pushGuiScale(-1);
        }

        scaledResolution = new Window(M.C);
        Utils.drawStringCentered("Position Editor", M.C.textRenderer, scaledResolution.getWidth() / 2, 8, true, 0xffffff);
        Utils.drawStringCentered("R to Reset - Arrow keys/mouse to move", M.C.textRenderer, scaledResolution.getWidth() / 2, 18, true, 0xffffff);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            Window scaledResolution;
            if (guiScaleOverride >= 0) {
                scaledResolution = Utils.pushGuiScale(guiScaleOverride);
            } else {
                scaledResolution = new Window(M.C);
            }
            mouseX = Mouse.getX() * width / M.C.width;
            mouseY = height - Mouse.getY() * height / M.C.height - 1;

            int x = position.getAbsX(scaledResolution, elementWidth);
            int y = position.getAbsY(scaledResolution, elementHeight);
            if (position.isCenterX()) x -= elementWidth / 2;
            if (position.isCenterY()) y -= elementHeight / 2;

            if (mouseX >= x && mouseY >= y && mouseX <= x + elementWidth && mouseY <= y + elementHeight) {
                clicked = true;
                grabbedX = mouseX;
                grabbedY = mouseY;
            }

            if (guiScaleOverride >= 0) {
                Utils.pushGuiScale(-1);
            }
        }
    }

    @Override
    protected void keyPressed(char typedChar, int keyCode) {
        Keyboard.enableRepeatEvents(true);

        if (keyCode == Keyboard.KEY_R) {
            position.set(originalPosition);
        } else if (!clicked) {
            boolean shiftHeld = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
            int dist = shiftHeld ? 10 : 1;
            if (keyCode == Keyboard.KEY_DOWN) {
                position.moveY(dist, elementHeight, new Window(M.C));
            } else if (keyCode == Keyboard.KEY_UP) {
                position.moveY(-dist, elementHeight, new Window(M.C));
            } else if (keyCode == Keyboard.KEY_LEFT) {
                position.moveX(-dist, elementWidth, new Window(M.C));
            } else if (keyCode == Keyboard.KEY_RIGHT) {
                position.moveX(dist, elementWidth, new Window(M.C));
            }
        }
        super.keyPressed(typedChar, keyCode);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        clicked = false;
    }

    @Override
    protected void mouseDragged(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseDragged(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        if (clicked) {
            Window scaledResolution;
            if (guiScaleOverride >= 0) {
                scaledResolution = Utils.pushGuiScale(guiScaleOverride);
            } else {
                scaledResolution = new Window(M.C);
            }
            mouseX = Mouse.getX() * width / M.C.width;
            mouseY = height - Mouse.getY() * height / M.C.height - 1;

            grabbedX += position.moveX(mouseX - grabbedX, elementWidth, scaledResolution);
            grabbedY += position.moveY(mouseY - grabbedY, elementHeight, scaledResolution);
            positionChangedCallback.run();

            if (guiScaleOverride >= 0) {
                Utils.pushGuiScale(-1);
            }
        }
    }
}
