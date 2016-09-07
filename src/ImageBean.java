import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;

/**
 * Class used to store the extracted information of one image
 */
public class ImageBean implements Comparable<ImageBean>{
    //TODO color extraction ADT
    //TODO feature extraction ADT
    private Object mImageInformation;
    private ImageDatabase mImageDatabase;
    private String mFilePath;
    
    public ImageBean(String filePath, Object imageInformation, ImageDatabase imageDatabase) {
        mImageInformation = imageInformation;
        mFilePath = filePath;
        initialize();
    }
    
    public String getFilePath() {
        return mFilePath;
    }
    
    public void calculateSearchVector() {
        if (mImageDatabase.isExtractingColor()) {
            
        }
        
        if (mImageDatabase.isExtractingFeature()) {
            
        }
    }
    
    private void initialize() {
        extractFeature();
        extractColor();
        calculateSearchVector();
    }
    
    private void extractFeature() {
        ClarifaiClient clarifai = new ClarifaiClient(
                "fcq9IaGGv6G_Pm1yirdPapOa13pYpoamNDLOPX3s", 
                "Xc9qLgll8bxQfr9J1oOsK4iqdf3WgcrkYQ2mzebG");
        
        List<RecognitionResult> results =
            clarifai.recognize(new RecognitionRequest(new File("kittens.jpg")));

        for (Tag tag : results.get(0).getTags()) {
          System.out.println(tag.getName() + ": " + tag.getProbability());
        }
    }
    
    private void extractColor() {
        
    }
   
    
    private ArrayList<Double> getSearchVector() {
        return new ArrayList<>();
    }

    @Override
    public int compareTo(ImageBean o) {
        // TODO Auto-generated method stub
        return 0;
    }
}
