package net.fabricmc.example.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.example.M;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.math.RoundingMode;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;

public class Utils {

    private static LinkedList<Integer> guiScales = new LinkedList<>();
    private static Window lastScale = new Window(M.C);
    //Labymod compatibility
    private static FloatBuffer projectionMatrixOld = BufferUtils.createFloatBuffer(16);
    private static FloatBuffer modelviewMatrixOld = BufferUtils.createFloatBuffer(16);

    public static String removeColor(String input) {
        return input.replaceAll("(?i)\\u00A7.", "");
    }

    public static String removeWhiteSpaceAndRemoveWord(String input, String replace) {
        return input.toLowerCase().replace(" ", "").replace(replace, "");
    }

//    public static boolean isPlayerHoldingRedstone(EntityPlayerSP player) {
//        if (!SkyblockHud.config.main.requireRedstone) return true;
//        ArrayList<Item> redstoneItems = new ArrayList<>(Arrays.asList(Items.redstone, Items.repeater, Items.comparator, Item.getByNameOrId("minecraft:redstone_torch")));
//        if (player.getHeldItem() != null) return redstoneItems.contains(player.getHeldItem().getItem());
//        return false;
//    }

    public static boolean inRangeInclusive(int value, int min, int max) {
        return value <= max && value >= min;
    }

    public static float lerp(float f, float g, float h) {
        return g + f * (h - g);
    }

    public static double lerp(double d, double e, double f) {
        return e + d * (f - e);
    }

    public static int lerp(float f, int g, int h) {
        return (int) (g + f * (h - g));
    }

    public static NbtCompound getSkyBlockTag(ItemStack stack) {
        if (stack == null) return null;
        if (!stack.hasNbt()) return null;
        if (!stack.getNbt().contains("ExtraAttributes")) return null;
        return stack.getNbt().getCompound("ExtraAttributes");
    }

    public static boolean isDrill(ItemStack stack) {
        NbtCompound tag = getSkyBlockTag(stack);
        return tag != null && tag.contains("drill_fuel");
    }

    public static int whatRomanNumeral(String roman) {
        switch (roman.toLowerCase()) {
            case "i":
                return 1;
            case "ii":
                return 2;
            case "iii":
                return 3;
            case "iv":
                return 4;
            case "v":
                return 5;
            case "vi":
                return 6;
            case "vii":
                return 7;
            case "viii":
                return 8;
            case "ix":
                return 9;
            case "x":
                return 10;
            default:
                return 0;
        }
    }

    public static String intToRomanNumeral(int i) {
        switch (i) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return "";
        }
    }

    /**
     * From https://stackoverflow.com/questions/10711494/get-values-in-treemap-whose-string-keys-start-with-a-pattern
     */
    public static <T> Map<String, T> subMapWithKeysThatAreSuffixes(String prefix, NavigableMap<String, T> map) {
        if ("".equals(prefix)) return map;
        String lastKey = createLexicographicallyNextStringOfTheSameLength(prefix);
        return map.subMap(prefix, true, lastKey, false);
    }

    public static String createLexicographicallyNextStringOfTheSameLength(String input) {
        final int lastCharPosition = input.length() - 1;
        String inputWithoutLastChar = input.substring(0, lastCharPosition);
        char lastChar = input.charAt(lastCharPosition);
        char incrementedLastChar = (char) (lastChar + 1);
        return inputWithoutLastChar + incrementedLastChar;
    }

