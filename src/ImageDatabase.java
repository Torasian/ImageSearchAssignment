import java.util.ArrayList;

/**
 * Class used to store Image Information
 */
public class ImageDatabase {
    private ArrayList<Image> mImages;
    private boolean mIsExtractingColor;
    private boolean mIsExtractingFeatures;
    
    public ImageDatabase() {
        mImages = new ArrayList<>();
    }

    public boolean loadImages() {
        boolean hasSucceeded = true;
        
        try {
            
        } catch (Exception e) {
            hasSucceeded = false;
        }
        
        return hasSucceeded;
    }

    private boolean loadImage(String path) {
        boolean hasSucceeded = true;
        
        try {
            
        } catch (Exception e) {
            hasSucceeded = false;
        }
        
        return hasSucceeded;
    }

    /**
     * Searches for a list of similar images
     */
    public ArrayList<Image> search(Image query) {
        return new ArrayList<>();
    }
    
    public boolean isExtractingColor() {
        return mIsExtractingColor;
    }
    
    public boolean isExtractingFeature() {
        return mIsExtractingFeatures;
    }
}
