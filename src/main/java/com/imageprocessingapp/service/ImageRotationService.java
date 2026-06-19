package com.imageprocessingapp.service;

import org.springframework.stereotype.Service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

@Service
public class ImageRotationService {

    public BufferedImage rotate(
            BufferedImage image,
            double angle) {

        int width = image.getWidth();
        int height = image.getHeight();

        double radians =
                Math.toRadians(angle);

        double sin =
                Math.abs(Math.sin(radians));

        double cos =
                Math.abs(Math.cos(radians));

        int newWidth =
                (int) Math.floor(
                        width * cos +
                                height * sin);

        int newHeight =
                (int) Math.floor(
                        height * cos +
                                width * sin);

        BufferedImage rotatedImage =
                new BufferedImage(
                        newWidth,
                        newHeight,
                        BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d =
                rotatedImage.createGraphics();

        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform transform =
                new AffineTransform();

        transform.translate(
                (newWidth - width) / 2.0,
                (newHeight - height) / 2.0);

        transform.rotate(
                radians,
                width / 2.0,
                height / 2.0);

        g2d.drawImage(
                image,
                transform,
                null);

        g2d.dispose();

        return rotatedImage;
    }
}
