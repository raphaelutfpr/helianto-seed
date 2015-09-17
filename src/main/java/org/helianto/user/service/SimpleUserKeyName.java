package org.helianto.user.service;

import java.io.Serializable;

import org.helianto.core.internal.KeyNameAdapter;

public enum SimpleUserKeyName implements KeyNameAdapter
{
	
	USER('A')
	, GUEST('X')
	, ADMIN('G');
	
	private char value;
	
	/**
	 * Construtor.
	 * 
	 * @param value
	 */
	private SimpleUserKeyName(char value) {
		this.value = value;
	}
	
	public Serializable getKey() {
		return this.value;
	}
	
	@Override
	public String getCode() {
		return value+"";
	}
	
	@Override
	public String getName() {
		return name();
	}
	
	public KeyNameAdapter[] getValues() {
		return values();
	}
	
}

