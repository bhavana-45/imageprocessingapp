package com.imageprocessingapp.service;

import org.springframework.stereotype.Service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@Service
public class LayerService {

    public BufferedImage layerImages(
            BufferedImage baseImage,
            BufferedImage overlayImage,
            int x,
            int y) {

        int width = Math.max(
                baseImage.getWidth(),
                x + overlayImage.getWidth());

        int height = Math.max(
                baseImage.getHeight(),
                y + overlayImage.getHeight());

        BufferedImage output =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d =
                output.createGraphics();

        g2d.drawImage(
                baseImage,
                0,
                0,
                null);

        g2d.drawImage(
                overlayImage,
                x,
                y,
                null);

        g2d.dispose();

        return output;
    }
}