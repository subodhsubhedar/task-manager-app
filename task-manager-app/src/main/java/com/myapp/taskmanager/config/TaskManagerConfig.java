package com.myapp.taskmanager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 
 * @author Admin
 *
 */
@Configuration
public class TaskManagerConfig {

	private static final Logger logger = LoggerFactory.getLogger(TaskManagerConfig.class);

	@Bean(name = "taskManagerCorsConfigSrc")
	public CorsConfigurationSource corsConfigurationSourc() {
		logger.debug("Initializing CorsConfigurationSource");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:4200");
		config.addAllowedHeader("*");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);

		logger.debug("CorsConfigurationSource initialized as {}", source);

		return source;
	}

}
