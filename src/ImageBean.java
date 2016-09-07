import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

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
    private static Histogram hist;
    
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
    public double calculateSimilarity(ImageBean query) {
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
}
