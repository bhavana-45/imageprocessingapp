package com.imageprocessingapp.service;

import org.springframework.stereotype.Service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

@Service
public class ZoomService {

    public BufferedImage zoom(
            BufferedImage image,
            double factor) {

        if (factor <= 0) {
            throw new IllegalArgumentException(
                    "Zoom factor must be greater than 0");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth =
                (int) Math.round(width * factor);

        int newHeight =
                (int) Math.round(height * factor);

        BufferedImage output =
                new BufferedImage(
                        newWidth,
                        newHeight,
                        BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d =
                output.createGraphics();

        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(
                image,
                0,
                0,
                newWidth,
                newHeight,
                null);

        g2d.dispose();

        return output;
    }
}
