package com.myapp.taskmanager.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class TaskManagerSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(TaskManagerSecurityConfiguration.class);

	private static final String TASK_MNGR_ROLE= "TASK_MNGR";
	private static final String TASK_ADMIN_ROLE= "TASK_ADMIN";
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private CorsConfigurationSource taskManagerCorsConfigSrc;
	
	@Bean
	public BasicAuthenticationEntryPoint getTaskMngrBasicAuthPoint() {
		return new TaskManagerBasicAuthEntryPoint();
	}

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.debug("Configuring AuthenticationManagerBuilder with inMemoryAuthentication params...");
		
		auth.inMemoryAuthentication()
				.withUser("subodh").password(passwordEncoder.encode("subodh123")).roles(TASK_MNGR_ROLE).and()
				.withUser("admin").password(passwordEncoder.encode("admin123"))
				.roles(TASK_ADMIN_ROLE);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("Configuring Authorization params...");
		
		http
			.cors().configurationSource(taskManagerCorsConfigSrc).and()
			.httpBasic().authenticationEntryPoint(getTaskMngrBasicAuthPoint()).realmName("TASK_MNGR_SECURITY").and()
				.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll().anyRequest().authenticated()
				.and()
				.authorizeRequests().antMatchers(HttpMethod.GET, "/tasks").hasAnyRole(TASK_MNGR_ROLE,TASK_ADMIN_ROLE).and()
				.authorizeRequests().antMatchers(HttpMethod.GET, "/task/**").hasAnyRole(TASK_MNGR_ROLE,TASK_ADMIN_ROLE).and()
				.authorizeRequests().antMatchers(HttpMethod.POST, "/task/add").hasAnyRole(TASK_ADMIN_ROLE).and()
				.authorizeRequests().antMatchers(HttpMethod.PUT, "/task/update").hasAnyRole(TASK_ADMIN_ROLE).and()
				.authorizeRequests().antMatchers(HttpMethod.DELETE, "/task/delete/**").hasAnyRole(TASK_ADMIN_ROLE).and()
				.csrf().disable()
				.formLogin().disable();
				
	}

}
