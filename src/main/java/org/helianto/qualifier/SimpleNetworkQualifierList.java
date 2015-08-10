package org.helianto.qualifier;

import java.io.Serializable;

import org.helianto.core.internal.KeyNameAdapter;
import org.helianto.qualifier.AbstractQualifierAdapterList;
import org.helianto.qualifier.AbstractQualifierAdapterList.NetworkKeyNameAdapter;

/**
 * Simple network qualifier list.
 * 
 * @author mauriciofernandesdecastro
 */
@NetworkKeyNameAdapter
public class SimpleNetworkQualifierList 
	extends AbstractQualifierAdapterList
{

	@Override
	protected KeyNameAdapter[] getQualifierArray() {
		return InternalNetworkKeyName.values();
	}
	
	public enum InternalNetworkKeyName implements KeyNameAdapter {
		
		CUSTOMER('C'),
		ASSOCIATE('A');
		
		private char value;
		
		/**
		 * Construtor.
		 * 
		 * @param value
		 */
		private InternalNetworkKeyName(char value) {
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
