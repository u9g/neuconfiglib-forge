package dev.u9g.configlib.forge;

import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.resource.ResourceManager;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DynamicTexture
extends AbstractTexture {
    private final int[] dynamicTextureData;
    /**
     * width of this icon in pixels
     */
    private final int width;
    /**
     * height of this icon in pixels
     */
    private final int height;

    public DynamicTexture(BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
        this.updateDynamicTexture();
    }

    public DynamicTexture(int i, int j) {
        this.width = i;
        this.height = j;
        this.dynamicTextureData = new int[i * j];
        TextureUtil.prepareImage(this.getGlId(), i, j);
    }

    @Override
    public void load(ResourceManager manager) throws IOException {

    }

    public void updateDynamicTexture() {
        TextureUtil.method_5861(this.getGlId(), this.dynamicTextureData, this.width, this.height);
    }

    public int[] getTextureData() {
        return this.dynamicTextureData;
    }
}