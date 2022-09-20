package net.fabricmc.example.util.render;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.example.M;
import net.fabricmc.example.util.structs.FogColors;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gui.DrawableHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BackgroundBlur {

    private static class OutputStuff {

        public Framebuffer framebuffer;
        public PostProcessShader blurShaderHorz = null;
        public PostProcessShader blurShaderVert = null;

        public OutputStuff(Framebuffer framebuffer, PostProcessShader blurShaderHorz, PostProcessShader blurShaderVert) {
            this.framebuffer = framebuffer;
            this.blurShaderHorz = blurShaderHorz;
            this.blurShaderVert = blurShaderVert;
        }
    }

    private static final HashMap<Float, OutputStuff> blurOutput = new HashMap<>();
    private static final HashMap<Float, Long> lastBlurUse = new HashMap<>();
    private static long lastBlur = 0;
    private static final HashSet<Float> requestedBlurs = new HashSet<>();

    private static int fogColour = 0;
    private static boolean registered = false;

//    public static void registerListener() {
//        if (!registered) {
//            registered = true;
//            MinecraftForge.EVENT_BUS.register(new BackgroundBlur());
//        }
//    }

    private static boolean shouldBlur = true;

    public static void markDirty() {
        if (M.C.world != null) {
            shouldBlur = true;
        }
    }

    public static void processBlurs() {
        if (shouldBlur) {
            shouldBlur = false;

            long currentTime = System.currentTimeMillis();

            for (float blur : requestedBlurs) {
                lastBlur = currentTime;
                lastBlurUse.put(blur, currentTime);

                int width = M.C.width;
                int height = M.C.height;

                OutputStuff output = blurOutput.computeIfAbsent(
                    blur,
                    k -> {
                        Framebuffer fb = new Framebuffer(width, height, false);
                        fb.setTexFilter(GL11.GL_NEAREST);
                        return new OutputStuff(fb, null, null);
                    }
                );

                if (output.framebuffer.viewportWidth != width || output.framebuffer.viewportHeight != height) {
                    output.framebuffer.resize(width, height);
                    if (output.blurShaderHorz != null) {
                        output.blurShaderHorz.method_10310(createProjectionMatrix(width, height));
                    }
                    if (output.blurShaderVert != null) {
                        output.blurShaderVert.method_10310(createProjectionMatrix(width, height));
                    }
                }

                blurBackground(output, blur);
            }

            Set<Float> remove = new HashSet<>();
            for (Map.Entry<Float, Long> entry : lastBlurUse.entrySet()) {
                if (currentTime - entry.getValue() > 30 * 1000) {
                    remove.add(entry.getKey());
                }
            }
            remove.remove(5f);

            lastBlurUse.keySet().removeAll(remove);
            blurOutput.keySet().removeAll(remove);

            requestedBlurs.clear();
        }
    }

    public static void onScreenRender() {
        processBlurs();
    }

    public static void onFogColour(FogColors event) {
        fogColour = 0xff000000;
        fogColour |= ((int) (event.red * 255) & 0xFF) << 16;
        fogColour |= ((int) (event.green * 255) & 0xFF) << 8;
        fogColour |= (int) (event.blue * 255) & 0xFF;
    }

    private static Framebuffer blurOutputHorz = null;

    /**
     * Creates a projection matrix that projects from our coordinate space [0->width; 0->height] to OpenGL coordinate
     * space [-1 -> 1; 1 -> -1] (Note: flipped y-axis).
     *
     * This is so that we can render to and from the framebuffer in a way that is familiar to us, instead of needing to
     * apply scales and translations manually.
     */
    private static Matrix4f createProjectionMatrix(int width, int height) {
        Matrix4f projMatrix = new Matrix4f();
        projMatrix.setIdentity();
        projMatrix.m00 = 2.0F / (float) width;
        projMatrix.m11 = 2.0F / (float) (-height);
        projMatrix.m22 = -0.0020001999F;
        projMatrix.m33 = 1.0F;
        projMatrix.m03 = -1.0F;
        projMatrix.m13 = 1.0F;
        projMatrix.m23 = -1.0001999F;
        return projMatrix;
    }

    private static void blurBackground(OutputStuff output, float blurFactor) {
        if (!GLX.supportsFbo() || !GLX.areShadersSupported()) return;

        int width = M.C.width;
        int height = M.C.height;

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translatef(0.0F, 0.0F, -2000.0F);

        if (blurOutputHorz == null) {
            blurOutputHorz = new Framebuffer(width, height, false);
            blurOutputHorz.setTexFilter(GL11.GL_NEAREST);
        }
        if (blurOutputHorz == null || output == null) {
            return;
        }
        if (blurOutputHorz.viewportWidth != width || blurOutputHorz.viewportHeight != height) {
            blurOutputHorz.resize(width, height);
            M.C.getFramebuffer().bind(false);
        }

        if (output.blurShaderHorz == null) {
            try {
                output.blurShaderHorz = new PostProcessShader(M.C.getResourceManager(), "blur", output.framebuffer, blurOutputHorz);
                output.blurShaderHorz.getProgram().getUniformByName("BlurDir").method_6977/*set*/(1, 0);
                output.blurShaderHorz.method_10310(createProjectionMatrix(width, height));
            } catch (Exception ignored) {}
        }
        if (output.blurShaderVert == null) {
            try {
                output.blurShaderVert = new PostProcessShader(M.C.getResourceManager(), "blur", blurOutputHorz, output.framebuffer);
                output.blurShaderVert.getProgram().getUniformByName("BlurDir").method_6977/*set*/(0, 1);
                output.blurShaderVert.method_10310(createProjectionMatrix(width, height));
            } catch (Exception ignored) {}
        }
        if (output.blurShaderHorz != null && output.blurShaderVert != null) {
            if (output.blurShaderHorz.getProgram().getUniformByName("Radius") == null) {
                //Corrupted shader?
                return;
            }

            output.blurShaderHorz.getProgram().getUniformByName("Radius").method_6976/*set*/(blurFactor);
            output.blurShaderVert.getProgram().getUniformByName("Radius").method_6976/*set*/(blurFactor);

            GL11.glPushMatrix();
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, M.C.getFramebuffer().fbo);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, output.framebuffer.fbo);
            GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, output.framebuffer.viewportWidth, output.framebuffer.viewportHeight, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);

            output.blurShaderHorz.render(0);
            output.blurShaderVert.render(0);
            GlStateManager.enableDepthTest();
            GL11.glPopMatrix();

            M.C.getFramebuffer().bind(false);
        }
    }

    public static void renderBlurredBackground(float blurStrength, int screenWidth, int screenHeight, int x, int y, int blurWidth, int blurHeight) {
        renderBlurredBackground(blurStrength, screenWidth, screenHeight, x, y, blurWidth, blurHeight, false);
    }

    /**
     * Renders a subsection of the blurred framebuffer on to the corresponding section of the screen.
     * Essentially, this method will "blur" the background inside the bounds specified by [x->x+blurWidth, y->y+blurHeight]
     */
    public static void renderBlurredBackground(float blurStrength, int screenWidth, int screenHeight, int x, int y, int blurWidth, int blurHeight, boolean forcedUpdate) {
        if (!GLX.supportsFbo() || !GLX.areShadersSupported()) return;
        if (blurStrength < 0.5) return;
        requestedBlurs.add(blurStrength);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBlur > 300) {
            shouldBlur = true;
            if (currentTime - lastBlur > 400 && forcedUpdate) return;
        }

        if (blurOutput.isEmpty()) return;

        OutputStuff out = blurOutput.get(blurStrength);
        if (out == null) {
            out = blurOutput.values().iterator().next();
        }

        float uMin = x / (float) screenWidth;
        float uMax = (x + blurWidth) / (float) screenWidth;
        float vMin = (screenHeight - y) / (float) screenHeight;
        float vMax = (screenHeight - y - blurHeight) / (float) screenHeight;

        GlStateManager.depthMask(false);
        DrawableHelper.fill(x, y, x + blurWidth, y + blurHeight, fogColour);
        out.framebuffer.beginRead();
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        RenderUtils.drawTexturedRect(x, y, blurWidth, blurHeight, uMin, uMax, vMin, vMax);
        out.framebuffer.endRead();
        GlStateManager.depthMask(true);
    }
}