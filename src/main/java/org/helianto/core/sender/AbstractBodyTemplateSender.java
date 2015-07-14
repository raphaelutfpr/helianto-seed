package org.helianto.core.sender;

import org.helianto.sendgrid.message.sender.AbstractTemplateSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

/**
 * E-mail template base class.
 * 
 * @author mauriciofernandesdecastro
 */
@PropertySource("classpath:/freeemarker/sendgrid/sender.properties")
public abstract class AbstractBodyTemplateSender 
	extends AbstractTemplateSender
{

	@Value("${sender.staticRedirectQuestion}")
	private String staticRedirectQuestion;
	
	@Value("${sender.staticRedirectMessage}")
	private String staticRedirectMessage;

	/**
	 * Constructor.
	 * 
	 * @param entityProps
	 */
	public AbstractBodyTemplateSender(String senderEmail, String senderName, String templateName) {
		super(senderEmail, senderName, templateName);
	}
	
	@Override
	protected String getConfirmationUri(String... params) {
		return getApiUrl()+"/signup/verify?token=x";
	}

	@Override
	public String getBody() {
		StringBuilder body = new StringBuilder();
		body.append("<div class='background-color: black; height: 12px;'> ")
		.append("<p align=\"center\" class=\"view-browser text-align-center\" ")
		.append("style=\"font-size: 12px; margin: 0 0 12px; ")
		.append("padding: 0; font-family: Arial, sans-serif; line-height: 22px; color: #2E2E2E; text-align: center;\">")
		.append(staticRedirectQuestion)
		.append("<a href=\"")
		.append(staticPath)
		.append(getTemplateId())
		.append(";");
		if (hasConfirmationUri()) {
			body.append("?confirmationuri=")
			.append(getConfirmationUriEncoded());
		}
		body.append(getConfirmationUriEncoded())
		.append("\" style=\"color: #08088A; text-decoration: underline;\">")
		.append(staticRedirectMessage)
		.append("</a></p></div>");
		return body.toString();
	}
	
}
