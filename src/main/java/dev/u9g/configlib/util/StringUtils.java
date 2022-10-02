package dev.u9g.configlib.util;

public class StringUtils {

    public static String cleanColour(String in) {
        return in.replaceAll("(?i)\\u00A7.", "");
    }
}