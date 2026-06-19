package com.imageprocessingapp.service;

import com.imageprocessingapp.util.PixelUtils;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class SharpenService {

    public BufferedImage sharpen(
            BufferedImage image,
            int intensity) {

        if (intensity < 1) {
            intensity = 1;
        }

        int[][] kernel = buildKernel(intensity);

        int width = image.getWidth();
        int height = image.getHeight();

        int[] sourcePixels =
                image.getRGB(
                        0,
                        0,
                        width,
                        height,
                        null,
                        0,
                        width);

        int[] outputPixels =
                sourcePixels.clone();

        for (int y = 1; y < height - 1; y++) {

            for (int x = 1; x < width - 1; x++) {

                int red = 0;
                int green = 0;
                int blue = 0;

                for (int ky = -1; ky <= 1; ky++) {

                    for (int kx = -1; kx <= 1; kx++) {

                        int neighborIndex =
                                (y + ky) * width
                                        + (x + kx);

                        int pixel =
                                sourcePixels[
                                        neighborIndex];

                        int weight =
                                kernel[ky + 1][kx + 1];

                        red +=
                                PixelUtils.getRed(pixel)
                                        * weight;

                        green +=
                                PixelUtils.getGreen(pixel)
                                        * weight;

                        blue +=
                                PixelUtils.getBlue(pixel)
                                        * weight;
                    }
                }

                red = PixelUtils.clamp(red);
                green = PixelUtils.clamp(green);
                blue = PixelUtils.clamp(blue);

                int currentIndex =
                        y * width + x;

                outputPixels[currentIndex] =
                        PixelUtils.createPixel(
                                red,
                                green,
                                blue);
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
                outputPixels,
                0,
                width);

        return output;
    }

    private int[][] buildKernel(
            int intensity) {

        return new int[][]{
                {0, -intensity, 0},
                {-intensity,
                        1 + (4 * intensity),
                        -intensity},
                {0, -intensity, 0}
        };
    }
}
