import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.sun.xml.internal.ws.util.StringUtils;

/**
 * Class used to store the extracted information of one image
 */
public class ImageBean {
    private Map<String, Double> mFeatureToProbMap; 
    private Object mImageInformation;
    private ImageDatabase mImageDatabase;
    private String mFilePath;
    private String mFileName;
    private static Histogram hist;
    
    public ImageBean(String fileName, String filePath, Object imageInformation, ImageDatabase imageDatabase) {
        mImageInformation = imageInformation;
        mFilePath = filePath;
        mFileName = fileName;
        mFeatureToProbMap = new HashMap<>();
        initialize();
    }
    
    public String getFilePath() {
        return mFilePath;
    }
    
    public String getFileName() {
        return mFileName;
    }
    
    /**
     * Given the information for the image's color
     * features and other extracted information,
     * it calculates a search vector that can be compared
     * to another ImageBean's search vector
     */
    public double calculateSimilarity(ImageBean query) {
        if (mImageDatabase.isExtractingColor()) {
            
        }
        
        if (mImageDatabase.isExtractingFeature()) {
            
        }
        
        if (mImageDatabase.isExtractingText()) {
            
        }
        
        return 0;
    }
    
    private void initialize() {
        //extractFeature();
        extractColor();
        extractText();
    }
    
    /**
     * https://github.com/Clarifai/clarifai-java
     */
    public void extractFeature() {
        mFeatureToProbMap.clear();
        Map<String, Double> mFeatureToProbMap = new HashMap<>();
        ClarifaiClient clarifai = new ClarifaiClient(
                "fcq9IaGGv6G_Pm1yirdPapOa13pYpoamNDLOPX3s", 
                "Xc9qLgll8bxQfr9J1oOsK4iqdf3WgcrkYQ2mzebG");
        
        List<RecognitionResult> results = 
            clarifai.recognize(new RecognitionRequest(new File(getFilePath())));

        for (Tag tag : results.get(0).getTags()) {
            mFeatureToProbMap.put(tag.getName(), tag.getProbability());
        }
    }
    
    public static void extractText() {
        Path testPath = getTestPath("train_text_tags.txt");
        try {
            List<String> lines = Files.readAllLines(testPath, Charset.defaultCharset());
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void extractColor() {
    	double histvalue[];
    	try {
    		BufferedImage img1 = ImageIO.read(new File("/Users/Admin/Documents/NUS/Sem-1-2016-17/CS2108/Assignment/ImageSeach_demo/dataset/0028_1070815604.jpg"));
        	hist = new Histogram();
            histvalue = hist.getHist(img1);
       	 for (int i=0; i<histvalue.length; i++){
    		 System.out.println(histvalue[i]);
    	 }
    	} catch (IOException e) {
    		
    	}
    }
  
    private ArrayList<Double> getSearchVector() {
        return new ArrayList<>();
    }
    
    private static Path getTestPath(String fileName) {
        Path currentRelativePath = Paths.get("").getParent().resolve("ImageData").resolve("train");
        if (fileName != null && !fileName.isEmpty()) {
            currentRelativePath.resolve(fileName);
        }
        return currentRelativePath;
    }
}
