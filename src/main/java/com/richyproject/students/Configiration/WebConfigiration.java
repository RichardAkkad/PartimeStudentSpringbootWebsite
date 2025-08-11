package com.richyproject.students.Configiration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigiration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //String uploadDir = System.getProperty("user.home") + "/OneDrive/Desktop/student-images/";
        String uploadDir = "C:/Users/richa/OneDrive/Desktop/student-images/";

        registry.addResourceHandler("/images/profiles/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
