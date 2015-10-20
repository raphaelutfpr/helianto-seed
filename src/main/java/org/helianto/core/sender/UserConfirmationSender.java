package org.helianto.core.sender;

import javax.inject.Inject;

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
	extends AbstractBodyTemplateSender
{

	/**
	 * Constructor.
	 */
	@Inject
	public UserConfirmationSender(Environment env) {
		super(env.getProperty("sender.noReplyEmail"), env.getProperty("sender.rootFullName"), "userConfirmation");
	}
}
