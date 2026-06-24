package com.imageprocessingapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .mediaType("css", MediaType.valueOf("text/css"))
                .mediaType("js",  MediaType.valueOf("application/javascript"))
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("png",  MediaType.IMAGE_PNG)
                .mediaType("jpg",  MediaType.IMAGE_JPEG);
    }
}