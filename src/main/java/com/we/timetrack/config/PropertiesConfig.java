package com.we.timetrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource({ "classpath:timex.properties" })
public class PropertiesConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer configurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
}
