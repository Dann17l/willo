package com.willoacademy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/auth/**")
                .addResourceLocations("classpath:/static/auth/");
        registry.addResourceHandler("/catalog/**")
                .addResourceLocations("classpath:/static/catalog/");
        registry.addResourceHandler("/player/**")
                .addResourceLocations("classpath:/static/player/");
        registry.addResourceHandler("/admin/**")
                .addResourceLocations("classpath:/static/admin/");
    }
}
