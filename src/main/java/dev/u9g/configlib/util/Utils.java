package dev.u9g.configlib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import dev.u9g.configlib.M;
import dev.u9g.configlib.util.lerp.LerpingFloat;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.math.RoundingMode;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static LinkedList<Integer> guiScales = new LinkedList<>();
    private static ScaledResolution lastScale = new ScaledResolution(M.C);
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

    public static void drawStringScaledMaxWidth(String str, FontRenderer fr, float x, float y, boolean shadow, int len, int colour) {
        int strLen = fr.getStringWidth(str);
        float factor = len / (float) strLen;
        factor = Math.min(1, factor);

        drawStringScaled(str, fr, x, y, shadow, colour, factor);
    }

    public static void drawStringScaled(String str, FontRenderer fr, float x, float y, boolean shadow, int colour, float factor) {
        GlStateManager.scale(factor, factor, 1);
        fr.drawString(str, x / factor, y / factor, colour, shadow);
        GlStateManager.scale(1 / factor, 1 / factor, 1);
    }

    public static void drawStringCenteredScaled(String str, FontRenderer fr, float x, float y, boolean shadow, int len, int colour) {
        int strLen = fr.getStringWidth(str);
        float factor = len / (float) strLen;
        float fontHeight = 8 * factor;

        drawStringScaled(str, fr, x - len / 2f, y - fontHeight / 2f, shadow, colour, factor);
    }

    public static void drawTexturedRect(float x, float y, float width, float height, float uMin, float uMax, float vMin, float vMax, int filter) {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(uMin, vMax).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex(uMax, vMax).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex(uMax, vMin).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(uMin, vMin).endVertex();
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

    public static ScaledResolution peekGuiScale() {
        return lastScale;
    }

    public static ScaledResolution pushGuiScale(int scale) {
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
                guiScales.push(M.C.gameSettings.guiScale);
            } else {
                guiScales.push(scale);
            }
        }

        int newScale = guiScales.size() > 0 ? Math.max(0, Math.min(4, guiScales.peek())) : M.C.gameSettings.guiScale;
        if (newScale == 0) newScale = M.C.gameSettings.guiScale;

        int oldScale = M.C.gameSettings.guiScale;
        M.C.gameSettings.guiScale = newScale;
        ScaledResolution scaledresolution = new ScaledResolution(M.C);
        M.C.gameSettings.guiScale = oldScale;

        if (guiScales.size() > 0) {
            GlStateManager.viewport(0, 0, M.C.displayWidth, M.C.displayHeight);
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        } else {
            if (Loader.isModLoaded("labymod") && projectionMatrixOld.limit() > 0 && modelviewMatrixOld.limit() > 0) {
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
                GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            }
        }

        lastScale = scaledresolution;
        return scaledresolution;
    }

    public static void drawStringCentered(String str, FontRenderer fr, float x, float y, boolean shadow, int colour) {
        int strLen = fr.getStringWidth(str);

        float x2 = x - strLen / 2f;
        float y2 = y - fr.FONT_HEIGHT / 2f;

        GL11.glTranslatef(x2, y2, 0);
        fr.drawString(str, 0, 0, colour, shadow);
        GL11.glTranslatef(-x2, -y2, 0);
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

    public static boolean hasEffectOverride = false;
    public static boolean disableCustomDungColours = false;

    /**
     * Removes colorcodes
     * @param in
     * @return
     */
    public static String cleanColour(String in) {
        return in.replaceAll("(?i)\\u00A7.", "");
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawTexturedRectNoBlend(
            float x,
            float y,
            float width,
            float height,
            float uMin,
            float uMax,
            float vMin,
            float vMax,
            int filter
    ) {
        GlStateManager.enableTexture2D();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer
                .pos(x, y + height, 0.0D)
                .tex(uMin, vMax).endVertex();
        worldrenderer
                .pos(x + width, y + height, 0.0D)
                .tex(uMax, vMax).endVertex();
        worldrenderer
                .pos(x + width, y, 0.0D)
                .tex(uMax, vMin).endVertex();
        worldrenderer
                .pos(x, y, 0.0D)
                .tex(uMin, vMin).endVertex();
        tessellator.draw();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    }

//    /**
//     * Searches a string for a query. This method is used to mimic the behaviour of the more complex map-based search
//     * function. This method is used for the chest-item-search feature.
//     */
//    public static boolean searchString(String toSearch, String query) {
//        int lastMatch = -1;
//
//        toSearch = clean(toSearch).toLowerCase();
//        query = clean(query).toLowerCase();
//        String[] splitToSeach = toSearch.split(" ");
//        out:
//        for (String s : query.split(" ")) {
//            for (int i = 0; i < splitToSeach.length; i++) {
//                if (!(lastMatch == -1 || lastMatch == i - 1)) continue;
//                if (splitToSeach[i].startsWith(s)) {
//                    lastMatch = i;
//                    continue out;
//                }
//            }
//            return false;
//        }
//
//        return true;
//    }

    private static class DebugMatch {
        int index;
        String match;

        DebugMatch(int index, String match) {
            this.index = index;
            this.match = match;
        }
    }

    public static boolean searchString(String toSearch, String query) {
        int lastStringMatch = -1;
        ArrayList<DebugMatch> debugMatches = new ArrayList<>();

        toSearch = clean(toSearch).toLowerCase();
        query = clean(query).toLowerCase();
        String[] splitToSearch = toSearch.split(" ");
        String[] queryArray = query.split(" ");

        {
            String currentSearch = queryArray[0];
            int queryIndex = 0;
            boolean matchedLastQueryItem = false;

            for (int k = 0; k < splitToSearch.length; k++) {
                if (queryIndex - 1 != -1 && (queryArray.length - queryIndex) > (splitToSearch.length - k)) continue;
                if (splitToSearch[k].startsWith(currentSearch)) {
                    if (((lastStringMatch != -1 ? lastStringMatch : k-1) == k-1)) {
                        debugMatches.add(new DebugMatch(k, currentSearch));
                        lastStringMatch = k;
                        if (queryIndex+1 != queryArray.length) {
                            queryIndex++;
                            currentSearch = queryArray[queryIndex];
                        } else {
                            matchedLastQueryItem = true;
                        }
                    }
                } else if (queryIndex != 0) {
                    queryIndex = 0;
                    currentSearch = queryArray[queryIndex];
                    lastStringMatch = -1;
                }
            }

            return matchedLastQueryItem;
        }
    }


    private static String clean(String str) {
        return str.replaceAll("(\u00a7.)|[^0-9a-zA-Z ]", "").toLowerCase().trim();
    }

    public static void drawItemStack(ItemStack stack, int x, int y) {
        drawItemStackWithText(stack, x, y, null);
    }

    public static void drawItemStackWithText(ItemStack stack, int x, int y, String text) {
        if (stack == null) return;
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

        disableCustomDungColours = true;
        RenderHelper.enableGUIStandardItemLighting();
        itemRender.zLevel = -145; //Negates the z-offset of the below method.
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, stack, x, y, text);
        itemRender.zLevel = 0;
        RenderHelper.disableStandardItemLighting();
        disableCustomDungColours = false;
    }

    public static void drawHoveringText(
            List<String> textLines,
            final int mouseX,
            final int mouseY,
            final int screenWidth,
            final int screenHeight,
            final int maxTextWidth,
            FontRenderer font,
            boolean tooltipBorderColours,
            int tooltipBorderOpacity
    ) {
        drawHoveringText(textLines, mouseX, mouseY, screenWidth, screenHeight, maxTextWidth, font, true, tooltipBorderColours, tooltipBorderOpacity);
    }

    private static final LerpingFloat scrollY = new LerpingFloat(0, 100);

    public static void drawHoveringText(
            List<String> textLines,
            final int mouseX,
            final int mouseY,
            final int screenWidth,
            final int screenHeight,
            final int maxTextWidth,
            FontRenderer font,
            boolean coloured,
            boolean tooltipBorderColours,
            int tooltipBorderOpacity
    ) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2) {
                        tooltipTextWidth = mouseX - 12 - 8;
                    } else {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap) {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<>();
                for (int i = 0; i < textLines.size(); i++) {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine) {
                        int lineWidth = font.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2) {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                } else {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2; // gap between title lines and endVertex lines
                }
            }

            //Scrollable tooltips
            if (tooltipHeight + 6 > screenHeight) {
                if (scrollY.getTarget() < 0) {
                    scrollY.setTarget(0);
                    scrollY.resetTimer();
                } else if (screenHeight - tooltipHeight - 12 + (int) scrollY.getTarget() > 0) {
                    scrollY.setTarget(-screenHeight + tooltipHeight + 12);
                    scrollY.resetTimer();
                }
            } else {
                scrollY.setValue(0);
                scrollY.resetTimer();
            }
            scrollY.tick();

            if (tooltipY + tooltipHeight + 6 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 6 + (int) scrollY.getValue();
            }

            final int zLevel = 300;
            final int backgroundColor = 0xF0100010;
            drawGradientRect(
                    zLevel,
                    tooltipX - 3,
                    tooltipY - 4,
                    tooltipX + tooltipTextWidth + 3,
                    tooltipY - 3,
                    backgroundColor,
                    backgroundColor
            );
            drawGradientRect(
                    zLevel,
                    tooltipX - 3,
                    tooltipY + tooltipHeight + 3,
                    tooltipX + tooltipTextWidth + 3,
                    tooltipY + tooltipHeight + 4,
                    backgroundColor,
                    backgroundColor
            );
            drawGradientRect(
                    zLevel,
                    tooltipX - 3,
                    tooltipY - 3,
                    tooltipX + tooltipTextWidth + 3,
                    tooltipY + tooltipHeight + 3,
                    backgroundColor,
                    backgroundColor
            );
            drawGradientRect(
                    zLevel,
                    tooltipX - 4,
                    tooltipY - 3,
                    tooltipX - 3,
                    tooltipY + tooltipHeight + 3,
                    backgroundColor,
                    backgroundColor
            );
            drawGradientRect(
                    zLevel,
                    tooltipX + tooltipTextWidth + 3,
                    tooltipY - 3,
                    tooltipX + tooltipTextWidth + 4,
                    tooltipY + tooltipHeight + 3,
                    backgroundColor,
                    backgroundColor
            );
            //TODO: Coloured Borders
            int borderColorStart = 0x505000FF;
            if (tooltipBorderColours && coloured) {
                if (textLines.size() > 0) {
                    String first = textLines.get(0);
                    // TODO: FIXME
                    borderColorStart = /*getPrimaryColour(first)*/0x55FFFF/*aqua*/ & 0x00FFFFFF |
                            ((tooltipBorderOpacity) << 24);
                }
            }
            final int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            drawGradientRect(
                    zLevel,
                    tooltipX - 3,
                    tooltipY - 3 + 1,
                    tooltipX - 3 + 1,
                    tooltipY + tooltipHeight + 3 - 1,
                    borderColorStart,
                    borderColorEnd
            );
            drawGradientRect(
                    zLevel,
                    tooltipX + tooltipTextWidth + 2,
                    tooltipY - 3 + 1,
                    tooltipX + tooltipTextWidth + 3,
                    tooltipY + tooltipHeight + 3 - 1,
                    borderColorStart,
                    borderColorEnd
            );
            drawGradientRect(
                    zLevel,
                    tooltipX - 3,
                    tooltipY - 3,
                    tooltipX + tooltipTextWidth + 3,
                    tooltipY - 3 + 1,
                    borderColorStart,
                    borderColorStart
            );
            drawGradientRect(
                    zLevel,
                    tooltipX - 3,
                    tooltipY + tooltipHeight + 2,
                    tooltipX + tooltipTextWidth + 3,
                    tooltipY + tooltipHeight + 3,
                    borderColorEnd,
                    borderColorEnd
            );

            GlStateManager.disableDepth();
            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
        GlStateManager.disableLighting();
    }

    public static void drawGradientRect(
            int zLevel,
            int left,
            int top,
            int right,
            int bottom,
            int startColor,
            int endColor
    ) {
        float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
        float startRed = (float) (startColor >> 16 & 255) / 255.0F;
        float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
        float startBlue = (float) (startColor & 255) / 255.0F;
        float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
        float endRed = (float) (endColor >> 16 & 255) / 255.0F;
        float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
        float endBlue = (float) (endColor & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        worldrenderer.pos(left, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        worldrenderer.pos(left, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        worldrenderer.pos(right, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    private static final Pattern CHROMA_REPLACE_PATTERN = Pattern.compile("\u00a7z(.+?)(?=\u00a7|$)");
    public static String chromaString(String str, int chromaSpeed) {
        return chromaString(str, 0, false, chromaSpeed);
    }
    private static long startTime = 0;
    private static final EnumChatFormatting[] rainbow = new EnumChatFormatting[]{
            EnumChatFormatting.RED,
            EnumChatFormatting.GOLD,
            EnumChatFormatting.YELLOW,
            EnumChatFormatting.GREEN,
            EnumChatFormatting.AQUA,
            EnumChatFormatting.LIGHT_PURPLE,
            EnumChatFormatting.DARK_PURPLE
    };

    public static String chromaString(String str, float offset, boolean bold, int chromaSpeed) {
        str = cleanColour(str);

        long currentTimeMillis = System.currentTimeMillis();
        if (startTime == 0) startTime = currentTimeMillis;

        if (chromaSpeed < 10) chromaSpeed = 10;
        if (chromaSpeed > 5000) chromaSpeed = 5000;

        StringBuilder rainbowText = new StringBuilder();
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int index = ((int) (offset + len / 12f - (currentTimeMillis - startTime) / chromaSpeed)) % rainbow.length;
            len += M.C.fontRendererObj.getCharWidth(c);
            if (bold) len++;

            if (index < 0) index += rainbow.length;
            rainbowText.append(rainbow[index]);
            if (bold) rainbowText.append(EnumChatFormatting.BOLD);
            rainbowText.append(c);
        }
        return rainbowText.toString();
    }

    public static String chromaStringByColourCode(String str, int chromaSpeed) {
        if (str.contains("\u00a7z")) {
            Matcher matcher = CHROMA_REPLACE_PATTERN.matcher(str);

            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                matcher.appendReplacement(
                        sb,
                        Utils.chromaString(matcher.group(1), chromaSpeed)
                                .replace("\\", "\\\\")
                                .replace("$", "\\$")
                );
            }
            matcher.appendTail(sb);

            str = sb.toString();
        }
        return str;
    }
}
