package org.helianto.seed;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.helianto.core.config.HeliantoServiceConfig;
import org.helianto.sendgrid.config.SendGridConfig;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;



/**
 * Basic Java configuration. 
 * 
 * @author mauriciofernandesdecastro
 */
@Configuration
@EnableWebMvc
@Import({HeliantoServiceConfig.class, SendGridConfig.class})
@ComponentScan(
	basePackages = {
		"org.helianto.*.controller"
})
@EnableJpaRepositories(
    basePackages={"org.helianto.*.repository"})
public abstract class AbstractRootContextConfig extends AbstractContextConfig {
	
	@Inject
	protected Environment env;
	
	/**
	 * Override to set packages to scan.
	 */
	protected String[] getPacakgesToScan() {
		return new String[] {"org.helianto.*.domain"};
	}
	
	/**
	 * Entity manager factory.
	 */
	@Bean 
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
		vendor.setGenerateDdl(env.getProperty("helianto.sql.generateDdl", Boolean.class, Boolean.TRUE));

		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(dataSource());
		bean.setPackagesToScan(getPacakgesToScan());
		bean.setJpaVendorAdapter(vendor);
		bean.setPersistenceProvider(new HibernatePersistence());
		bean.afterPropertiesSet();
        return bean.getObject();
	}
	
	/**
	 * Simple data source.
	 * 
	 * @throws NamingException 
	 * @throws IllegalArgumentException 
	 */
	@Bean
	public DataSource dataSource() throws IllegalArgumentException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        dataSource.setDriverClassName(env.getRequiredProperty("helianto.db.driver"));
        dataSource.setUrl(env.getRequiredProperty("helianto.db.url"));
        dataSource.setUsername(env.getRequiredProperty("helianto.db.username"));
        dataSource.setPassword(env.getRequiredProperty("helianto.db.password"));
         
        return dataSource;
	}
	
}
