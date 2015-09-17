package org.springframework.social.iservport.connect;

import org.springframework.social.iservport.api.Iservport;
import org.springframework.social.iservport.api.impl.IservportTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * Iservport service provider.
 * 
 * @author mauriciofernandesdecastro
 */
public final class IservportServiceProvider 
	extends AbstractOAuth2ServiceProvider<Iservport> 
{

	private final String applicationUrl;
	
	/**
	 * Constructor.
	 * 
	 * @param clientId
	 * @param secret
	 * @param remoteUser
	 * @param applicationUrl
	 */
    public IservportServiceProvider(String clientId, String secret, String applicationUrl) {
        super(new OAuth2Template(clientId, secret,
        		applicationUrl+"/rest/oauth/authorize",
        		applicationUrl+"/rest/oauth/token"));
        this.applicationUrl = applicationUrl;
    }

    public Iservport getApi(String accessToken) {
        return new IservportTemplate(accessToken, applicationUrl);
    }

}