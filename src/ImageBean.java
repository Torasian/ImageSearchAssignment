import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;

/**
 * Class used to store the extracted information of one image
 */
public class ImageBean {
    //TODO color extraction ADT
    HashMap<String, Double> mFeatureToProbMap; 
    private Object mImageInformation;
    private ImageDatabase mImageDatabase;
    private String mFilePath;
    
    public ImageBean(String filePath, Object imageInformation, ImageDatabase imageDatabase) {
        mImageInformation = imageInformation;
        mFilePath = filePath;
        mFeatureToProbMap = new HashMap<>();
        initialize();
    }
    
    public String getFilePath() {
        return mFilePath;
    }
    
    /**
     * Given the information for the image's color
     * features and other extracted information,
     * it calculates a search vector that can be compared
     * to another ImageBean's search vector
     */
    public double calculateSimilarityg(ImageBean query) {
        if (mImageDatabase.isExtractingColor()) {
            
        }
        
        if (mImageDatabase.isExtractingFeature()) {
            
        }
        
        return 0;
    }
    
    private void initialize() {
        extractFeature();
        extractColor();
    }
    
    /**
     * https://github.com/Clarifai/clarifai-java
     */
    public void extractFeature() {
        mFeatureToProbMap.clear();
        ClarifaiClient clarifai = new ClarifaiClient(
                "fcq9IaGGv6G_Pm1yirdPapOa13pYpoamNDLOPX3s", 
                "Xc9qLgll8bxQfr9J1oOsK4iqdf3WgcrkYQ2mzebG");
        
        List<RecognitionResult> results =
            clarifai.recognize(new RecognitionRequest(new File(getFilePath())));

        for (Tag tag : results.get(0).getTags()) {
            mFeatureToProbMap.put(tag.getName(), tag.getProbability());
        }
    }
    
    private void extractColor() {
        
    }
    
    private ArrayList<Double> getSearchVector() {
        return new ArrayList<>();
    }
}
