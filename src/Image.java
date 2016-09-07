import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageIO;

/**
 * Class used to store the extracted information of one image
 */
public class Image implements Comparable<Image>{
    //TODO color extraction ADT
    //TODO feature extraction ADT
    private Object mImageInformation;
    private ImageDatabase mImageDatabase;
    
	

    private static Histogram hist;
    
    public Image(Object imageInformation, ImageDatabase imageDatabase)throws IOException {
        mImageInformation = imageInformation;
        initialize();
    }
    
    public void calculateSearchVector() {
        if (mImageDatabase.isExtractingColor()) {
            
        }
        
        if (mImageDatabase.isExtractingFeature()) {
            
        }
    }
    
    private void initialize() throws IOException {
        extractFeature();
        extractColor();
        calculateSearchVector();
    }
    
    private void extractFeature() {
        
    }
    
    public static void extractColor() {
    	try {
    		BufferedImage img1 = ImageIO.read(new File("/Users/Admin/Documents/NUS/Sem-1-2016-17/CS2108/Assignment/ImageSeach_demo/dataset/0042_461838579.jpg"));
        	hist = new Histogram();
            hist.getHist(img1);
    	} catch (IOException e) {
    		
    	}
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
