package dev.u9g.configlib.config;

import net.minecraft.util.ResourceLocation;

public interface Config {
    void executeRunnable(String runnableId);
    String getHeaderText();
    void save();
    Badge[] getBadges();

    final class Badge {
        public final ResourceLocation icon;
        public final String url;

        public Badge(ResourceLocation icon, String url) {
            this.icon = icon;
            this.url = url;
        }
    }
}
