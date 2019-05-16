package com.myapp;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
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

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			logger.debug("################################################");
			logger.debug("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);

			logger.debug(Arrays.asList(beanNames).toString());

			logger.debug("################################################");
		};
	}
}
