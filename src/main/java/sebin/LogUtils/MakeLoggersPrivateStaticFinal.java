package sebin.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakeLoggersPrivateStaticFinal extends DirectoryIterator  {
    private static final String DECLARATION = "private static final ";
    public static final String LOGGER_CLASS_NAME = "Logger";
    private final static Pattern PATTERN_LOGGER_VARIABLE = Pattern.compile("(?m)^.*"
            + LOGGER_CLASS_NAME
            + " \\s*(\\S+)");

    public MakeLoggersPrivateStaticFinal(final String[] args) {
        super(args);
    }

    protected File[] filesToProcess123(final File directory) {
        return new File[]{
                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\AdditionalProcessInfoDaoImpl.java")

        };
    }

    private void changeToPrivateStaticFinal(final File file) throws IOException {
        final JavaFileWithLog javaFileWithLog = new JavaFileWithLog(file);

        String fileContent = javaFileWithLog.fileContent();
        final Matcher matcher = PATTERN_LOGGER_VARIABLE.matcher(fileContent);

        final String loggerVariable= javaFileWithLog.loggerVariable();


        if (matcher.find()){
            final int startIndex = matcher.start();
            final int endIndex = matcher.end();
            final String groupValue = matcher.group();
            final String line = groupValue.trim();
//            System.out.println(line);
            final String stringBeforeLogger = line.substring(0, line.indexOf(LOGGER_CLASS_NAME));
//            System.out.println(stringBeforeLogger);
            final List<String> keywords = Arrays.asList(stringBeforeLogger.split("\\s"));
            if(!keywords.contains("private") ||
                    !keywords.contains("static") ||
                    !keywords.contains("final")){

                final String newVariableDeclaration = groupValue.substring(0,groupValue.indexOf(line))
                        + DECLARATION + LOGGER_CLASS_NAME + " " + loggerVariable;
//                System.out.println(newVariableDeclaration);
                fileContent = matcher.replaceAll(newVariableDeclaration);
                javaFileWithLog.writeToFile(fileContent);
            }
        }
    }

    @Override public void processFile(final File file) {
        try {
            changeToPrivateStaticFinal(file);
        } catch (Exception exception) {
            if(!exception.getMessage().startsWith("No Logger Found!")){
                exception.printStackTrace();
                System.out.println(exception.getMessage() + " :- "+ file.getAbsolutePath());
            }
        }
    }

    public static void main(String args[]){
        new MakeLoggersPrivateStaticFinal(args);

    }
}
