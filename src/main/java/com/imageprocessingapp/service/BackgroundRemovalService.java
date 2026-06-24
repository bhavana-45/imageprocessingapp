package com.imageprocessingapp.service;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class BackgroundRemovalService {

    private static final int THRESHOLD = 60;

    public BufferedImage removeBackground(
            BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage output =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB);

        int[] backgroundColor =
                estimateBackgroundColor(image);

        int bgRed = backgroundColor[0];
        int bgGreen = backgroundColor[1];
        int bgBlue = backgroundColor[2];

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                int pixel =
                        image.getRGB(x, y);

                int red =
                        (pixel >> 16) & 0xff;

                int green =
                        (pixel >> 8) & 0xff;

                int blue =
                        pixel & 0xff;

                double distance =
                        Math.sqrt(
                                Math.pow(red - bgRed, 2)
                                        + Math.pow(green - bgGreen, 2)
                                        + Math.pow(blue - bgBlue, 2));

                if (distance < THRESHOLD) {

                    output.setRGB(
                            x,
                            y,
                            0x00000000);
                } else {

                    int argb =
                            (255 << 24)
                                    | (red << 16)
                                    | (green << 8)
                                    | blue;

                    output.setRGB(
                            x,
                            y,
                            argb);
                }
            }
        }

        return output;
    }

    private int[] estimateBackgroundColor(
            BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        long redSum = 0;
        long greenSum = 0;
        long blueSum = 0;
        long count = 0;

        for (int x = 0; x < width; x++) {

            int top =
                    image.getRGB(x, 0);

            int bottom =
                    image.getRGB(x, height - 1);

            redSum += (top >> 16) & 0xff;
            greenSum += (top >> 8) & 0xff;
            blueSum += top & 0xff;

            redSum += (bottom >> 16) & 0xff;
            greenSum += (bottom >> 8) & 0xff;
            blueSum += bottom & 0xff;

            count += 2;
        }

        for (int y = 0; y < height; y++) {

            int left =
                    image.getRGB(0, y);

            int right =
                    image.getRGB(width - 1, y);

            redSum += (left >> 16) & 0xff;
            greenSum += (left >> 8) & 0xff;
            blueSum += left & 0xff;

            redSum += (right >> 16) & 0xff;
            greenSum += (right >> 8) & 0xff;
            blueSum += right & 0xff;

            count += 2;
        }

        return new int[]{
                (int) (redSum / count),
                (int) (greenSum / count),
                (int) (blueSum / count)
        };
    }
}
