package org.helianto.seed;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * Helianto OAuth2 configuration.
 * 
 * @author mauriciofernandesdecastro
 */
@Configuration
public class OAuth2ServerConfig {

	private static final String HELIANTO_RESOURCE_ID = "helianto";

	/**
	 * Authorization server configuration.
	 * 
	 * @author mauriciofernandesdecastro
	 */
	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private DataSource dataSource;
		
		@Autowired
		private TokenStore tokenStore;

		@Autowired
		private UserApprovalHandler userApprovalHandler;

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Value("${helianto.trusted.client.secret:helianto}")
		private String heliantoTrustedClientSecret;

		/**
		 * Client configuration.
		 */
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

			clients.inMemory().withClient("helianto-client")
			 			.resourceIds(HELIANTO_RESOURCE_ID)
			 			.authorizedGrantTypes("authorization_code", "implicit")
			 			.authorities("ROLE_USER")
			 			.scopes("read", "write")
			 			.secret("secret")
			 		.and()
	 		        .withClient("helianto-trusted-client")
 			            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
 			            .authorities("ROLE_USER")
 			            .scopes("read", "write", "trust")
 			            .accessTokenValiditySeconds(60)
		 		    .and()
	 		        .withClient("helianto-trusted-client-with-secret")
 			            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
 			            .authorities("ROLE_USER")
 			            .scopes("read", "write", "trust")
 			            .secret(heliantoTrustedClientSecret);
		}

		@Bean
		public TokenStore tokenStore() {
			return new JdbcTokenStore(dataSource);
		}
		
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.tokenStore(tokenStore)
			.userApprovalHandler(userApprovalHandler)
			.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer.realm("helianto/client");
		}

	}

	/**
	 * Resource server configuration.
	 * 
	 * @author mauriciofernandesdecastro
	 */
	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources.resourceId(HELIANTO_RESOURCE_ID).stateless(false);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.and()
				.requestMatchers().antMatchers("/api/**", "/oauth/users/**", "/oauth/clients/**")
			.and()
				.authorizeRequests()
					.antMatchers("/api").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")                                        
					.antMatchers("/api/**").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
					.regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
						.access("#oauth2.clientHasRole('ROLE_USER') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('write')")
					.regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
						.access("#oauth2.clientHasRole('ROLE_USER') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
					.regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
						.access("#oauth2.clientHasRole('ROLE_USER') and #oauth2.isClient() and #oauth2.hasScope('read')");
		}

	}

	/**
	 * Inner configuration.
	 * 
	 * @author mauriciofernandesdecastro
	 */
	protected static class InnerConfiguration {

		@Autowired
		private ClientDetailsService clientDetailsService;

		@Bean
		@Lazy
		@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
		public ApprovalStoreUserApprovalHandler userApprovalHandler() throws Exception {
			ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
			handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
			handler.setClientDetailsService(clientDetailsService);
			return handler;
		}
	}

}