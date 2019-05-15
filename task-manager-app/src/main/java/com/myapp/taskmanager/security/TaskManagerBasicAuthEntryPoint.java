package com.myapp.taskmanager.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class TaskManagerBasicAuthEntryPoint extends BasicAuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(TaskManagerBasicAuthEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		logger.error("AuthenticationException ex {}",authException.getMessage());
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader("auth-custom", authException.getMessage());
		
		super.commence(request, response, authException);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setRealmName("TASK_MNGR_SECURITY");
		super.afterPropertiesSet();
	}
}
