package sebin.LogUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConvertToPatternMatchingTest {

    @org.junit.Test
    public void testConvert() {
        final String string = "\"query =\" + query.toString() + \" \" + annualRecItemId";
        assertEquals("\"query ={} {}\"  , query , annualRecItemId",
                new ConvertToPatternMatching(string).convert());

        final String string1 = "QUERY + query.toString() + \" \" + scheduleItem";
        assertEquals("\"{}{} {}\"  , QUERY , query , scheduleItem",
                new ConvertToPatternMatching(string1).convert());

        final String string2 = "\"QUERY - \"+ query.toString() + \" \" + type + \" \" + status";
        assertEquals("\"QUERY - {} {} {}\"  , query , type , status",
                new ConvertToPatternMatching(string2
                ).convert()
        );

        final String string3 =
                "\"Exception in getContractUpdatedDetails while executing the query \" + contractUpdateNativeSql\r\n"
                        + "                + \"-\" + ex.getMessage()";
        assertEquals("\"Exception in getContractUpdatedDetails while executing the query {}-{}\"  , contractUpdateNativeSql , ex.getMessage()",
                new ConvertToPatternMatching(
                string3).convert());
        final String string4 =
                "\"Exception in getContractUpdatedDetails while executing the query\" + contractUpdateNativeSql\r\n"
                        + "                + \"-\" + ex.getMessage()";
        assertEquals("\"Exception in getContractUpdatedDetails while executing the query {}-{}\"  , contractUpdateNativeSql , ex.getMessage()",
                new ConvertToPatternMatching(
                string4).convert());

        final String string5 = "\"query + \"+ query.toString() + \"--  \"+organisationTypeCode";
        assertEquals("\"query + {}--  {}\"  , query , organisationTypeCode",
                new ConvertToPatternMatching(
                        string5).convert());

        final String string6 = "\"query  \"+ query.toString() + \"--  \"+organisationTypeCode";
        assertEquals("\"query  {}--  {}\"  , query , organisationTypeCode",
                new ConvertToPatternMatching(
                        string6).convert());

        final String string7 = "\"Not updated AppScore And Comment from Previous To New Category in App_Aprvl table\"";
        assertEquals(string7,
                new ConvertToPatternMatching(
                        string7).convert());

        final String string8 = "applicationContractOfferDBO.toString()";
        assertEquals("\"{}\" , applicationContractOfferDBO",
                new ConvertToPatternMatching(
                        string8).convert());


    }

    @Test
    public void testCheckForProcessing(){
        final String string8 = "\"Exception while removing removeReductionBPSDetailDBO for appLandParcelId \" + appLandParcelId,\n"
                + "                    e";
        checkForProcessing(string8);

        final String string9 = "\"Exception while removing removeReductionBPSDetailDBO for {} appLandParcelId \" + appLandParcelId,\n"
                + "                    e";
        checkForProcessingWithMessage(string9,"Possible mix of + and Simple Pattern Matching the log::");

        final String string10 = "\"Exception while removing removeReductionBPSDetailDBO for {} appLandParcelId \" , appLandParcelId,\n"
                + "                    e";
        checkForProcessing(string10);
        final String string11 = "\"Exception while removing removeReductionBPSDetailDBO for {} appLandParcelId {}\" , appLandParcelId,\n"
                + "                    e";

        checkForProcessing(string11);

        final String string12 = "\"Exception while removing removeReductionBPSDetailDBO for appLandParcelId \" + appLandParcelId";

        assertEquals("\"Exception while removing removeReductionBPSDetailDBO for appLandParcelId {}\"  , appLandParcelId",
                new ConvertToPatternMatching(string12).convert());

        final String string13 = "\"Exception while removing removeReductionBPSDetailDBO for appLandParcelId {} \" , appLandParcelId";
        checkForProcessing(string13);
    }

    private void checkForProcessingWithMessage(final String string,final String message) {
        try {
            new ConvertToPatternMatching(string).convert();
        } catch (Exception e) {
            assertTrue(e.getMessage().startsWith(message));
        }
    }

    private void checkForProcessing(final String string) {
        checkForProcessingWithMessage(string,"Possible Throwable or Simple Pattern Matching use in the file");
    }

    @Test
    public void testWithMultipleVariables(){
        final String string1 = "\"query = \" + query.toString() + parentLpisLndParcelId + \" \" + childLpisLndParcelId";
        assertEquals("\"query = {}{} {}\"  , query , parentLpisLndParcelId , childLpisLndParcelId" ,
                new ConvertToPatternMatching(string1).convert());

        final String string2 = "query.toString()";
        assertEquals("\"{}\" , query" ,
                new ConvertToPatternMatching(string2).convert());

        final String string3 =  "\"query = \" + query.toString() +  \" \" + childLpisLndParcelId";
        assertEquals("\"query = {} {}\"  , query , childLpisLndParcelId" ,
                new ConvertToPatternMatching(string3).convert());

        final String string4 = "query.toString() + parentLpisLndParcelId + \" \" + childLpisLndParcelId";
        assertEquals("\"{}{} {}\"  , query , parentLpisLndParcelId , childLpisLndParcelId" ,
                new ConvertToPatternMatching(string4).convert());


    }

    @Test
    public void testWithMultipleVariablesInbetween(){
        final String string = "\"Calling getAssessmentStatusCount assessmentId-\" + assessmentId + \"assessmentTypeCode-\"\n"
                + "            + assessmentTypeCode";
        assertEquals( "\"Calling getAssessmentStatusCount assessmentId-{} assessmentTypeCode-{}\"  , assessmentId , assessmentTypeCode" ,
                new ConvertToPatternMatching(string).convert());
    }

    @Test
    public void testTheStringEndsWithVariables(){
        final String string1 = "QUERY2 + query.toString() + \"Query to find the missing supp Doc in Parent from child \"";
        assertEquals("\"{}{} Query to find the missing supp Doc in Parent from child \" , QUERY2  ,  query" ,
                new ConvertToPatternMatching(string1).convert());


        final String string2 = "\"Query to find the missing supp Doc in Parent from child \" + QUERY2 + query.toString() + \"abc\"";
        assertEquals( "\"Query to find the missing supp Doc in Parent from child {}{} abc\" , QUERY2 , query" ,
                new ConvertToPatternMatching(string2).convert());

        final String string3 = "TASKS_SIZE + tasks.size()";
        assertEquals( string3 ,
                new ConvertToPatternMatching(string3).convert());

        final String string4 = "QUERY + query.toString()";
        assertEquals( "\"{}{}\" , QUERY  ,  query" ,
                new ConvertToPatternMatching(string4).convert());

        final String string5 = "\"aaaa \"+bbbbb+\" cccc\"+ddddd+\"eeeeeee\"";
        assertEquals( "\"aaaa {} cccc {} eeeeeee\" , bbbbb , ddddd" ,
                new ConvertToPatternMatching(string5).convert());


        final String string = "\"Query to find the missing supp Doc in Parent from child \" + QUERY2 + query.toString()";
        assertEquals( "\"Query to find the missing supp Doc in Parent from child {}{}\" , QUERY2 , query" ,
                new ConvertToPatternMatching(string).convert());
   }

    @Test
    public void testTheStringEndsWithSimpleParameters(){
        final String string1 = "\"Find Reduction {} application id: {}\", applicationId";
        try {
                    new ConvertToPatternMatching(string1).convert();
        } catch (Exception e) {
            assertEquals( "Possible Throwable or Simple Pattern Matching use in the file: in the log::"
                    + " \"Find Reduction {} application id: {}\", applicationId" ,e.getMessage());
        }

        final String string2 = "\"Find Reduction {} application id: {}\", abc, applicationId";
        assertEquals( string2 ,
                new ConvertToPatternMatching(string2).convert());

        final String string = "\"Find Reduction By application id: {}\", applicationId";
        assertEquals( string ,
                new ConvertToPatternMatching(string).convert());

    }

    @Test
    public void testMixOfConcatAndSimpleParams() {
        final String string = "QUERY + query.toString() + \" [annualRecurrentRedPayRateBandId: {}]\", annualRecurrentRedPayRateBandId";
        try {
                new ConvertToPatternMatching(string).convert();
        } catch (Exception e) {
            assertEquals( "Possible mix of + and Simple Pattern Matching the log::"
                    + " QUERY + query.toString() + \" [annualRecurrentRedPayRateBandId: {}]\","
                    + " annualRecurrentRedPayRateBandId" ,e.getMessage());
        }
    }
}