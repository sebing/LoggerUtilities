package sebin.LogUtils;

import java.io.File;
import java.io.IOException;

public class LoggerToUpperCase extends DirectoryIterator {
    public LoggerToUpperCase(final String[] args) {
        super(args);
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
    private void changeToPrivateStaticFinal(final File file) throws IOException {
        final JavaFileWithLog javaFileWithLog = new JavaFileWithLog(file);

        String fileContent = javaFileWithLog.fileContent();

        final String loggerVariable = javaFileWithLog.loggerVariable();
        if (!loggerVariable.toUpperCase().equals(loggerVariable)){
            fileContent = fileContent.replaceAll(loggerVariable,loggerVariable.toUpperCase());
            javaFileWithLog.writeToFile(fileContent);
        }

    }

    protected File[] filesToProcess123(final File directory) {
        return new File[]{
                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\AdditionalProcessInfoDaoImpl.java")

        };
    }

    public static void main(String args[]){
        new LoggerToUpperCase(args);

    }


}
