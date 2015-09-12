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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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
	
	/**
	 * Override to set JNDI name.
	 */
	protected String getJndiName() {
		return "jdbc/iservportDB";
	}
	
	/**
	 * Override to set packages to scan.
	 */
	protected String[] getPacakgesToScan() {
		return new String[] {"org.helianto.*.domain"};
	}
	
	@Inject
	private DataSource dataSource;
	
	@Inject
	private JpaVendorAdapter vendorAdapter;
	
	/**
	 * JNDI factory.
	 * 
	 * @throws IllegalArgumentException
	 * @throws NamingException
	 */
	@Bean
	public Object jndiObjectFactoryBean() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
		jndiFactory.setJndiName(getJndiName());
		jndiFactory.setResourceRef(true);
		jndiFactory.afterPropertiesSet();
		return jndiFactory.getObject();
	}
	
	/**
	 * Substitui a configuração original do <code>EntityManagerFactory</code>
	 * para incluir novos pacotes onde pesquisar por entidades persistentes.
	 */
	@Bean 
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(dataSource);
		bean.setPackagesToScan(getPacakgesToScan());
		bean.setJpaVendorAdapter(vendorAdapter);
		bean.setPersistenceProvider(new HibernatePersistence());
		bean.afterPropertiesSet();
        return bean.getObject();
	}
	
	/**
	 * JNDI data source.
	 * 
	 * @throws NamingException 
	 * @throws IllegalArgumentException 
	 */
	@Bean
	public DataSource dataSource() throws IllegalArgumentException, NamingException {
		return (DataSource) jndiObjectFactoryBean();
	}
	
}
