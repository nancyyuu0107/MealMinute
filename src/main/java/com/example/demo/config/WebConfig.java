package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${resource.url}") // file:C:\\mealminute\\
	private String resouce;  
	
	@Value("${cors.domain}")
	private String corsDomain;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 使得所有以 /images/ 開頭的請求可以從指定的本地文件系統路徑獲取靜態圖片。
		registry.addResourceHandler("/images/**").addResourceLocations(resouce);
		
	}
	
//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins(corsDomain) // 允许的来源
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
//                .allowedHeaders("*"); // 允许的头部
//    }
}
