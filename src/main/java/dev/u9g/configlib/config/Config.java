package dev.u9g.configlib.config;

import net.minecraft.util.Identifier;

public interface Config {
    void executeRunnable(String runnableId);
    String getHeaderText();
    void save();
    Badge[] getBadges();

    final class Badge {
        public final Identifier icon;
        public final String url;

        public Badge(Identifier icon, String url) {
            this.icon = icon;
            this.url = url;
        }
    }
}
