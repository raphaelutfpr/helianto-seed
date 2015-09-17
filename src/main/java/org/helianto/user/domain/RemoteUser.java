package org.helianto.user.domain;

import java.io.Serializable;
import java.security.Principal;

import org.helianto.core.def.ProviderType;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * Representation of a remote user.
 * 
 * @author mauriciofernandesdecastro
 */
public class RemoteUser 
	implements Serializable
	, Principal
	, UserIdSource
{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id = new Integer(0);
	
	private String userKey = "";
	
	private String firstName = "";
	
	private String lastName = "";
	
	private String displayName = "";
	
	private String profileUrl = "";
	
	private String imageUrl = "";
	
	private String roles = "";
	
	private String providerType = "";
	
	/**
	 * Constructor.
	 */
	public RemoteUser() {
		super();
	}
	
	/**
	 * User key constructor.
	 * 
	 * @param userKey
	 */
	public RemoteUser(String userKey) {
		this();
		setUserKey(userKey);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param userProfile
	 */
	public RemoteUser(UserProfile userProfile) {
		this();
		this.firstName = userProfile.getFirstName();
		this.lastName = userProfile.getLastName();
		this.userKey = userProfile.getEmail();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param userKey
	 * @param firstName
	 * @param lastName
	 * @param displayName
	 * @param profileUrl
	 * @param imageUrl
	 * @param roles
	 * @param providerType
	 */
	public RemoteUser(Integer id, String userKey, String firstName, String lastName, 
			String displayName, String profileUrl, String imageUrl,
			String roles, ProviderType providerType) {
		this();
		setId(id);
		setUserKey(userKey);
		setFirstName(firstName);
		setLastName(lastName);
		setDisplayName(displayName);
		setProfileUrl(profileUrl);
		setImageUrl(imageUrl);
		setRoles(roles);
		setProviderType(providerType);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getPassword() {
		return "unsafe";
	}
	
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public void setConnectionValues(ConnectionValues values) {
		values.setProviderUserId(String.valueOf(id));
		values.setDisplayName(displayName);
		values.setProfileUrl(profileUrl);
		values.setImageUrl(imageUrl);
	}
	
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public ProviderType getProviderType() {
		return ProviderType.valueOf(providerType);
	}
	public String getProviderTypeAsString() {
		return providerType;
	}
	public void setProviderType(ProviderType providerType) {
		this.providerType = providerType.name();
	}

	@Override
	public String toString() {
		return "RemoteUser [id=" + id + ", userKey=" + userKey + ", firstName="
				+ firstName + ", lastName=" + lastName + ", displayName="
				+ displayName + ", providerType=" + providerType + "]";
	}

	//-- From UserIdSource interface
	
	@Override
	public String getUserId() {
		return String.valueOf(getId());
	}

	//-- From Principal interface
	
	@Override
	public String getName() {
		return String.valueOf(getId());
	}
	
}
