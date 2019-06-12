package sebin.LogUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaFileWithLog {
    private final static Pattern PATTERN_LOGGER_VARIABLE = Pattern.compile("Logger \\s*(\\S+)",Pattern.DOTALL);
    private final static Pattern PATTERN_STRING_VARIABLE = Pattern.compile("\\s+String\\s+(\\S+)",Pattern.DOTALL);

    private final String fileContent;
    private final File file;
    public JavaFileWithLog(final File file) throws IOException {
        this.file = file;
        this.fileContent = FileUtils.readFileToString(file, "UTF-8");
    }

    public boolean isCodeContainsSystemOut(){
        return fileContent.indexOf("System.out") != -1;
    }

    public boolean isCodeContainsTodo(){
        return fileContent.indexOf("TODO") != -1;
    }

    public String loggerVariable() {
        final Matcher matcher = PATTERN_LOGGER_VARIABLE.matcher(fileContent);
        final boolean result = matcher.find();
        if(!result){
            throw new RuntimeException("No Logger Found! " + file.getAbsolutePath());
        }
        if (matcher.groupCount() ==1) {
            return matcher.group(0).split("\\s+")[1].trim();
        }
        throw new RuntimeException("More than one Logger Found! " + file.getAbsolutePath());
    }

    public List<String> stringVariables(){
        final Matcher matcher = PATTERN_STRING_VARIABLE.matcher(fileContent);
        final List<String> stringVariables = new ArrayList<String>();
        while ((matcher.find())){
            stringVariables.add(extractStringVariable(matcher.group()));
        }
        return stringVariables;
    }

    private String extractStringVariable(final String string) {
        final String stringVariablePrefix = "String ";
        final String variable = string.trim().replaceAll("[^a-zA-Z0-9_-]", "");
        return variable.substring(stringVariablePrefix.length()-1);
    }

    public static void main(String args[]) throws IOException {
          File file = new File("P:\\work\\FuturesFuseBRMS\\Applications\\"
                  + "entity\\src\\main\\java\\uk\\gov\\scotland\\afrc\\applications\\dao\\impl\\"
                  + "AnnualRecurrentItemDaoImpl.java");
          new JavaFileWithLog(file).stringVariables();
;
    }

    public String fileContent() {
        return fileContent;
    }

    public String className(){
        return FilenameUtils.removeExtension(file.getName());
    }

    public void writeToFile(final String convertLogMessages) throws IOException {
        FileUtils.write(file,convertLogMessages,"UTF-8");
    }
}
