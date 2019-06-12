package sebin.LogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class DirectoryIterator {
    private static final String FILE_FILTER = ".java";
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryIterator.class);
    public static final String SEBIN_DIR_LOCATION = "P:\\work\\FuturesFuseBRMS\\Applications\\entity";

    public DirectoryIterator(String args[]) {
        final File rootDirectory = parseArgs(args);
        LOGGER.info("Root Directory: {}",rootDirectory.getAbsolutePath());
        parseFiles(filesToProcess(rootDirectory));
    }

    private void parseFiles(final File[] filesToProcess) {
        for(File file: filesToProcess){
            if(file.isDirectory()){
                parseFiles(filesToProcess(file));
            }else if(file.isFile() && file.getName().endsWith(FILE_FILTER)){
                processFile(file);
            }
        }
    }

    /**
     * For testing a single file over-ride this method
     * @param directory
     * @return
     */
    protected File[] filesToProcess(final File directory) {
        return directory.listFiles();
    }

    public abstract void processFile(final File file);

    private File parseArgs(final String[] args) {
        File rootDirectory = new File(SEBIN_DIR_LOCATION);
        if(args.length>0){
            rootDirectory = new File(args[0]);
        }
        if(!rootDirectory.isDirectory()){
            throw new RuntimeException("Provide a valid folder path!" + rootDirectory.getAbsolutePath());
        }
        return rootDirectory;
    }
}
