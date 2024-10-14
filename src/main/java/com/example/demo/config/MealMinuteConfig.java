package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.util.JsonWebTokenInterceptor;

@Configuration
public class MealMinuteConfig implements WebMvcConfigurer{
//	@Bean
//    JsonWebTokenUtility jsonWebTokenUtility() {
//        return new JsonWebTokenUtility();
//    }
	
	@Autowired
	private JsonWebTokenInterceptor jsonWebTokenInterceptor;
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jsonWebTokenInterceptor).addPathPatterns("/customers/*");
	}
	
	
}
