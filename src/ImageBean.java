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
import java.util.stream.Collectors;
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
    private static double MINIMUM_PROB = 0.90;
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
    
    public Map<String, Double> getFeatureMap() {
        return mFeatureToProbMap;
    }
    
    /**
     * Given the information for the image's color
     * features and other extracted information,
     * it calculates a search vector that can be compared
     * to another ImageBean's search vector
     */
    public double calculateSimilarity(ImageBean query) {
        double similarityValue = 0;
        if (mImageDatabase.isExtractingColor()) {
            
        }
        
        if (mImageDatabase.isExtractingFeature()) {
            similarityValue += compareFeature(query);
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
        List<Tag> tags = results.get(0).getTags();
        
        for (int i = 0; i < tags.size() && i < 5; ++i) {
            Tag tag = tags.get(i);
            if (tag.getProbability() > MINIMUM_PROB)
            mFeatureToProbMap.put(tag.getName(), tag.getProbability());
        }
    }
    
    public static void extractText() {
        Path trainPath = getTestPath("train_text_tags.txt");
        Path testPath = getTestPath("../test/train_text_tags.txt");
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
    
    /**
     * mFeatureToProbMap    = { "dog": 0.95, "cat": 0.4, "sky": 0.9 }
     * query.getFeatureMap  = { "dog": 0.94, "baby": 0.3, "sky": 0.85 }
     * similarity = min(0.95, 0.94) = 0.94
     * 
     * explanation:
     * dog appears in both, take the smallest probability (include)
     * cat doesn't appear in both and has probability < 90 (exclude)
     * baby doesn't appear in both and has probability < 90 (exclude)
     * sky appears in both, but one of the probability < 90 (exclude)
     */
    private double compareFeature(ImageBean query) {
        double similarity = 0;
        Map<String, Double> map = query.getFeatureMap();
        for (String object : map.keySet()) {
            if (mFeatureToProbMap.containsKey(object)) {
                double currentProb = mFeatureToProbMap.get(object);
                double queryProb = map.get(object);
                similarity += Math.min(currentProb, queryProb);
            }
        }
        return similarity;
    }
  
    private ArrayList<Double> getSearchVector() {
        return new ArrayList<>();
    }
    
    private static Path getTestPath(String fileName) {
        Path currentRelativePath = Paths.get("").toAbsolutePath();
        currentRelativePath = currentRelativePath.getParent();
        currentRelativePath = currentRelativePath.resolve("ImageData");
        currentRelativePath = currentRelativePath.resolve("train");
        if (fileName != null && !fileName.isEmpty()) {
            currentRelativePath = currentRelativePath.resolve(fileName);
        }
        return currentRelativePath;
    }
}
