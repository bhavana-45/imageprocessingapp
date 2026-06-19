package com.imageprocessingapp.service;

import com.imageprocessingapp.util.PixelUtils;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class BlurService {

    public BufferedImage blur(
            BufferedImage image,
            int intensity) {

        if (intensity < 1) {
            intensity = 1;
        }

        BufferedImage result = image;

        for (int i = 0; i < intensity; i++) {
            result = applySingleBlur(result);
        }

        return result;
    }

    private BufferedImage applySingleBlur(
            BufferedImage image) {

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

                int redSum = 0;
                int greenSum = 0;
                int blueSum = 0;

                for (int ky = -1; ky <= 1; ky++) {

                    for (int kx = -1; kx <= 1; kx++) {

                        int neighborIndex =
                                (y + ky) * width
                                        + (x + kx);

                        int pixel =
                                sourcePixels[
                                        neighborIndex];

                        redSum +=
                                PixelUtils.getRed(pixel);

                        greenSum +=
                                PixelUtils.getGreen(pixel);

                        blueSum +=
                                PixelUtils.getBlue(pixel);
                    }
                }

                int red = redSum / 9;
                int green = greenSum / 9;
                int blue = blueSum / 9;

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
}