import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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
    private BufferedImage mImageInformation;
    private ImageDatabase mImageDatabase;
    private ArrayList<String> tags;
    private ArrayList<Integer> intTags;
    private double[] mHistValues;
    private String mFilePath;
    private String mFileName;
    
    public double simValue;
    double topHalf ;
    double bottomHalf;
    double querySquared ;
    double currentSquared;

    public ImageBean(String fileName, String filePath, BufferedImage imageInformation) {
        mImageInformation = imageInformation;
        mFilePath = filePath;
        mFileName = fileName;
        mFeatureToProbMap = new HashMap<>();
        intTags = new ArrayList<>();
    }
    
    public void setImageDatabase(ImageDatabase imageDatabase) {
        mImageDatabase = imageDatabase;
    }

    public String getFilePath() {
        if (SearchUI.DEBUG)System.out.print(mFilePath);
        return mFilePath;
    }

    public String getFileName() {
        return mFileName;
    }

    public Map<String, Double> getFeatureMap() {
        return mFeatureToProbMap;
    }
    
    public double[] getColorHist() {
        return mHistValues;
    }

    /**
     * Given the information for the image's color features and other extracted
     * information, it calculates a search vector that can be compared to
     * another ImageBean's search vector
     */
    public void calculateSimilarity(ImageBean query) {
        double similarityValue = 0;
        if (mImageDatabase.isExtractingColor()) {
            similarityValue += compareColor(query);
        }

        if (mImageDatabase.isExtractingFeature()) {
            similarityValue += compareFeature(query);
        }

        if (mImageDatabase.isExtractingText()) {
            similarityValue += compareText(query);
        }

        this.simValue = similarityValue;
    }

    public void initialize() {
        extractFeature();
        extractColor();
        extractText();
    }

    /**
     * https://github.com/Clarifai/clarifai-java
     */
    private void extractFeature() {
        mFeatureToProbMap.clear();
        String fileName = getTextFileName(mFileName);
        if (isExtracted(fileName)) {
            mFeatureToProbMap = getExtractedFeatureList(fileName);
            return;
        }
        
        Map<String, Double> mFeatureToProbMap = new HashMap<>();
        ClarifaiClient clarifai = new ClarifaiClient(
                "fcq9IaGGv6G_Pm1yirdPapOa13pYpoamNDLOPX3s",
                "Xc9qLgll8bxQfr9J1oOsK4iqdf3WgcrkYQ2mzebG");

        List<RecognitionResult> results = clarifai
                .recognize(new RecognitionRequest(new File(getFilePath())));
        List<Tag> tags = results.get(0).getTags();

        for (int i = 0; i < tags.size() && i < 5; ++i) {
            Tag tag = tags.get(i);
            if (tag.getProbability() > MINIMUM_PROB) {
                mFeatureToProbMap.put(tag.getName(), tag.getProbability());
            }
        }
        
        try {
            saveFeatureMap(fileName, mFeatureToProbMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractText() {
        tags = mImageDatabase.getTagsForFileName(mFileName);
        Map<String, ArrayList<String>> getFiletoTagList = mImageDatabase
                .getFileToTagListMap();

        ArrayList<String> allWords = mImageDatabase
                .getAllWords(getFiletoTagList);
        intTags = mImageDatabase.getVectorForTags(tags, allWords);
    }

    private void extractColor() {
        Histogram hist = new Histogram();
        mHistValues = hist.getHist(mImageInformation);
        for (int i = 0; i < mHistValues.length; i++) {
            if (SearchUI.DEBUG) System.out.println(mHistValues[i]);
        }
    }
    
    private String getTextFileName(String fileName) {
        return fileName.replace(".jpg", ".txt");
    }
    
    private boolean isExtracted(String fileName) {
        String filePath = Utils.getFeaturePath(fileName).toString();
        File f = new File(filePath);
        return f.exists() && !f.isDirectory();
    }
    
    private Map<String, Double> getExtractedFeatureList(String fileName) {
        Map<String, Double> featureMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        String filePath = Utils.getFeaturePath(fileName).toString();
        
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            list = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (String line : list) {
            String[] featureAndProb = line.split("##");
            String feature = featureAndProb[0];
            double prob = Double.parseDouble(featureAndProb[1]);
            featureMap.put(feature, prob);
        }
        return featureMap;
    }
    
    private void saveFeatureMap(String fileName, Map<String, Double> featureMap) throws IOException {
        String filePath = Utils.getFeaturePath(fileName).toString();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), 
                StandardCharsets.UTF_8)) {
            for (String key : featureMap.keySet()) {
                double prob = featureMap.get(key);
                writer.write(key + "##" + prob + "\n");
            }
        }
    }

    /**
     * mFeatureToProbMap = { "dog": 0.95, "cat": 0.4, "sky": 0.9 }
     * query.getFeatureMap = { "dog": 0.94, "baby": 0.3, "sky": 0.85 }
     * similarity = min(0.95, 0.94) = 0.94
     * 
     * explanation: dog appears in both, take the smallest probability (include)
     * cat doesn't appear in both and has probability < 90 (exclude) baby
     * doesn't appear in both and has probability < 90 (exclude) sky appears in
     * both, but one of the probability < 90 (exclude)
     */
    public double compareFeature(ImageBean query) {
    	int count =0;
        double similarity = 0;
        Map<String, Double> map = query.getFeatureMap();
        for (String object : map.keySet()) {
            if (mFeatureToProbMap.containsKey(object)) {
                double currentProb = mFeatureToProbMap.get(object);
                double queryProb = map.get(object);
                similarity += Math.min(currentProb, queryProb);
                count ++;
            }
        }
        if (similarity == 0) {
            return 0;
        }
        double similarity_new = similarity/count;
        return similarity_new;
    }

    private double compareColor(ImageBean query) {
        double similarity = 0;
        double[] queryHist = query.getColorHist();
        double[] imageHist = mHistValues;
        ColourHistCompare histCompare = new ColourHistCompare();
        double distance;
        distance = histCompare.calculateDistance(queryHist, imageHist);
        similarity = 1 - distance;
        
        if (SearchUI.DEBUG)System.out.println("Colour Histogram Similarity:");
        if (SearchUI.DEBUG)System.out.println(similarity);
        
        return similarity;
    }
    
    private ArrayList<Integer> getIntTags() {
        return intTags;
    }

    
    private double compareText(ImageBean query) {
        double similarity = 0.0;
        ArrayList<Integer> queryVector = query.getIntTags();
        ArrayList<Integer> currentVector = intTags;

        if (queryVector.size() != currentVector.size()) {
            return 0;
        }

        /*for (int i = 0; i < queryVector.size(); ++i) {
            double topHalf = queryVector.get(i) * currentVector.get(i);
            int querySquared = queryVector.get(i) * queryVector.get(i);
            int currentSquared = currentVector.get(i) * currentVector.get(i);
            double bottomHalf = Math.sqrt(querySquared * currentSquared);
            similarity += bottomHalf;

        }*/

        for (int i=0; i<queryVector.size(); i++){
        	topHalf +=  queryVector.get(i) * currentVector.get(i) *1.0;
        	 querySquared += queryVector.get(i) * queryVector.get(i) *1.0;
             currentSquared += currentVector.get(i) * currentVector.get(i) *1.0;
        }
        bottomHalf = Math.sqrt(querySquared * currentSquared);
        if (bottomHalf == 0) {
            return 0;
        }
        similarity = topHalf/bottomHalf;
        return similarity;
    }
}