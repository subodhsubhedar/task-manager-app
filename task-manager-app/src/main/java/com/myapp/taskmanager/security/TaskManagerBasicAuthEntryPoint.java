package com.myapp.taskmanager.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class TaskManagerBasicAuthEntryPoint extends BasicAuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

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
