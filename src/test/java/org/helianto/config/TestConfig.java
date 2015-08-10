package org.helianto.config;

import org.helianto.qualifier.QualifierAdapterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);
	
	@Bean
	public QualifierAdapterList networkQualifierStub() throws Exception {
		NetworkQualifierStub stub = new NetworkQualifierStub();
		logger.info("New stub {} created.", stub);
		return stub;
	}
	
	@Bean
	public QualifierAdapterList userQualifierStub() throws Exception {
		UserQualifierStub stub = new UserQualifierStub();
		logger.info("New stub {} created.", stub);
		return stub;
	}
	
}
