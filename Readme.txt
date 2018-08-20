README file
rakib, B00598853, rakib@cs.dal.ca
==============================================================
--------------------------Naive Bays----------------
===============================================================

- What is this program for?
 	This program is an implementation of the Naive Bayes Classifier in Java.
	
- Directory Info
	-The NaiveBays classifier is stored in the folder "NaiveBays/BaysClssifier" where "BaysClssifier" is the package folder
	- Train and Test files should be in the folder  "NaiveBays/"
	- makefile is in the folder  "NaiveBays/"
	- You should compile and run the program under the folder "NaiveBays/"

-- Assumptions on Data Format
	1. The database file is space delimited. (e.g. "North America" is not allowed. Instead of that "North_America" is ok)
	2. The target attribute must have the values of at least two class labels
	3. Missing value should be represented as empty string (e.g., space)
	
- Compile the program inside "NaiveBays/" (in Unix environment)
	$make (or javac BaysClssifier/*.java)

- Run the program
	$java BaysClssifier.BC

- Check Result 
	$cat result-NB.txt (located in the folder "NaiveBays/")

- Clean compiled files and output results
	$make clean

- Overview the program code:
	There are totally 6 Java source code files, the description of the core methods (excluding constructors) are given below.
	
	BC.java		-----		main (): entry point of the whole program, getInputFile() : read the train and test file
	Transaction.java ----	loadTransactionFile(): Loads the train and test file as transactions. 
	TransactionEntity.java ---- It contains the properties of the transactions
	Util.java ---- getAttributes(): Determine the attributes for prediction. reorderTransactions(): reorder the transactions based on the target attribute which is placed at the right most column
	PriorCountEntity.java ---- It contains the properties of the Proior counts of each attribute from training data
	Classification.java ---- It contains the core function for classification. buildTrainingModel(): builds the training model based on the counts of the attribute values in training data. classify(): Classifies the test data. 
	

- The following is the program structure:
	
	main() 	---->	read input 
				----> check file exist 
				----> Determine the target attribute

			
			----->	building databases for both training data and testing data 
				
			----->	Build Training model using the prior counts of each attribute values
		
		 		
			----->	Using the Training model, do prediction of target attribute on testing data
			
			----->	output the result into "result-NB.txt" file 


==============================================================
--------------------------Data Cleaning----------------
===============================================================
- What is this program for?
 	This program is an implementation for Data Cleaning in Java.

- Manual Data cleaning in the training file
	-Map the column names of training file into the column names of testing file (e.g., change the column name "PlayTennis" in training file into "Class" when "class" represents the "PlayTennis" in the test file)  
	-Map the values of training file into the values of testing file (e.g., "n" is replaced by "no" and "p" is replaced by "yes")
	
- Dynamic Data cleaning (by the program) in the training file
	- If there are inconsistent instances in the training file (e.g., there are different labels for all instances where each instance is same). There are two possible cases:
		a) there is tie (e.g., 5 instances have label "yes", 5 instances have label "no"). We do not do anything
		b) No tie (e.g., 8 instances have label "yes", 10 instances have label "no"). then we compute the ratio (e.g., 8/10=0.8). If the ratio is >=0.8. Then we apply majority voting (e.g., replace the class label "yes" by "no") 
	
- Directory Info
	-The Data Cleaning module is stored in the folder "DataCleaning/DataClean" where "DataClean" is the package folder
	- Train file should be in the folder  "DataCleaning/"
	- You should compile and run the program under the folder "DataCleaning/" 
	
- Compile the program inside "DataCleaning/" (in Unix environment)
	(javac DataClean/*.java)

- Run the program
	$java DataClean.DC

- Check Result 
	$cat cleaned-tarin (located in the folder "DataCleaning/")
	
- Core Class and Function
		The core class is in DataCleanUtil.java and core method is cleanData().
		
- The cleaned training file "cleaned-tarin" can be used as the training file for the Naive Bayes classifier.		


==============================================================
--------------------------Bonus Question #1 (ID3 Classifier)----------------
===============================================================
- What is this program for?
 	This program is an implementation for ID3 classifier in Java.
	
- Directory Info
	-The ID3 classifier is stored in the folder "ID3/DT" where "DT" is the package folder
	- Train and Test files should be in the folder  "ID3/"
	- You should compile and run the program under the folder "ID3/"
	
- Compile the program inside "ID3/" (in Unix environment)
	javac DT/*.java

- Run the program
	$java DT.ID3

- Check Result 
	$cat result-ID3.txt (located in the folder "ID3/")

- Clean compiled files and output results
	$make clean

- Overview the program code:
	There are totally 6 Java source code files, the description of the core methods (excluding constructors) are given below.
	
	ID3.java		-----		main (): entry point of the whole program, getInputFile() : read the train and test file
	Transaction.java ----	loadTransactionFile(): Loads the train and test file as transactions. 
	TransactionEntity.java ---- It contains the properties of the transactions
	Util.java ---- getAttributes(): Determine the attributes for prediction. reorderTransactions(): reorder the
					transactions based on the target attribute which is placed at the right most column
	Predict.java ---- It contains the core function for classification. GenerateDT(): Generates the ID3 Decision Tree Model.
					 Classify(): Classifies the test data. getID3Rules(): Returns the generated rules. 
	
	

- The following is the program structure:
	
	main() 	---->	read input 
				----> check file exist 
				----> Determine the target attribute

			
			----->	building databases for both training data and testing data 
				
			----->	Build ID3 Training model
		
		 		
			----->	Using the Training model, do prediction of target attribute on testing data
			
			----->	output the result into "result-ID3.txt" file 



==============================================================
--------------------------Bonus Question #2 (Comparison between ID3 and Naive Bayes )----------------
===============================================================			
- I have used the iris-data(http://archive.ics.uci.edu/ml/datasets/Iris). randomly choose ~70% data for training (iris-train) and ~30% data for testing (iris-test)
-ID3 classifier performs better than Naive Bayes classifier. Accuracy of ID3=39/46 and NaiveBays=38/46.
-Why ID3 is better than NaiveBays on iris-data?
	- iris is an structured data set. And for structured data-set model based prediction (e.g., ID3) performs better than the instance based prediction (NaiveBays)
	- The dimension of iris-data is small (e.g., only 5 attributes) and for the data-set with small dimension, model based prediction (e.g., ID3) performs better than the instance based prediction (NaiveBays)
	- NaiveBays assumes that the instances are independent. On the other hand, ID3 uses the attribute importance for prediction.
	- NaiveBays uses all attribute values to predict the class of an instance where as ID3 uses only the important attributes to predict the class of an instance.
	
-My Observation
	- Using ID3 we can do reasoning the prediction result. because ID3 shows the prediction rules explicitly
	- Using NaiveBays reasoning the prediction result is not possible
	
	-NaiveBays is a fast classifier where as ID3 is slower than that

	-NaiveBays works better than ID3 for high-dimension data (e.g., text classification)	

	-NaiveBays is simple and easy to implement, ID3 implementation is quite complex than that
	
	-When reasoning is not important (e.g., in text classification, reasoning is ignored), NaiveBays is a good choice than ID3 and vice versa
	
	-For structured and low dimension data, ID3 is better and vice versa
-  