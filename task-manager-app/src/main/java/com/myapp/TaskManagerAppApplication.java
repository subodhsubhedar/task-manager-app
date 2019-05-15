package com.myapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 
 * @author Admin
 *
 */
@SpringBootApplication
public class TaskManagerAppApplication {

	private static final Logger logger = LoggerFactory.getLogger(TaskManagerAppApplication.class);

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskManagerAppApplication.class, args);
	}

	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		logger.debug("Initializing LocalValidatorFactoryBean");

		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);

		logger.debug("Configuring message source{}", messageSource);
		return bean;
	}
}
