package com.example.ems.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.example.ems.filter.EmsInterceptor;

@Configuration
@SuppressWarnings("deprecation")
public class InterceptorConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private EmsInterceptor emsInterceptor;

	   @Override
	   public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(emsInterceptor).addPathPatterns("/Admin-Dashboard","/profile-page","/update-{id}","/forgetpass",
	        		"/User-Dashboard","/registration-page");
	   }
}
