package dev.u9g.configlib;

import com.google.gson.annotations.Expose;
import dev.u9g.configlib.config.GuiTextures;
import dev.u9g.configlib.config.MyModConfigEditor;
import dev.u9g.configlib.config.annotations.*;
import dev.u9g.configlib.config.Config;
import dev.u9g.configlib.config.ScreenElementWrapper;
import net.minecraft.util.EnumChatFormatting;

public class MyModConfig implements Config {
    public static MyModConfig INSTANCE = new MyModConfig();

    @Override
    public void executeRunnable(String runnableId) {
        String activeConfigCategory = null;
        if (M.C.currentScreen instanceof ScreenElementWrapper) {
            ScreenElementWrapper wrapper = (ScreenElementWrapper) M.C.currentScreen;
            if (wrapper.element instanceof MyModConfigEditor) {
                activeConfigCategory = ((MyModConfigEditor) wrapper.element).getSelectedCategoryName();
            }
        }
    }

    @Override
    public void save() {

    }

    @Override
    public Badge[] getBadges() {
        return new Badge[] { new Badge(GuiTextures.GITHUB, "https://github.com/u9g") };
    }

    @Override
    public String getHeaderText() {
        return "MyGreatMod v1.0.0 by " + EnumChatFormatting.AQUA + "U9G" + EnumChatFormatting.RESET + ", config by " + EnumChatFormatting.DARK_PURPLE + "Moulberry";
    }

    @Expose
    @Category(name = "Player Options", desc = "All options for players.")
    public Player player = new Player();

    @Expose
    @Category(name = "Misc Options", desc = "Just a bunch of random options.")
    public Misc misc = new Misc();

    public static class Player {
        @Expose
        @ConfigOption(name = "Player Size", desc = "")
        @ConfigGroupHeader(groupId = 0)
        public boolean playerSize = false;

        @Expose
        @ConfigOption(name = "Make Players Small", desc = "Allows you to make all players small.")
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 0)
        public boolean playersAreSmall = false;

        @Expose
        @ConfigOption(name = "Player Opacity", desc = "Allows you to change the opacity of other players.")
        @ConfigEditorSlider(minValue = 0, maxValue = 100, minStep = 1)
        public int playerOpacity = 0;
    }

    public static class Misc {
        @Expose
        @ConfigOption(name = "Waypoint Beacon Color", desc = "Allows you to change the color of all waypoint beacons.")
        @ConfigEditorColour
        public String waypointColor = "255:0:0:0:255";

        @Expose
        @ConfigOption(name = "Waypoint Block Color", desc = "Allows you to change the color of all outlined waypoint blocks.")
        @ConfigEditorColour
        //chroma time:alpha:r:g:b
        public String waypointBlockColor = "0:10:81:255:64";
    }
}
