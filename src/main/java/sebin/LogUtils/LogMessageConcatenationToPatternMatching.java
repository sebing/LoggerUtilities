package sebin.LogUtils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMessageConcatenationToPatternMatching extends DirectoryIterator  {

    public LogMessageConcatenationToPatternMatching(final String[] args) {
        super(args);
    }

    public void processFile(final File file) {
        processConcatenatedString(file);
    }

    protected File[] filesToProcess987(final File directory) {
        try {
            final List<File> listOfFiles = new LoadFileNames("P:\\AreLoggerUtilities\\"
                    + "src\\main\\resources\\LogMessageConcatenationToPatternMatching.txt").listOfFiles();
            return listOfFiles.toArray(new File[listOfFiles.size()]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected File[] filesToProcess123(final File directory) {
        return new File[]{
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\"
//                        + "entity\\src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\"
//                        + "AnnualRecurrentItemDaoImpl.java"),
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\"
//                        + "src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\LocationDaoImpl.java"),
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\"
//                        + "src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\"
//                        + "AnnualRecurrentRotationYrDaoImpl.java"),
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\"
//                        + "java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\AppValidationErrDaoImpl.java"),
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java"
//                        + "\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\ClaimDaoImpl.java"),
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java"
//                        + "\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\SchemeApprovalGroupDaoImpl.java"),
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\"
//                        + "java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\AdditionalProcessInfoDaoImpl.java"),
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java\\uk\\gov\\scotland\\"
//                        + "afrc\\applications\\dao\\impl\\AdjustmentValueDaoImpl.java"),
//                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\"
//                        + "main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\AreaOfficeTypeDaoImpl.java"),
                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java\\"
                        + "uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\ReductionBPSDaoImpl.java"),
                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\"
                        + "src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\ReductionGreeningDaoImpl.java")
        };
    }

    private void processConcatenatedString(final File file)  {
        try {
//            System.out.println("File:" + file.getName());
            final JavaFileWithLog javaFileWithLog = new JavaFileWithLog(file);
            final LogFileDetails logFileDetails = convertLogMessages(
                    new LogFileDetails(
                            javaFileWithLog.loggerVariable(),
                            javaFileWithLog.fileContent()
                    ));
            if(logFileDetails.hasErrors()){
                System.out.println(file.getAbsolutePath());
                System.out.println(logFileDetails.errorMessages());
            }
            if(logFileDetails.modifiedFileContent()){
                javaFileWithLog.writeToFile(logFileDetails.fileContent());
            }
        } catch (Exception exception) {
            if(!exception.getMessage().startsWith("No Logger Found!")){
//                exception.printStackTrace();
                System.out.println(exception.getMessage() + " :- "+ file.getAbsolutePath());
            }
        }
    }

    private LogFileDetails convertLogMessages(LogFileDetails logFileDetails){
        final String regex = logFileDetails.loggerVariable() + "\\s?\\.\\s?.*?\\(";
//        System.out.println(regex);
        final Pattern pattern = Pattern.compile(regex);
        int occurrence = 0;
        outerLoop:
        while (true){
            final Matcher matcher = pattern.matcher(logFileDetails.fileContent());
            for (int i=0;matcher.find();i++){
                if(i>occurrence){
                    occurrence++;
                    continue outerLoop;
                }
                if(i<occurrence){
                    continue ;
                }
//                System.out.println(matcher.group());
                //Skip if it is LOGGER.error
                Pattern errorPattern = Pattern.compile("\\.\\s*error");
                Matcher errorMatcher = errorPattern.matcher(matcher.group());
                if(errorMatcher.find()){
                    occurrence++;
                    continue outerLoop;
                }
//                            System.out.println(matcher.start() + " --- "  + matcher.end());
//                            System.out.println(fileContent.substring(matcher.start(),matcher.end()));
                final int endIndex = matcher.end();
                final String stringToCovert = logFileDetails.fileContent().substring(
                        endIndex,
                        closingParenthesisLocation(logFileDetails.fileContent(), endIndex - 1)
                );
                if(StringUtils.isBlank(stringToCovert)){
                    continue;
                }

//                skipFiles(stringVariables, filePath, stringToCovert);
                final String convertedString;
                try {
                    convertedString = new ConvertToPatternMatching(stringToCovert).convert();
                } catch (Exception exception) {
                    logFileDetails.updateErrorMessages(exception.getMessage());
                    continue;
                }

                if(!stringToCovert.equals(convertedString)){
//                                    System.out.println(stringToCovert);
//                                    System.out.println(convertedString);
                    logFileDetails.setFileContent(logFileDetails.fileContent().substring(0, endIndex)
                            + convertedString
                            + logFileDetails.fileContent().substring((endIndex+stringToCovert.length())));
                    logFileDetails.setModifiedFileContent(true);

                    //                System.out.println(fileContent);
                    //                System.exit(0);
                }

            }
            break;
        }

        return logFileDetails;
    }

    private void skipFiles(final List<String> stringVariables, final String filePath, final String stringToCovert) {
        final String[] parts = stringToCovert.split("\\+");
        //                System.out.println(Arrays.asList(parts));
        //                System.out.println(stringVariables);
        for (String part : parts) {
            if(stringVariables.contains(part.trim())){
                final String message = "Variable::"+ part + " In file::" + filePath;
//                System.out.println(message);
                throw new RuntimeException("Variable concatenation, need to do manually");
            }
        }
    }

    private int closingParenthesisLocation(
            final String string, int startingLocation) {

        int counter = 0;
        final char opening = '(';
        final char closing = ')';
        int positionOfMatchingParen = -1;
        boolean found = false;

        while (startingLocation < string.length() && !found) {

            if (string.charAt(startingLocation) == (opening)) {
                counter++;
            } else if (string.charAt(startingLocation) == (closing)) {
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
        new LogMessageConcatenationToPatternMatching(args);
//        //LOGGER.info("AppValidationErrDBO.findByApplicationAndValTypeCatName query  : " + query + "For application id : "
//        //            + applicationId + "Catogory name" + catName);
//
//        final String string1 = "\"AppValidationErrDBO.findByApplicationAndValTypeCatName query  : \" + query + \"For application id : \"\n"
//                + "            + applicationId + \"Catogory name\" + catName";
//
//
//        System.out.println(convertToPatternMatching(string1));
//        System.out.println(convertToPatternMatching("\"Just some string \""));
//
//        final String string2 = "\"AppValidationErrDBO.findByApplicationAndValTypeCatName query  : \" + query + \"For application id : \"\n"
//                + "            + applicationId + \"Catogory name\" ";
//
//        System.out.println(convertToPatternMatching(string2));

    }


}
