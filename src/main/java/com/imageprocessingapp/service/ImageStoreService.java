package com.imageprocessingapp.service;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ImageStoreService {

    private final Map<String, BufferedImage> imageStore =
            new ConcurrentHashMap<>();

    public String save(BufferedImage image) {

        String imageId =
                UUID.randomUUID().toString();

        imageStore.put(imageId, image);

        return imageId;
    }

    public BufferedImage get(String imageId) {

        return imageStore.get(imageId);
    }

    public void update(
            String imageId,
            BufferedImage image) {

        imageStore.put(imageId, image);
    }

    public boolean exists(String imageId) {

        return imageStore.containsKey(imageId);
    }
}