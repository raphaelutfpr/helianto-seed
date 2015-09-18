package org.helianto.seed;

import org.helianto.core.sender.NotificationSender;
import org.helianto.network.service.RootQueryService;
import org.helianto.network.service.SimpleNetworkKeyName;
import org.helianto.user.service.SimpleUserKeyName;
import org.helianto.user.service.UserQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base config.
 * 
 * @author mauriciofernandesdecastro
 */
public abstract class AbstractContextConfig extends WebMvcConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Static resources.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/META-INF/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/META-INF/fonts/").setCachePeriod(31556926);
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/META-INF/images/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/META-INF/js/").setCachePeriod(31556926);
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/").setCachePeriod(31556926);
        registry.addResourceHandler("/views/**").addResourceLocations("classpath:/views/").setCachePeriod(31556926);
	}	                    
	
	/**
	 * Jackson mapper.
	 * 
	 * Configured to allow comments on JSON files and to disable fail if class 
	 * doens't contains a property.
	 */
	@Bean
	public ObjectMapper mapper(){
		JsonFactory factory = new JsonFactory().configure(Feature.ALLOW_COMMENTS, true);
		ObjectMapper mapper =  new ObjectMapper(factory);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}
	
	/**
	 * Notification sender.
	 */
	@Bean
	public NotificationSender notificationSender() {
		return new NotificationSender();
	}
	
	/**
	 * Password encoder.
	 */
	@Bean
	public Md5PasswordEncoder notificationEncoder() {
		return new Md5PasswordEncoder();
	}
	
	/**
	 * Add Locale change interceptor.
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
		localeInterceptor.setParamName("siteLocale");
		registry.addInterceptor(localeInterceptor);
	}
	
	/**
	 * Cookie locale resolver.
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
	}
	
	/**
	 * Subclasses may override to create custom qualifiers.
	 */
	@Bean
	public RootQueryService rootQueryService() {
		return new RootQueryService(SimpleNetworkKeyName.values());
	}

	/**
	 * Subclasses may override to create custom qualifiers.
	 */
	@Bean
	public UserQueryService userQueryService() {
		return new UserQueryService(SimpleUserKeyName.values());
	}

}
