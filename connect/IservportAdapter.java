package org.springframework.social.iservport.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.iservport.api.Iservport;
import org.springframework.social.iservport.api.impl.RemoteUser;

/**
 * Iservport API adapter.
 * 
 * @author mauriciofernandesdecastro
 */
public class IservportAdapter 
	implements ApiAdapter<Iservport> 
{

	/**
	 * true if the API is functional, false if not.
	 * 
	 * @param api
	 */
	@Override
	public boolean test(Iservport api) {
		try {
			api.getProfile();
            return true;
        } catch (ApiException e) {
            return false;
        }	
	}

	@Override
	public void setConnectionValues(Iservport api, ConnectionValues values) {
		RemoteUser user = api.getProfile();
		user.setConnectionValues(values);
	}

	@Override
	public UserProfile fetchUserProfile(Iservport api) {
		RemoteUser user = api.getProfile();
		return new UserProfileBuilder()
				.setFirstName(user.getFirstName())
				.setLastName(user.getLastName())
				.setUsername(user.getUserId())
				.setEmail(user.getUserKey())
			.build();
	}

	@Override
	public void updateStatus(Iservport api, String message) {
		// not supported yet
	}

}
