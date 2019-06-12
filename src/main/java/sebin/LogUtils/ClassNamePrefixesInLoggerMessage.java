package sebin.LogUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassNamePrefixesInLoggerMessage extends DirectoryIterator {

    public ClassNamePrefixesInLoggerMessage(String args[]) {
        super(args);
    }

    public void processFile(final File file) {
        removeClassNameFromLog(file);
    }

    private void removeClassNameFromLog(final File file) {
        try {
            final JavaFileWithLog javaFileWithLog = new JavaFileWithLog(file);
            printFilePathContainsClassNameInTheLogMessage(
                    javaFileWithLog.className(),
                    javaFileWithLog.loggerVariable(),
                    file.getAbsolutePath(),
                    javaFileWithLog.fileContent()
            );
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }

    }

    private void printFilePathContainsClassNameInTheLogMessage(
            final String className,
            final String loggerVariable,
            final String filePath,
            final String fileContent){
        //        final Pattern pattern = Pattern.compile(loggerVariable,Pattern.DOTALL);
        final Pattern pattern = Pattern.compile(loggerVariable +
                "\\s?\\.\\s?.*\\(.*\\\".?"+className,Pattern.DOTALL);

        final Matcher matcher = pattern.matcher(fileContent);
        while(matcher.find()){
            System.out.println(filePath);
        }
    }


    public static void main(String args[]){
        new ClassNamePrefixesInLoggerMessage(args);
    }

}
