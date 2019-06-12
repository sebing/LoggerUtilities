package sebin.LogUtils;

import java.io.File;
import java.io.IOException;

public class RemoveTodo extends DirectoryIterator   {

    public RemoveTodo(final String[] args) {
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
            final boolean codeContainsTodo = javaFileWithLog.isCodeContainsTodo();
            if(codeContainsTodo){
                final String newFileContent = removeTodo(javaFileWithLog.fileContent());
                if(newFileContent.indexOf("TODO") != -1){
                    System.out.println(file.getAbsolutePath());
                }
                if(!javaFileWithLog.fileContent().equals(newFileContent)){
                    javaFileWithLog.writeToFile(newFileContent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String removeTodo(final String fileContent) {
        final String newString = fileContent.replaceAll(".*// TODO Auto-generated method stub.*(\\n|\\r\\n|$)", "");
        return newString.replaceAll(".*// TODO Auto-generated constructor stub.*(\\n|\\r\\n|$)", "");
    }

    public static void main(String args[]){
        new RemoveTodo(args);
    }
}
