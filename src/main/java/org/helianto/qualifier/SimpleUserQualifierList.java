package org.helianto.qualifier;

import java.io.Serializable;

import org.helianto.core.internal.KeyNameAdapter;
import org.helianto.qualifier.AbstractQualifierAdapterList.UserKeyNameAdapter;

/**
 * Simple user qualifier list.
 * 
 * @author mauriciofernandesdecastro
 */
@UserKeyNameAdapter
public class SimpleUserQualifierList 
	extends AbstractQualifierAdapterList
{

	@Override
	protected KeyNameAdapter[] getQualifierArray() {
		return InternalUserKeyName.values();
	}
	
	public enum InternalUserKeyName implements KeyNameAdapter {
		
		USER('U'),
		ADMIN('A');
		
		private char value;
		
		/**
		 * Construtor.
		 * 
		 * @param value
		 */
		private InternalUserKeyName(char value) {
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
		
	}

}
