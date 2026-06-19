package com.imageprocessingapp.service;

import com.imageprocessingapp.util.PixelUtils;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class BrightnessService {

    public BufferedImage adjustBrightness(
            BufferedImage image,
            int brightnessValue) {

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = image.getRGB(
                0,
                0,
                width,
                height,
                null,
                0,
                width);

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                int index = y * width + x;

                int pixel = pixels[index];

                int r = PixelUtils.getRed(pixel);
                int g = PixelUtils.getGreen(pixel);
                int b = PixelUtils.getBlue(pixel);

                r = PixelUtils.clamp(r + brightnessValue);
                g = PixelUtils.clamp(g + brightnessValue);
                b = PixelUtils.clamp(b + brightnessValue);

                pixels[index] =
                        PixelUtils.createPixel(r, g, b);
            }
        }

        BufferedImage output =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB);

        output.setRGB(
                0,
                0,
                width,
                height,
                pixels,
                0,
                width);

        return output;
    }
}