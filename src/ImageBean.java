import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;

/**
 * Class used to store the extracted information of one image
 */
public class ImageBean {
    private static double MINIMUM_PROB = 0.90;
    private Map<String, Double> mFeatureToProbMap; 
    private Image mImageInformation;
    private ImageDatabase mImageDatabase;
    //private static ImageDatabase mImageDatabase_browse;
    private ArrayList<String> tags;
    private ArrayList<Integer> intTags;
    private String mFilePath;
    private  String mFileName;
    
    private static Histogram hist;
    public double simValue;
    private SearchUI Search;
    private ArrayList<Double> distanceVal;
 	private static Histogram histogram1;
	private static Histogram histogram2;
	private static ColourHistCompare compare;
   // private static ArrayList<ImageBean> BrowseBeans;
    
    
    public ImageBean(String fileName, String filePath, /*Image imageInformation,*/ ImageDatabase imageDatabase) {
       // mImageInformation = imageInformation;
        mFilePath = filePath;
        //System.out.print(mFilePath);
        mFileName = fileName;
        mImageDatabase = imageDatabase;
        mFeatureToProbMap = new HashMap<>();
        intTags = new ArrayList<>();
        initialize();
    }
    
    public String getFilePath() {
    	//System.out.print(mFilePath);
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
    public void calculateSimilarity(ImageBean query) {
        double similarityValue = 0;
        if (mImageDatabase.isExtractingColor()) {
         // similarityValue += compareColor(query);
        }
        
        if (mImageDatabase.isExtractingFeature()) {
            similarityValue += compareFeature(query);
        }
        
        if (mImageDatabase.isExtractingText()) {
        	similarityValue += compareText(query);
        }
        
        this.simValue = similarityValue;
    }
    
    private void initialize() {
        extractFeature();
        extractColor();
       // extractText();
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
    
    public void extractText() {
    	tags = mImageDatabase.getTagsForFileName(mFileName);
    	Map<String, ArrayList<String>> getFiletoTagList = mImageDatabase.getFileToTagListMap();

    	ArrayList<String> allWords = mImageDatabase.getAllWords(getFiletoTagList);
    	intTags = mImageDatabase.getVectorForTags(tags, allWords);
    	
    }
    

    public void extractColor() {
    	double[] histvalue;
    	String path = getFilePath();
    	try {
    		BufferedImage img1 = ImageIO.read(new File(path));
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
    
    public static void ColourHistSimilarityCal(){
    	
		try{
		BufferedImage img1 = ImageIO.read(new File("/Users/Admin/Documents/NUS/Sem-1-2016-17/CS2108/Assignment/ImageSeach_demo/dataset/0028_1070815604.jpg"));
		BufferedImage img2 = ImageIO.read(new File("/Users/Admin/Documents/NUS/Sem-1-2016-17/CS2108/Assignment/ImageSeach_demo/dataset/0030_1091560018.jpg"));
		histogram1 = new Histogram();
		histogram2 = new Histogram();
		double[] histVal1 = histogram1.getHist(img1);
		double[] histVal2 = histogram2.getHist(img2);
		compare = new ColourHistCompare();
		double distance;
		distance = compare.calculateDistance(histVal1, histVal2);
		
		System.out.print("Colour Histogram Similarity value:");
		System.out.println(1-distance);
		}
		catch (IOException e) {
    		
    	}
    }
    /*public void compareColor(ImageBean query){
    	double similarity = 0;
    	histogram1 = new Histogram();
		histogram2 = new Histogram();
		try{
			for (int i=0; i< 10;i++ ){
			String Path1 = query.getFilePath();
			String Path2 = getFilePath();
			System.out.println(Path2);
			BufferedImage img1 = ImageIO.read(new File(Path1));
			BufferedImage img2 = ImageIO.read(new File(Path2));
			double[] histVal1 = histogram1.getHist(img1);
			double[] histVal2 = histogram1.getHist(img2);
			compare = new ColourHistCompare();
			double distance;
			distance = compare.calculateDistance(histVal1, histVal2);
			similarity = 1- distance;
			System.out.println("Colour Histogram Similarity:");
			System.out.println(similarity);
		}
		}
		catch (IOException e) {
    	}
		//System.out.println(similarity);
	   	//return similarity;
	   	
    }*/
    
    private ArrayList<Integer> getIntTags(){
    	return intTags;
    }
    
    private double compareText(ImageBean query){
    	double similarity = 0;
    	ArrayList<Integer> queryVector = query.getIntTags();
    	ArrayList<Integer> currentVector = intTags;
    	
    	
    	if (queryVector.size() != currentVector.size()) {
    		return 0;
    	}
    	
    	for (int i = 0; i < queryVector.size(); ++i) {
    		int topHalf =queryVector.get(i)*currentVector.get(i);
    		int querySquared = queryVector.get(i) * queryVector.get(i);
    		int currentSquared = currentVector.get(i) * currentVector.get(i);
    		double bottomHalf = Math.sqrt(querySquared*currentSquared);
    		similarity+= bottomHalf;
    		
    	}
    	
		return similarity;
    	
    }
}