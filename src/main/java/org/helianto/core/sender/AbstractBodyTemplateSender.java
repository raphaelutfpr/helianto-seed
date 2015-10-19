package org.helianto.core.sender;

import java.util.Map;

import org.helianto.sendgrid.message.sender.AbstractTemplateSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * E-mail template base class.
 * 
 * @author mauriciofernandesdecastro
 */
@PropertySource("classpath:/freemarker/sendgrid/sender.properties")
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
	protected String getConfirmationUri(String confirmationToken) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getApiUrl()+"/verify").queryParam("confirmationToken", confirmationToken);
		return builder.build().encode().toUri().toString();
	}

	@Override
	public String getBody(Map<String, String> paramMap) {
		//paramMap.put("recipientFirstName", recipientName);
		//paramMap.put("recipientEmail", recipientEmail);
		
		//System.err.println("recipientFirstName: "+ recipientName);
		
		StringBuilder body = new StringBuilder();
		body.append("<div class='background-color: black; height: 12px;'> ")
		.append("<p align=\"center\" class=\"view-browser text-align-center\" ")
		.append("style=\"font-size: 12px; margin: 0 0 12px; ")
		.append("padding: 0; font-family: Arial, sans-serif; line-height: 22px; color: #2E2E2E; text-align: center;\">")
		.append(staticRedirectQuestion)
		.append("<a href=\"")
		.append(getApiUrl())
		.append(staticPath)
		.append(getTemplateId());
		for (String param: paramMap.keySet()) {
			body.append(";"+param+"="+paramMap.get(param));
		}
		body.append(";?confirmationuri=");
		if(paramMap.containsKey("confirmationToken")){
			body.append(getConfirmationUriEncoded(getConfirmationUri(paramMap.get("paramMap"))));
		}
		body.append("\" style=\"color: #08088A; text-decoration: underline;\">")
		.append(staticRedirectMessage)
		.append("</a></p></div>");
			
		System.err.println(body.toString());
		
		return body.toString();
	}
	
}
