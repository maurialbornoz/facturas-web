package com.demo.springboot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.rmi.registry.Registry;

@Configuration
public class MvcConfig implements WebMvcConfigurer  {
//    private final Logger log = LoggerFactory.getLogger(getClass());
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        WebMvcConfigurer.super.addResourceHandlers(registry);
//        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
//        log.info(resourcePath);
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations(resourcePath);
//    }

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/error_403").setViewName("/error_403");
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}