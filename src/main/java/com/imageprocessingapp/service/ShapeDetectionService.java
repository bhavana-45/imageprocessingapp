package com.imageprocessingapp.service;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class ShapeDetectionService {

    public String detectShape(
            BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        if (width == height) {
            return "Square";
        }

        return "Rectangle";
    }
}