//    public static boolean overlayShouldRender(RenderGameOverlayEvent.ElementType type, boolean... booleans) {
//        return overlayShouldRender(false, type, RenderGameOverlayEvent.ElementType.HOTBAR, booleans);
//    }
//
//    public static boolean overlayShouldRender(boolean hideOnf3, RenderGameOverlayEvent.ElementType type, RenderGameOverlayEvent.ElementType checkType, boolean... booleans) {
//        MinecraftClient mc = M.C;
//        for (boolean aBoolean : booleans) if (!aBoolean) return false;
//        if (hideOnf3) {
//            if (mc.options.debugEnabled || (mc.options.keyPlayerList.isPressed() && (!mc.isIntegratedServerRunning() || mc.player.networkHandler.getPlayerList().size() > 1))) {
//                return false;
//            }
//        }
//        return ((type == null && false/*Loader.isModLoaded("labymod")*/) || type == checkType); // TODO: fix labymod check
//    }

    public static void drawStringScaledMaxWidth(String str, TextRenderer fr, float x, float y, boolean shadow, int len, int colour) {
        int strLen = fr.getStringWidth(str);
        float factor = len / (float) strLen;
        factor = Math.min(1, factor);

        drawStringScaled(str, fr, x, y, shadow, colour, factor);
    }

    public static void drawStringScaled(String str, TextRenderer fr, float x, float y, boolean shadow, int colour, float factor) {
        GlStateManager.scalef(factor, factor, 1);
        fr.draw(str, x / factor, y / factor, colour, shadow);
        GlStateManager.scalef(1 / factor, 1 / factor, 1);
    }

    public static void drawStringCenteredScaled(String str, TextRenderer fr, float x, float y, boolean shadow, int len, int colour) {
        int strLen = fr.getStringWidth(str);
        float factor = len / (float) strLen;
        float fontHeight = 8 * factor;

        drawStringScaled(str, fr, x - len / 2f, y - fontHeight / 2f, shadow, colour, factor);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax, int filter) {
        GlStateManager.enableTexture();
        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, VertexFormats.POSITION_TEXTURE);
        worldrenderer.vertex(x, y + height, 0.0D).texture(uMin, vMax).next();
        worldrenderer.vertex(x + width, y + height, 0.0D).texture(uMax, vMax).next();
        worldrenderer.vertex(x + width, y, 0.0D).texture(uMax, vMin).next();
        worldrenderer.vertex(x, y, 0.0D).texture(uMin, vMin).next();
        tessellator.draw();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GlStateManager.disableBlend();
    }

    public static void drawTexturedRect(float x, float y, float width, float height) {
        drawTexturedRect(x, y, width, height, 0, 1, 0, 1);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, int filter) {
        drawTexturedRect(x, y, width, height, 0, 1, 0, 1, filter);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax) {
        drawTexturedRect(x, y, width, height, uMin, uMax, vMin, vMax, GL11.GL_LINEAR);
    }

    public static void resetGuiScale() {
        guiScales.clear();
    }

    public static Window peekGuiScale() {
        return lastScale;
    }

    public static Window pushGuiScale(int scale) {
        if (guiScales.size() == 0) {
            if (/*Loader.isModLoaded("labymod")*/false) { // Fix labymod detection
                GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrixOld);
                GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelviewMatrixOld);
            }
        }

        if (scale < 0) {
            if (guiScales.size() > 0) {
                guiScales.pop();
            }
        } else {
            if (scale == 0) {
                guiScales.push(M.C.options.guiScale);
            } else {
                guiScales.push(scale);
            }
        }

        int newScale = guiScales.size() > 0 ? Math.max(0, Math.min(4, guiScales.peek())) : M.C.options.guiScale;
        if (newScale == 0) newScale = M.C.options.guiScale;

        int oldScale = M.C.options.guiScale;
        M.C.options.guiScale = newScale;
        Window scaledresolution = new Window(M.C);
        M.C.options.guiScale = oldScale;

        if (guiScales.size() > 0) {
            GlStateManager.viewPort(0, 0, M.C.width, M.C.height);
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
        } else {
            if (/*Loader.isModLoaded("labymod")*/false && projectionMatrixOld.limit() > 0 && modelviewMatrixOld.limit() > 0) { // todo: fix labymod detection
                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GL11.glLoadMatrix(projectionMatrixOld);
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadMatrix(modelviewMatrixOld);
            } else {
                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.loadIdentity();
                GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
            }
        }

        lastScale = scaledresolution;
        return scaledresolution;
    }

    public static void drawStringCentered(String str, TextRenderer fr, float x, float y, boolean shadow, int colour) {
        int strLen = fr.getStringWidth(str);

        float x2 = x - strLen / 2f;
        float y2 = y - fr.fontHeight / 2f;

        GL11.glTranslatef(x2, y2, 0);
        fr.draw(str, 0, 0, colour, shadow);
        GL11.glTranslatef(-x2, -y2, 0);
    }

    public static void renderWaypointText(String str, BlockPos loc, float partialTicks) {
        GlStateManager.alphaFunc(516, 0.1F);

        GlStateManager.pushMatrix();

        Entity viewer = M.C.getCameraEntity();
        double viewerX = viewer.prevTickX + (viewer.x - viewer.prevTickX) * partialTicks;
        double viewerY = viewer.prevTickY + (viewer.y - viewer.prevTickY) * partialTicks;
        double viewerZ = viewer.prevTickZ + (viewer.z - viewer.prevTickZ) * partialTicks;

        double x = loc.getX() - viewerX;
        double y = loc.getY() - viewerY - viewer.getEyeHeight();
        double z = loc.getZ() - viewerZ;

        double distSq = x * x + y * y + z * z;
        double dist = Math.sqrt(distSq);
        if (distSq > 144) {
            x *= 12 / dist;
            y *= 12 / dist;
            z *= 12 / dist;
        }
        GlStateManager.translated(x, y, z);
        GlStateManager.translatef(0, viewer.getEyeHeight(), 0);

        drawNametag(str);

        GlStateManager.rotatef(-M.C.getEntityRenderManager().yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(M.C.getEntityRenderManager().pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.translatef(0, -0.25f, 0);
        GlStateManager.rotatef(-M.C.getEntityRenderManager().pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(M.C.getEntityRenderManager().yaw, 0.0F, 1.0F, 0.0F);

        drawNametag(Formatting.YELLOW.toString() + Math.round(dist) + "m");

        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
    }

    public static void drawNametag(String str) {
        TextRenderer fontrenderer = M.C.textRenderer;
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GlStateManager.pushMatrix();
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-M.C.getEntityRenderManager().yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(M.C.getEntityRenderManager().pitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scalef(-f1, -f1, f1);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepthTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        int i = 0;

        int j = fontrenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture();
        worldrenderer.begin(7, VertexFormats.POSITION_COLOR);
        worldrenderer.vertex(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).next();
        worldrenderer.vertex(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).next();
        worldrenderer.vertex(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).next();
        worldrenderer.vertex(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).next();
        tessellator.draw();
        GlStateManager.enableTexture();
        fontrenderer.draw(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
        GlStateManager.depthMask(true);

        fontrenderer.draw(str, -fontrenderer.getStringWidth(str) / 2, i, -1);

        GlStateManager.enableDepthTest();
        GlStateManager.enableBlend();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static String formattedNumber(int number, int numberToFormatAt) {
        DecimalFormat formatter = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.CANADA));
        formatter.setRoundingMode(RoundingMode.FLOOR);
        return number > numberToFormatAt - 1 ? formatter.format((double) number / 1000) + "k" : String.valueOf(number);
    }

    public static boolean equalsIgnoreCaseAnyOf(String string, String... strings) {
        for (String o : strings) if (string.equalsIgnoreCase(o)) return true;
        return false;
    }

    public static String getItemCustomId(ItemStack stack) {
        if (stack == null) return null;
        if (!stack.hasNbt()) return null;
        if (!stack.getNbt().contains("ExtraAttributes")) return null;
        if (!stack.getNbt().getCompound("ExtraAttributes").contains("id")) return null;
        return stack.getNbt().getCompound("ExtraAttributes").getString("id");
    }
}
