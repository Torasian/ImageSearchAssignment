import java.util.ArrayList;

/**
 * Class used to store the extracted information of one image
 */
public class Image implements Comparable<Image>{
    //TODO color extraction ADT
    //TODO feature extraction ADT
    private Object mImageInformation;
    private ImageDatabase mImageDatabase;
    
    public Image(Object imageInformation, ImageDatabase imageDatabase) {
        mImageInformation = imageInformation;
        initialize();
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
        
    }
    
    private void extractColor() {
        
    }
   
    
    private ArrayList<Double> getSearchVector() {
        return new ArrayList<>();
    }

    @Override
    public int compareTo(Image o) {
        // TODO Auto-generated method stub
        return 0;
    }
}
