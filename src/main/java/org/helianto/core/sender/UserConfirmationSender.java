package org.helianto.core.sender;

import javax.inject.Inject;

import org.helianto.sendgrid.message.sender.AbstractTemplateSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Send confirmation e-mail.
 * 
 * @author mauriciofernandesdecastro
 */
@Component
@PropertySource("classpath:/META-INF/sender.properties")
public class UserConfirmationSender 
	extends AbstractTemplateSender
{

	@Value("${sender.staticRedirectQuestion}")
	private String staticRedirectQuestion;
	
	@Value("${sender.staticRedirectMessage}")
	private String staticRedirectMessage;

	/**
	 * Constructor.
	 */
	@Inject
	public UserConfirmationSender(Environment env) {
		super(env.getProperty("winnect.noReplyEmail"), env.getProperty("winnect.rootFullName"), "userConfirmation");
	}
	
	protected String getConfirmationUri(String... params) {
		return getApiUrl()+"/signup/verify?token=x";
	}

}
