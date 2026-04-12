package com.allblue.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.image.product-path}")
    private String productImagePath;

    @Value("${app.image.model-path}")
    private String modelImagePath;

    @Value("${app.image.lookbook-output-path}")
    private String lookbookOutputPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/products/**")
                .addResourceLocations("file:" + productImagePath + "/");

        registry.addResourceHandler("/images/models/**")
                .addResourceLocations("file:" + modelImagePath + "/");

        registry.addResourceHandler("/images/lookbooks/**")
                .addResourceLocations("file:" + lookbookOutputPath + "/");

        log.info("[WebMvcConfig] 상품 이미지 경로: {}", productImagePath);
        log.info("[WebMvcConfig] 모델 이미지 경로: {}", modelImagePath);
        log.info("[WebMvcConfig] 룩북 생성 이미지 경로: {}", lookbookOutputPath);
    }
}
