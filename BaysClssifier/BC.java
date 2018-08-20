/**
 * 
 */
package BaysClssifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

/**
 * @author rakib
 *
 */

public class BC {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BC obj = new BC();
		Util objU = new Util();
		
		BufferedReader br= new BufferedReader( new InputStreamReader(System.in));
		
		String trainingFile= obj.getInputFile(br, "Please enter a training file: ");
		String testingFile= obj.getInputFile(br, "Please enter a testing file: ");
		
		Transaction objLT = new Transaction();
		TransactionEntity objET = objLT.loadTransactionFile(trainingFile);
		
		LinkedHashMap<String,Integer> bAttributeNameIndex = objU.getAttributes(objET);
		String predictionColumn = obj.getPredictionColumn(bAttributeNameIndex, br);
		br.close();
		
		if(!predictionColumn.isEmpty()){
			TransactionEntity objNewTE = objU.reorderTransactions(objET, bAttributeNameIndex.get(predictionColumn));
						
			Classification objC = new Classification(objNewTE);
			PriorCountEntity objPriorProbalityEntity = objC.buildTrainingModel();
			
			TransactionEntity objETTest = objU.reorderTransactions(objLT.loadTransactionFile(testingFile),bAttributeNameIndex.get(predictionColumn));
			objC.classify(objPriorProbalityEntity, objETTest);
		}
	}
	
	/**
	 * @param br
	 * @param message
	 * @return the input file
	 */
	private String getInputFile(BufferedReader br, String message){
		String inputFile="";
		try{
			while(true){	
				System.out.println(message);
				inputFile = br.readLine().trim();
								
				if(inputFile.isEmpty()){
					System.out.println("File name cannot be empty.\n");
					continue;
				}else{
					File f = new File(inputFile);
					if(!f.exists()){
						System.out.println("The file does not exist.\n");
						continue;
					}else{
						break;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return inputFile;
	}
	
	/**
	 * @param bAttributeNameIndex
	 * @param br
	 * @return the prediction attribute
	 */
	private String getPredictionColumn(LinkedHashMap<String,Integer> bAttributeNameIndex, BufferedReader br) {
		int index=-1;
		String[] keys = (String [])bAttributeNameIndex.keySet().toArray(new String[bAttributeNameIndex.size()]);
		try{
			System.out.println("Please choose an attribute (by number):");
			int c=1;
			
			for(String column: keys){
				System.out.println("\t"+c+". "+column);
				c++;
			}
			System.out.print("Attribute: ");
			try{
				index = Integer.parseInt(br.readLine().trim());
				if(index<1 || index>=c){
					index=-1;
					System.out.println("Invalid attribute number...");
					return "";
				}
				
			}catch(Exception formatEx){
				System.out.println("Invalid format of attribute number...");
				return "";
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		System.out.println("Target attribute is: "+keys[index-1]+"\n");
		return keys[index-1];
	}

}
