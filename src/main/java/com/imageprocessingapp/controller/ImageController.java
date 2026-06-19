package com.imageprocessingapp.controller;

import com.imageprocessingapp.service.BrightnessService;
import com.imageprocessingapp.service.GrayScaleService;
import com.imageprocessingapp.service.ImageStoreService;
import com.imageprocessingapp.service.ContrastService;
import com.imageprocessingapp.service.BlurService;
import com.imageprocessingapp.service.SharpenService;
import com.imageprocessingapp.service.ImageRotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final GrayScaleService grayScaleService;
    private final BrightnessService brightnessService;
    private final ImageStoreService imageStoreService;
    private final ContrastService contrastService;
    private final BlurService blurService;
    private final SharpenService sharpenService;
    private final ImageRotationService imageRotationService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        BufferedImage image = readImage(file);

        String imageId =
                imageStoreService.save(image);

        return ResponseEntity.ok(
                "Image uploaded successfully.\nImage ID: "
                        + imageId);
    }

    @PostMapping(
            value = "/{imageId}/grayscale",
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> grayscale(
            @PathVariable String imageId)
            throws IOException {

        BufferedImage image =
                imageStoreService.get(imageId);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        BufferedImage grayImage =
                grayScaleService.convertToGrayScale(image);

        imageStoreService.update(
                imageId,
                grayImage);

        return buildImageResponse(grayImage);
    }

    @PostMapping(
            value = "/{imageId}/brightness",
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> adjustBrightness(
            @PathVariable String imageId,
            @RequestParam("value") int value)
            throws IOException {

        BufferedImage image =
                imageStoreService.get(imageId);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        BufferedImage processedImage =
                brightnessService.adjustBrightness(
                        image,
                        value);

        imageStoreService.update(
                imageId,
                processedImage);

        return buildImageResponse(processedImage);
    }


    @PostMapping(
            value = "/{imageId}/contrast",
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> adjustContrast(
            @PathVariable String imageId,
            @RequestParam("factor")
            double factor)
            throws IOException {

        BufferedImage image =
                imageStoreService.get(imageId);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        BufferedImage processedImage =
                contrastService.adjustContrast(
                        image,
                        factor);

        imageStoreService.update(
                imageId,
                processedImage);

        return buildImageResponse(
                processedImage);
    }


    @PostMapping(
            value = "/{imageId}/blur",
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> blur(
            @PathVariable String imageId,
            @RequestParam(defaultValue = "1")
            int intensity)
            throws IOException {

        BufferedImage image =
                imageStoreService.get(imageId);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        BufferedImage blurredImage =
                blurService.blur(
                        image,
                        intensity);

        imageStoreService.update(
                imageId,
                blurredImage);

        return buildImageResponse(
                blurredImage);
    }



    @PostMapping(
            value = "/{imageId}/sharpen",
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> sharpen(
            @PathVariable String imageId,
            @RequestParam(defaultValue = "1")
            int intensity)
            throws IOException {

        BufferedImage image =
                imageStoreService.get(imageId);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        BufferedImage sharpenedImage =
                sharpenService.sharpen(
                        image,
                        intensity);

        imageStoreService.update(
                imageId,
                sharpenedImage);

        return buildImageResponse(
                sharpenedImage);
    }


    @PostMapping(
            value = "/{imageId}/rotate",
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> rotate(
            @PathVariable String imageId,
            @RequestParam double angle)
            throws IOException {

        BufferedImage image =
                imageStoreService.get(imageId);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        BufferedImage rotatedImage =
                imageRotationService.rotate(
                        image,
                        angle);

        imageStoreService.update(
                imageId,
                rotatedImage);

        return buildImageResponse(
                rotatedImage);
    }

    @GetMapping(
            value = "/{imageId}",
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> previewImage(
            @PathVariable String imageId)
            throws IOException {

        BufferedImage image =
                imageStoreService.get(imageId);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return buildImageResponse(image);
    }

    private BufferedImage readImage(
            MultipartFile file)
            throws IOException {

        BufferedImage image =
                ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IllegalArgumentException(
                    "Invalid image file");
        }

        return image;
    }

    private ResponseEntity<byte[]> buildImageResponse(
            BufferedImage image)
            throws IOException {

        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();

        ImageIO.write(
                image,
                "png",
                baos);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(baos.toByteArray());
    }
}