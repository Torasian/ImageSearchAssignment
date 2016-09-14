import java.nio.file.Path;
import java.nio.file.Paths;


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
}
