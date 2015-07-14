package org.helianto.core.sender;

import javax.inject.Inject;

import org.helianto.sendgrid.message.sender.AbstractTemplateSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author mauriciofernandesdecastro
 *
 */
@Component
public class NotificationSender 
	extends AbstractTemplateSender
{
	@Value(value = "noReplyEmail")
	private static String noReplyEmail = "";
	
	@Value(value = "rootFullName")
	private static String rootFullName = "";
	
	/**
	 * Constructor.
	 * 
	 * @param entityProps
	 */
	@Inject
	public NotificationSender() {
		super(noReplyEmail, rootFullName, "userConfirmation");
	}

}
