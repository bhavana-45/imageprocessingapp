package com.imageprocessingapp.service;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class FlipService {

    public BufferedImage flip(
            BufferedImage image,
            String direction) {

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage output =
                new BufferedImage(
                        width,
                        height,
                        image.getType());

        if ("horizontal".equalsIgnoreCase(direction)) {

            for (int y = 0; y < height; y++) {

                for (int x = 0; x < width; x++) {

                    output.setRGB(
                            width - 1 - x,
                            y,
                            image.getRGB(x, y));
                }
            }

        } else if ("vertical".equalsIgnoreCase(direction)) {

            for (int y = 0; y < height; y++) {

                for (int x = 0; x < width; x++) {

                    output.setRGB(
                            x,
                            height - 1 - y,
                            image.getRGB(x, y));
                }
            }

        } else {

            throw new IllegalArgumentException(
                    "Direction must be 'horizontal' or 'vertical'");
        }

        return output;
    }
}
