/**
 * 
 */
package BaysClssifier;

import java.util.LinkedHashMap;

/**
 * @author rakib
 *
 */
public class PriorCountEntity {
	LinkedHashMap<String,Integer> hmTotalItemCountInClass;
	LinkedHashMap<String,LinkedHashMap<String,Integer>> hmIndividualItemCountInClass;
	LinkedHashMap<String,Integer> hmUniqueItemCountInClass; //for m-estimate
	
	public PriorCountEntity(LinkedHashMap<String,Integer> hmTotalItemCountInClass, LinkedHashMap<String,LinkedHashMap<String,Integer>> hmIndividualItemCountInClass, LinkedHashMap<String,Integer> hmUniqueItemCountInClass){
		this.hmTotalItemCountInClass = hmTotalItemCountInClass;
		this.hmIndividualItemCountInClass = hmIndividualItemCountInClass;
		this.hmUniqueItemCountInClass = hmUniqueItemCountInClass;
	}
}
