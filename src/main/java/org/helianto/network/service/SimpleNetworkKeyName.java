package org.helianto.network.service;

import java.io.Serializable;

import org.helianto.core.internal.KeyNameAdapter;

public enum SimpleNetworkKeyName implements KeyNameAdapter
{
	
	CUSTOMER('C'),
	ASSOCIATE('A');
	
	private char value;
	
	/**
	 * Construtor.
	 * 
	 * @param value
	 */
	private SimpleNetworkKeyName(char value) {
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

