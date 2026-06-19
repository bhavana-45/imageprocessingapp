package com.imageprocessingapp.util;

public class PixelUtils {

    public static int getRed(int pixel) {
        return (pixel >> 16) & 0xFF;
    }

    public static int getGreen(int pixel) {
        return (pixel >> 8) & 0xFF;
    }

    public static int getBlue(int pixel) {
        return pixel & 0xFF;
    }

    public static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    public static int createPixel(
            int r,
            int g,
            int b) {

        return (r << 16)
                | (g << 8)
                | b;
    }
}