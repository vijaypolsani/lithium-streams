package com.lithium.streams.compliance.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@PropertySource("classpath:/spring/config.properties")
public class PropertiesReader {

	public PropertiesReader() {
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[] { new ClassPathResource("/spring/config.properties") };
		pspc.setLocations(resources);
		pspc.setIgnoreUnresolvablePlaceholders(true);
		return pspc;
	}

}
