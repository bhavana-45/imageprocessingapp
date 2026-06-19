package com.imageprocessingapp.service;

import com.imageprocessingapp.util.PixelUtils;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class ContrastService {

    public BufferedImage adjustContrast(
            BufferedImage image,
            double contrastFactor) {

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

        for (int i = 0; i < pixels.length; i++) {

            int pixel = pixels[i];

            int r = PixelUtils.getRed(pixel);
            int g = PixelUtils.getGreen(pixel);
            int b = PixelUtils.getBlue(pixel);

            r = adjustChannel(r, contrastFactor);
            g = adjustChannel(g, contrastFactor);
            b = adjustChannel(b, contrastFactor);

            pixels[i] = PixelUtils.createPixel(
                    r,
                    g,
                    b);
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

    private int adjustChannel(
            int value,
            double contrastFactor) {

        int result =
                (int) ((value - 128)
                        * contrastFactor
                        + 128);

        return PixelUtils.clamp(result);
    }
    }
