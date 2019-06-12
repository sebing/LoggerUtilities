package sebin.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class LogFileDetails {
    private final String loggerVariable;
    private String fileContent;
    private boolean modifiedFileContent;
    private boolean hasErrors;
    private final List<String> errorMessages = new ArrayList<>();

    public LogFileDetails(final String loggerVariable,
                          final String fileContent) {
        this.loggerVariable = loggerVariable;
        this.fileContent = fileContent;
        this.modifiedFileContent = false;
        this.hasErrors = false;

    }
    public String loggerVariable() {
        return loggerVariable;
    }

    public String fileContent() {
        return fileContent;
    }

    public boolean modifiedFileContent() {
        return modifiedFileContent;
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public List<String> errorMessages() {
        return errorMessages;
    }

    public void setModifiedFileContent(final boolean modifiedFileContent) {
        this.modifiedFileContent = modifiedFileContent;
    }


    public void setFileContent(final String fileContent) {
        this.fileContent = fileContent;
    }

    public void updateErrorMessages(final String message){
        this.hasErrors = true;
        errorMessages.add(message);
    }

}
