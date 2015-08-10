package org.helianto.qualifier;

import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import org.helianto.core.internal.KeyNameAdapter;
import org.helianto.core.internal.QualifierAdapter;

/**
 * Abstract qualifier adapter list factory.
 * 
 * @author mauriciofernandesdecastro
 */
public abstract class AbstractQualifierAdapterList 
	implements QualifierAdapterList
{
	
	protected abstract KeyNameAdapter[] getQualifierArray();
	
	public List<QualifierAdapter> getQualifierList() {
		List<QualifierAdapter> qualifierAdapterList = new ArrayList<QualifierAdapter>();
		for (KeyNameAdapter qualifier: getQualifierArray()) {
			qualifierAdapterList.add(new QualifierAdapter(qualifier));
		}
		return qualifierAdapterList;
	}

	/**
	 * Required to qualify injection.
	 * 
	 * @author mauriciofernandesdecastro
	 */
	@java.lang.annotation.Documented
	@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
	@javax.inject.Qualifier
	public @interface NetworkKeyNameAdapter {}

	/**
	 * Required to qualify injection.
	 * 
	 * @author mauriciofernandesdecastro
	 */
	@java.lang.annotation.Documented
	@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
	@javax.inject.Qualifier
	public @interface UserKeyNameAdapter {}

}
