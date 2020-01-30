package com.we.timetrack.config;

import java.io.File;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.we.timetrack.config.RootConfig;
import com.we.timetrack.config.WebConfig;

public class TimeTrackWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	private final static int maxUploadSize = 5 * 1024 * 1024;
	
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class };
	}
	
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.setInitParameter("spring.profiles.active", "securityConfigProduction, dataSourceProduction, LdapAndDBEmployees");
	}
	
	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		
		// upload temp file will put here
		File uploadDirectory = new File(System.getProperty("java.io.tmpdir"));
		
		// register a MultipartConfigElement
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(uploadDirectory.getAbsolutePath(),
				maxUploadSize, maxUploadSize * 2, maxUploadSize / 2);
		
		registration.setMultipartConfig(multipartConfigElement);
				
			
	}
	
}
