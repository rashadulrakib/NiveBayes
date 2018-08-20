/**
 * 
 */
package BaysClssifier;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author rakib
 *
 */
public class Classification {
	
	TransactionEntity objET;
	
	public Classification(TransactionEntity objET){
		this.objET = objET;
	}

	/**
	 * @return
	 */
	public PriorCountEntity buildTrainingModel() {
		PriorCountEntity objPriorCountEntity = null;
		//LinkedHashMap<String, LinkedHashMap<String,Double>> hmPriorProbalities = new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		//LinkedHashMap<String,Double> hmClassProbilities = new LinkedHashMap<String, Double>();
		
		LinkedHashMap<String,Integer> hmTotalItemCountInClass = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String,LinkedHashMap<String,Integer>> hmIndividualItemCountInClass = new LinkedHashMap<String, LinkedHashMap<String,Integer>>();
		LinkedHashMap<String,Integer> hmUniqueItemCountInClass = null;
		
		try{
			LinkedHashMap<String, ArrayList<ArrayList<String>>> hmSplitTransactions = splitTransactionByTarget(objET.transactions);
			hmUniqueItemCountInClass = uniqueItemCountInClass(objET.transactions);
			
			for(String target : hmSplitTransactions.keySet()){
				
				ArrayList<ArrayList<String>> trans = hmSplitTransactions.get(target);
				int rows = trans.size();
				int cols = trans.get(0).size();
				
				hmTotalItemCountInClass.put(target, rows);
				
				//LinkedHashMap<String,Double> hmIndependentProbabalities = new LinkedHashMap<String, Double>();
				LinkedHashMap<String,Integer> hmIndividualItemCountInSingleClass = new LinkedHashMap<String, Integer>(); 
				
				//double classProb = (double)rows/(double)objET.transactions.size();
				//System.out.println(rows+"/"+objET.transactions.size()+"="+classProb);
				//hmClassProbilities.put(target, classProb);
				
				for(int j=0;j<cols;j++){
					//LinkedHashMap<String,Integer> hmItemCount = new LinkedHashMap<String, Integer>();
					for(int i=0;i<rows;i++){
						String item =  trans.get(i).get(j);
						//if(!hmItemCount.containsKey(item)){
						//	hmItemCount.put(item, 1);
						//}else{
						//	hmItemCount.put(item, hmItemCount.get(item)+1);
						//}
						if(!hmIndividualItemCountInSingleClass.containsKey(item)){
							hmIndividualItemCountInSingleClass.put(item, 1);
						}else{
							hmIndividualItemCountInSingleClass.put(item, hmIndividualItemCountInSingleClass.get(item)+1);
						}	
					}
					
					//double pValue = 1.0/(double)hmItemCount.size();
					
					
					/*for(String item: hmItemCount.keySet()){
						int itemCount = hmItemCount.get(item);
						System.out.println(objET.itemCodes.get(item)+","+itemCount+"/"+rows);
						double mStepProbability = ((double)itemCount+ pValue)/(double)(rows+1);
						hmIndependentProbabalities.put(item, mStepProbability);
					}*/
				}
				
				//hmPriorProbalities.put(target, hmIndependentProbabalities);
				hmIndividualItemCountInClass.put(target, hmIndividualItemCountInSingleClass);
			}
			
			//objPriorProbalityEntity = new PrioProbalityEntity(hmPriorProbalities, hmClassProbilities);
			objPriorCountEntity = new PriorCountEntity(hmTotalItemCountInClass, hmIndividualItemCountInClass, hmUniqueItemCountInClass);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		//return objPriorProbalityEntity;
		return objPriorCountEntity;
	}
	

	/**
	 * @param hmPriorProbalities
	 * @param testingFile
	 */
	public void classify(PriorCountEntity objPriorCountEntity, TransactionEntity objETTest) {
		try{
			int trainRows = countTrainRows(objPriorCountEntity.hmTotalItemCountInClass);
			
			int testrows = objETTest.transactions.size();
			int testcols = objETTest.transactions.get(0).size();
			
			PrintWriter pr = new PrintWriter("result-NB.txt");
			
			for(int j=0;j<testcols;j++){
				//System.out.printf("%-20s",objETTest.columnNames.get(j));
				pr.printf("%-20s",objETTest.columnNames.get(j));
			}
			
			//System.out.printf("%-20s\n","classification");
			pr.printf("%-20s\n","classification");
			
			int correctCount=0;
			for(int i=0;i<testrows;i++){
				String predictionClass="";
				double maxProbability = Double.NEGATIVE_INFINITY;
				for(String target: objPriorCountEntity.hmTotalItemCountInClass.keySet()){
					
					int totalItemsInClass = objPriorCountEntity.hmTotalItemCountInClass.get(target);
					double classProb = (double)totalItemsInClass /(double)trainRows;
					double jointProbaility = Math.log(classProb);
					for(int j=0;j<testcols-1;j++){
						String testItem = objETTest.transactions.get(i).get(j);
						int uniqueItemsInClassForMEstimate = 0;
						if(objPriorCountEntity.hmUniqueItemCountInClass.get(testItem.split(":")[0])!=null){
							uniqueItemsInClassForMEstimate = objPriorCountEntity.hmUniqueItemCountInClass.get(testItem.split(":")[0]);
						}
							
						int tetsItemCount=0;
						if(objPriorCountEntity.hmIndividualItemCountInClass.get(target)!=null && objPriorCountEntity.hmIndividualItemCountInClass.get(target).get(testItem)!=null){
							tetsItemCount = objPriorCountEntity.hmIndividualItemCountInClass.get(target).get(testItem);
						}
						double itemProbability = ((double)tetsItemCount+1/(double)uniqueItemsInClassForMEstimate)/((double)totalItemsInClass+1.0);
						jointProbaility= jointProbaility + Math.log(itemProbability);
					}
					if(jointProbaility>maxProbability){
						maxProbability = jointProbaility;
						predictionClass = target;
					}
				}
				//System.out.println("prediction="+objET.itemCodes.get(predictionClass)+",target="+objET.itemCodes.get(objETTest.transactions.get(i).get(testcols-1)));
				//System.out.println(maxProbability);
				for(int j=0; j<testcols-1;j++){
					String testItem = objETTest.transactions.get(i).get(j);
					//System.out.printf("%-20s",objETTest.itemCodes.get(testItem).split(":")[1]);
					//pr.printf("%-20s",objETTest.itemCodes.get(testItem).split(":")[1]);
					//System.out.printf("%-20s",testItem.split(":")[1]);
					pr.printf("%-20s",testItem.split(":")[1]);
				}
				String groundTruth=objETTest.transactions.get(i).get(testcols-1).split(":")[1].trim();
				String prediction = predictionClass.split(":")[1].trim();
				
				if(groundTruth.equals(prediction)){
					correctCount++;
				}
				
				//System.out.printf("%-20s %-20s\n",groundTruth,prediction);
				pr.printf("%-20s %-20s\n",groundTruth,prediction);
			}
			//System.out.println("\nAccuracy: "+correctCount+"/"+testrows);
			pr.println("\nAccuracy: "+correctCount+"/"+testrows);
			
			pr.close();
			
			System.out.println("The classification result is stored in result-NB.txt");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @param transactions
	 * @return
	 */
	private LinkedHashMap<String, ArrayList<ArrayList<String>>> splitTransactionByTarget(ArrayList<ArrayList<String>> transactions) {
		LinkedHashMap<String, ArrayList<ArrayList<String>>> hmSplitTransactions = new LinkedHashMap<String, ArrayList<ArrayList<String>>>();
		try{
			int rows=transactions.size();
			int cols =transactions.get(0).size();
			for(int i=0;i<rows;i++){
				ArrayList<String> alItems = new ArrayList<String>();
				for(int j=0;j<cols-1;j++){
					alItems.add(transactions.get(i).get(j));
				}
				String target =  transactions.get(i).get(cols-1);
				if(!hmSplitTransactions.containsKey(target)){
					ArrayList<ArrayList<String>> alTrans = new ArrayList<ArrayList<String>>();
					alTrans.add(alItems);
					hmSplitTransactions.put(target, alTrans);
				}else{
					ArrayList<ArrayList<String>> alTrans = hmSplitTransactions.get(target);
					alTrans.add(alItems);
					hmSplitTransactions.put(target, alTrans);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return hmSplitTransactions;
	}
	
	/**
	 * @param transactions
	 * @return
	 */
	private LinkedHashMap<String, Integer> uniqueItemCountInClass(ArrayList<ArrayList<String>> transactions) {
		LinkedHashMap<String, Integer> hmUniqueItemCountInClass = new LinkedHashMap<String, Integer>();
		try{
			int rows=transactions.size();
			int cols =transactions.get(0).size();
			for(int j=0;j<cols-1;j++){
				LinkedHashMap<String, Integer> hmCount = new LinkedHashMap<String, Integer>();
				for(int i=0;i<rows;i++){
					String item = transactions.get(i).get(j);
					if(!hmCount.containsKey(item)){
						hmCount.put(item, 1);
					}else{
						hmCount.put(item, hmCount.get(item)+ 1);
					}
				}
				
				String key = transactions.get(0).get(j).split(":")[0];
				hmUniqueItemCountInClass.put(key, hmCount.size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return hmUniqueItemCountInClass;
	}
	
	/**
	 * @param hmTotalItemCountInClass
	 * @return
	 */
	private int countTrainRows(LinkedHashMap<String, Integer> hmTotalItemCountInClass) {
		int count=0;
		try{
			for(String key: hmTotalItemCountInClass.keySet()){
				count = count + hmTotalItemCountInClass.get(key);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
}
