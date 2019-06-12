package sebin.LogUtils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyCorrectNumberOfArguments extends DirectoryIterator  {

    public VerifyCorrectNumberOfArguments(final String[] args) {
        super(args);
    }
    protected File[] filesToProcess123(final File directory) {
        return new File[]{
                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\AdditionalProcessInfoDaoImpl.java"),
                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\ReductionRuleLogDaoImpl.java")

        };
    }

    @Override public void processFile(final File file) {
        verify(file);

    }

    private void verify(final File file) {
        try {
            //            System.out.println("File:" + file.getName());
            final JavaFileWithLog javaFileWithLog = new JavaFileWithLog(file);
            verifyLogMessages(
                    javaFileWithLog.loggerVariable(),
                    javaFileWithLog.stringVariables(),
                    file.getAbsolutePath(),
                    javaFileWithLog.fileContent()
            );
        } catch (Exception exception) {
            if(!exception.getMessage().startsWith("No Logger Found!")){
                exception.printStackTrace();
                System.out.println(exception.getMessage() + " :- "+ file.getAbsolutePath());
            }
        }
    }

    private void verifyLogMessages(final String loggerVariable,
                                     final List<String> stringVariables,
                                     final String absolutePath,
                                     final String fileContent) {
        final String regex = loggerVariable + "\\s?\\.\\s?.*?\\(";
        final Pattern pattern = Pattern.compile(regex);
        int occurrence = 0;
        outerLoop:
        while (true){
            final Matcher matcher = pattern.matcher(fileContent);
            for (int i=0;matcher.find();i++){
                if(i>occurrence){
                    occurrence++;
                    continue outerLoop;
                }
                if(i<occurrence){
                    continue ;
                }
                //Skip if it is LOGGER.error
                if(matcher.group().matches("\\.\\s*error")){
                    continue outerLoop;
                }
                final int endIndex = matcher.end();

                final String stringToCovert = fileContent.substring(
                        endIndex,
                        closingParenthesisLocation(fileContent, endIndex - 1)
                );
                if(StringUtils.isBlank(stringToCovert)){
                    continue ;
                }
                if(stringToCovert.indexOf("{}") == -1){
                    continue ;
                }

                boolean numberOfSimplePatternsMatches =
                        new VerifySimplePattern(stringToCovert).isNumberOfSimplePatternsMatches();
                if(!numberOfSimplePatternsMatches){
                    System.out.println(absolutePath);
                    System.out.println(stringToCovert);
                }

//                final int start = stringToCovert.indexOf('{');
//                if(start>=0){
//
////                    System.out.println(st);
//
////                    final int end = closingBraceLocation(stringToCovert, start);
////                    if((end-start) != 1){
////                        System.out.println(start + "-"+ end + "::"+ stringToCovert.substring(start,end));
////                    }
//                }

            }
            break;
        }

    }

    private int closingParenthesisLocation(final String string, int startingLocation) {

        final char openingChar = '(';
        final char closingChar = ')';

        return findClosingCharLocation(string, startingLocation, openingChar, closingChar);
    }

    private int findClosingCharLocation(final String string, int startingLocation, final char openingChar,
                                        final char closingChar) {
        int positionOfMatchingParen = -1;
        boolean found = false;
        int counter = 0;

        while (startingLocation < string.length() && !found) {

            if (string.charAt(startingLocation) == (openingChar)) {
                counter++;
            } else if (string.charAt(startingLocation) == (closingChar)) {
                counter--;
                if (counter == 0) {
                    positionOfMatchingParen = startingLocation;
                    found = true;
                }
            }
            startingLocation++;
        }
        return positionOfMatchingParen;
    }

    public static void main(String args[]){

        new VerifyCorrectNumberOfArguments(args);
    }

}
