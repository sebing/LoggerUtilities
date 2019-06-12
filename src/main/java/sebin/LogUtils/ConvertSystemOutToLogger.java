package sebin.LogUtils;

import java.io.File;
import java.io.IOException;

public class ConvertSystemOutToLogger extends DirectoryIterator   {

    public ConvertSystemOutToLogger(final String[] args) {
        super(args);
    }
    protected File[] filesToProcess123(final File directory) {
        return new File[]{
                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\AdditionalProcessInfoDaoImpl.java"),
                new File("P:\\work\\FuturesFuseBRMS\\Applications\\entity\\src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\ReductionRuleLogDaoImpl.java")

        };
    }

    @Override
    public void processFile(final File file) {
        try {
            final JavaFileWithLog javaFileWithLog = new JavaFileWithLog(file);
            final boolean codeContainsSystemOut = javaFileWithLog.isCodeContainsSystemOut();
            if(codeContainsSystemOut){
                System.out.println(file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        new ConvertSystemOutToLogger(args);
    }
}
