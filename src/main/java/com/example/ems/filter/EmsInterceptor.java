package com.example.ems.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class EmsInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //HTTP 1.1   must-revalidate
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setHeader("Expires" ,"0"); //Proxy
				
		if(request.getRequestURI().contains("registration-page"))
		{
			return true;
		}
		if(request.getRequestURI().contains("forgetpass"))
		{
			return true;
		}
		if(request.getRequestURI().contains("home"))
		{
			return true;
		}
		
		if(request.getSession(false) != null)
		{
			return true;
		}
		else
		{
			response.sendRedirect("home");
			return false;
		}
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
