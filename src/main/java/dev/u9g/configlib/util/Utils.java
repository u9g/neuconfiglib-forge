package dev.u9g.configlib.util;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.u9g.configlib.M;
import dev.u9g.configlib.util.lerp.LerpingFloat;
import net.minecraft.block.Block;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
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

    public static boolean hasEffectOverride = false;
    public static boolean disableCustomDungColours = false;

    public static void drawItemStackWithoutGlint(ItemStack stack, int x, int y) {
        ItemRenderer itemRender = M.C.getItemRenderer();

        disableCustomDungColours = true;
        DiffuseLighting.enable();
        itemRender.zOffset = -145; //Negates the z-offset of the below method.
        hasEffectOverride = true;
        try {
            itemRender.renderInGuiWithOverrides(stack, x, y);
        } catch (Exception e) {
            e.printStackTrace();
        } //Catch exceptions to ensure that hasEffectOverride is set back to false.
        itemRender.renderGuiItemOverlay(M.C.textRenderer, stack, x, y, null);
        hasEffectOverride = false;
        itemRender.zOffset = 0;
        DiffuseLighting.disable();
        disableCustomDungColours = false;
    }

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
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, VertexFormats.POSITION_COLOR);
        worldrenderer.vertex(right, top, 0).color(f1, f2, f3, f).next();
        worldrenderer.vertex(left, top, 0).color(f1, f2, f3, f).next();
        worldrenderer.vertex(left, bottom, 0).color(f5, f6, f7, f4).next();
        worldrenderer.vertex(right, bottom, 0).color(f5, f6, f7, f4).next();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
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
        GlStateManager.enableTexture();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filter);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, VertexFormats.POSITION_TEXTURE);
        worldrenderer
                .vertex(x, y + height, 0.0D)
                .texture(uMin, vMax).next();
        worldrenderer
                .vertex(x + width, y + height, 0.0D)
                .texture(uMax, vMax).next();
        worldrenderer
                .vertex(x + width, y, 0.0D)
                .texture(uMax, vMin).next();
        worldrenderer
                .vertex(x, y, 0.0D)
                .texture(uMin, vMin).next();
        tessellator.draw();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    }

    public static ItemStack createItemStack(Item item, String displayName, String... lore) {
        return createItemStack(item, displayName, 0, lore);
    }

    public static ItemStack createItemStack(Block item, String displayName, String... lore) {
        return createItemStack(Item.fromBlock(item), displayName, lore);
    }

    public static ItemStack createItemStack(Item item, String displayName, int damage, String... lore) {
        ItemStack stack = new ItemStack(item, 1, damage);
        NbtCompound tag = new NbtCompound();
        NbtCompound display = new NbtCompound();
        NbtList Lore = new NbtList();

        for (String line : lore) {
            Lore.add(new NbtString(line));
        }

        display.putString("Name", displayName);
        display.put("Lore", Lore);

        tag.put("display", display);
        tag.putInt("HideFlags", 254);

        stack.setNbt(tag);

        return stack;
    }

    public static boolean doesStackMatchSearch(ItemStack stack, String query) {
        if (query.startsWith("title:")) {
            query = query.substring(6);
            return searchString(stack.getName(), query);
        } else if (query.startsWith("desc:")) {
            query = query.substring(5);
            String lore = "";
            NbtCompound tag = stack.getNbt();
            if (tag != null) {
                NbtCompound display = tag.getCompound("display");
                if (display.contains("Lore", 9)) {
                    NbtList list = display.getList("Lore", 8);
                    for (int i = 0; i < list.size(); i++) {
                        lore += list.getString(i) + " ";
                    }
                }
            }
            return searchString(lore, query);
        }/* else if (*//*query.startsWith("id:")*//*false) {
//            query = query.substring(3);
            // TODO: do this with cosmic items?
//            String internalName = getInternalNameForItem(stack);
//            return query.equalsIgnoreCase(internalName);
        }*/ else {
            boolean result = false;
            if (!query.trim().contains(" ")) {
                StringBuilder sb = new StringBuilder();
                for (char c : query.toCharArray()) {
                    sb.append(c).append(" ");
                }
                result = result || searchString(stack.getName(), sb.toString());
            }
            result = result || searchString(stack.getName(), query);

            String lore = "";
            NbtCompound tag = stack.getNbt();
            if (tag != null) {
                NbtCompound display = tag.getCompound("display");
                if (display.contains("Lore", 9)) {
                    NbtList list = display.getList("Lore", 8);
                    for (int i = 0; i < list.size(); i++) {
                        lore += list.getString(i) + " ";
                    }
                }
            }

            result = result || searchString(lore, query);

            return result;
        }
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
        ItemRenderer itemRender = M.C.getItemRenderer();

        disableCustomDungColours = true;
        DiffuseLighting.enable();
        itemRender.zOffset = -145; //Negates the z-offset of the below method.
        itemRender.renderInGuiWithOverrides(stack, x, y);
        itemRender.renderGuiItemOverlay(M.C.textRenderer, stack, x, y, text);
        itemRender.zOffset = 0;
        DiffuseLighting.disable();
        disableCustomDungColours = false;
    }

    public static void drawHoveringText(
            List<String> textLines,
            final int mouseX,
            final int mouseY,
            final int screenWidth,
            final int screenHeight,
            final int maxTextWidth,
            TextRenderer font,
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
            TextRenderer font,
            boolean coloured,
            boolean tooltipBorderColours,
            int tooltipBorderOpacity
    ) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            DiffuseLighting.disable();
            GlStateManager.disableLighting();
            GlStateManager.enableDepthTest();
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
                    List<String> wrappedLine = font.wrapLines(textLine, tooltipTextWidth);
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
                    tooltipHeight += 2; // gap between title lines and next lines
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

            GlStateManager.disableDepthTest();
            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                font.drawWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GlStateManager.enableLighting();
            GlStateManager.enableDepthTest();
            DiffuseLighting.enableNormally();
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

        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, VertexFormats.POSITION_COLOR);
        worldrenderer.vertex(right, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).next();
        worldrenderer.vertex(left, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).next();
        worldrenderer.vertex(left, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).next();
        worldrenderer.vertex(right, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).next();
        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
    }

    private static final Pattern CHROMA_REPLACE_PATTERN = Pattern.compile("\u00a7z(.+?)(?=\u00a7|$)");
    public static String chromaString(String str, int chromaSpeed) {
        return chromaString(str, 0, false, chromaSpeed);
    }
    private static long startTime = 0;
    private static final Formatting[] rainbow = new Formatting[]{
            Formatting.RED,
            Formatting.GOLD,
            Formatting.YELLOW,
            Formatting.GREEN,
            Formatting.AQUA,
            Formatting.LIGHT_PURPLE,
            Formatting.DARK_PURPLE
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
            len += M.C.textRenderer.method_949(c);
            if (bold) len++;

            if (index < 0) index += rainbow.length;
            rainbowText.append(rainbow[index]);
            if (bold) rainbowText.append(Formatting.BOLD);
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
