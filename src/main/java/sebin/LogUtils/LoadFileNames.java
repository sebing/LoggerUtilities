package sebin.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class LoadFileNames {
    private final String fileLocation;

    public LoadFileNames(final String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public List<File> listOfFiles() throws IOException {

        return Files.lines(Paths.get(fileLocation))
                .map(str -> new File(str))
                .collect(Collectors.toList());
    }
}
