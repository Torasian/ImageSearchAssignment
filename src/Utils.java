import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Utils {
    public static Path getRelativePath() {
        return Paths.get("").toAbsolutePath();
    }
    
    public static Path getTestPath(String fileName) {
        Path currentRelativePath = getRelativePath();
        currentRelativePath = currentRelativePath.getParent();
        currentRelativePath = currentRelativePath.resolve("ImageData");
        currentRelativePath = currentRelativePath.resolve("train");
        if (fileName != null && !fileName.isEmpty()) {
            currentRelativePath = currentRelativePath.resolve(fileName);
        }
        return currentRelativePath;
    }
    
    public static Path getFeaturePath(String fileName) {
        Path currentRelativePath = getRelativePath();
        currentRelativePath = currentRelativePath.resolve("feature");
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("filename is invalid");
        }
        currentRelativePath = currentRelativePath.resolve(fileName);
        return currentRelativePath;
    }
}
