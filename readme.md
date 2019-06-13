### Task:Logging Improvements:Replace all usages of log4j with slf4j
   e.g. `import org.apache.log4j.Logger;` ->` import org.slf4j.Logger;`  
   Add `import org.slf4j.LoggerFactory;`  
   `Logger.getLogger(...)` -> `LoggerFactory.getLogger(...)`  

Solution:  
  run this command (the jar is located in the src\main\resources folder)
   `java -jar slf4j-migrator-1.8.0-beta4.jar`


### Task: Remove class name prefixes from logging statements
   e.g.   
   `LOG.info("In PayCalcEventProcessorImpl.execute BatchEventId:{}", batchEvent.getBatchEventId());`  
   ->  
   `LOG.info("In execute BatchEventId:{}", batchEvent.getBatchEventId());`  
  
   Solution:  
 - Run the class `ClassNamePrefixesInLoggerMessage` with the root folder as the parameter 
 - e.g. `java ClassNamePrefixesInLoggerMessage p:\Futures_Git\FuturesFuseBRMS\Entitlement\integration`
 - It will list all the classes need analysis.

### Make the Logger variable `private static final`
   e.g.  
   `private static Logger logger = LoggerFactory.getLogger(AdditionalProcessInfoDao.class);`  
   ->   
   `private static final Logger logger = LoggerFactory.getLogger(AdditionalProcessInfoDao.class);`  
   
#### Solution
 - Run the class `MakeLoggersPrivateStaticFinal` with the root folder as the parameter 
 - e.g. `java MakeLoggersPrivateStaticFinal p:\Futures_Git\FuturesFuseBRMS\Entitlement\integration`

### Make the Logger variable to uppercase
   e.g.  
   `private static final Logger logger = LoggerFactory.getLogger(AdditionalProcessInfoDao.class);`  
   ->   
   `private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalProcessInfoDao.class);`  
   
#### Solution
 - Run the class `LoggerToUpperCase` with the root folder as the parameter 
 - e.g. `java LoggerToUpperCase p:\Futures_Git\FuturesFuseBRMS\Entitlement\integration`


### Changing the String Concatenation to simple pattern matching parameters in the logger
e.g.
   ```
   LOGGER.info("AppValidationErrDBO.findByApplicationAndValTypeCatName query  : " + query + "For application id : "
            + applicationId + "Catogory name" + catName);
            ```   
   ->  
   ```
   LOGGER.info("AppValidationErrDBO.findByApplicationAndValTypeCatName query  : {} For application id {} : Catogory name {}",
   query, applicationId, catName);
   ```  

  Solution:  
  - Run the class `LogMessageConcatenationToPatternMatching` with the root folder as the parameter 
    + e.g. `java LogMessageConcatenationToPatternMatching p:\Futures_Git\FuturesFuseBRMS\Entitlement\integration`
  - Warning: the skipped files are listed with the reason.
    + e.g. 
    
     ```
     P:\work\FuturesFuseBRMS\Applications\entity\src\main\java\uk\gov\scotland\afrc\applications\dao\impl\AnnualRecurrentReductionPayRateBandDaoImpl.java
     [Possible mix of + and Simple Pattern Matching the log:: QUERY + query.toString() + " [annualRecurrentRedPayRateBandId: {}]", annualRecurrentRedPayRateBandId, Possible mix of + and Simple Pattern Matching the log:: QUERY + query.toString() + " [applicationId: {}, reducedFlag: {}]", applicationId, true, Possible mix of + and Simple Pattern Matching the log:: QUERY + query.toString() + " [applicationId: {}, reducedFlag: {}]", applicationId, true]

     ```
  - Kind of error messages:
       + `Possible mix of + and Simple Pattern Matching` When the mix of string concatenation and the simple pattern matching, we need to fix them manually.
       + `Possible Throwable or Simple Pattern Matching` These lines of code should be re-viewed to make sure they are `LOGGER.error`


### Ensure loggers have the correct number of arguments
e.g. LOG.info("Parent Childs : {} {}", lpisLandParcelDBO.getPrclId()); ? LOG.info("Parent Childs : {}", lpisLandParcelDBO.getPrclId());

#### Solution
 - Run the class `VerifyCorrectNumberOfArguments` with the root folder as the parameter 
 - e.g. `java VerifyCorrectNumberOfArguments p:\Futures_Git\FuturesFuseBRMS\Entitlement\integration`
 - Correct the listed files with the log messages manually. (Note: this script won't modify the code.)


### Remove all TODO statements
 - Run the class `RemoveTodo` with the root folder as the parameter 
 - e.g. `java RemoveTodo p:\Futures_Git\FuturesFuseBRMS\Entitlement\integration`
   + The script will remove 
   ```
    // TODO Auto-generated method stub
    // TODO Auto-generated constructor stub
   ```
 - Examine the listed files: 
 if the TODO instructs a concrete action is taken to address some kind of design issue/technical debt then please create a Tech Debt Jira with the contensts of the TODO along with details of the class it was found in.



## Working on:




## Todo:


#### Replace all System.out calls in production code with log.info`
e.g. `System.out.print("Soap Response: "); -> LOG.info("Soap Response: ");

Ensure System.out calls in test classes are replaced with assertions
e.g.
```
System.out.println(contractDocumentObj.toString()); 
-> 
assertEquals("Example Expected Document", contractDocumentObj.toString());
```




#### Unit Test Migration (testNg to Junit):
Example search replace terms *not an exhaustive list:   
```
import static org.testng.Assert.assertFalse; -> import static org.junit.Assert.assertFalse;
import static org.testng.Assert.assertTrue; -> import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals; -> import static org.junit.Assert.assertEquals;
import static org.testng.Assert.fail; -> import static org.junit.Assert.fail;
import static org.testng.Assert.assertSame; -> import static org.junit.Assert.assertSame;
import static org.testng.Assert.assertNull; -> import static org.junit.Assert.assertNull;
import static org.testng.Assert.assertNotNull; -> import static org.junit.Assert.assertNotNull;
import org.testng.Assert; -> import org.junit.Assert;
```
To verify that all usages of testNg have been migrated do a "find in path"/grep in the sub-module for "testng"
When all usages have been migrated, finally remove the testNg dependancy from the sub-module pom.xml

#### Code Format Improvements:
Remove all commented out code lines
e.g. // ApplicationTypeDBO applicationType =


Before submitting for review you must:
1.Include a before and after screenshot of the unit test reuslts for the sub-module e.g. Applications\entity
2.Include a before and after screenshot of "mvn clean compile test" for the sub-module e.g. Applications\entity
3. Include a link to the Merge Request in GitLab
