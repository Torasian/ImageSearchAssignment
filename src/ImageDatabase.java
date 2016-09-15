
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class used to store Image Information
 */
public class ImageDatabase {
    private ArrayList<ImageBean> mImages;
    private boolean mIsExtractingColor;
    private boolean mIsExtractingFeature;
    private boolean mIsExtractingText;
    private ArrayList<String> allWords;
    private Map<String, ArrayList<String>> fileToListMap;
    private ArrayList<ImageBean> imageBeans;
    
    public ImageDatabase(ArrayList<ImageBean> images) {
    	imageBeans = images;
        initializeVariables();
    }
    
    /**
     * Searches for a list of similar images
     */
    public ArrayList<ImageBean> search(ImageBean query) {
        return new ArrayList<>();
    }
    
    public boolean isExtractingColor() {
        return mIsExtractingColor;
    }
    
    public boolean isExtractingFeature() {
        return mIsExtractingFeature;
    }
    
    public boolean isExtractingText() {
        return mIsExtractingText;
    }
    
    public void setExtractFeature(boolean isExtractingFeature){
        mIsExtractingFeature = isExtractingFeature;
    }
    
    public void setExtractColor(boolean isExtractingFeature){
        mIsExtractingFeature = isExtractingFeature;
    }
    
    public void setExtractingText(boolean isExtractingText) {
        mIsExtractingText = isExtractingText;
    }
    
    public ArrayList<ImageBean> getImages() {
        return new ArrayList<ImageBean>();
    }
    
    public void setImageBeans(ImageBean beans){
    	imageBeans.add(beans);
    }
    
    private void initializeVariables() {
        mImages = new ArrayList<>();
        Path trainPath = Utils.getTestPath("train_text_tags.txt");
        Path testPath = Utils.getTestPath("../test/test_text_tags.txt");
        String path = trainPath.toString();
        fileToListMap = getFileToTagListMap(path);
        allWords = getAllWords(fileToListMap);
    }
    
    public ArrayList<String> getTagsForFileName(String fileName){
    	if(!fileToListMap.containsKey(fileName)){
    		return new ArrayList<>();
    	}
    	System.out.println(fileToListMap.get(fileName).toString());
    	return fileToListMap.get(fileName);
    }
    
    public ArrayList<Integer> getVectorForTags(ArrayList<String> fileTags, ArrayList<String> allWords){
    	ArrayList<Integer> tagMatches = new ArrayList<>();
    	
    	int j= 0;
    	
    	for (int i = 0; i < allWords.size(); i++) {
			if (j < fileTags.size() && allWords.get(i).equals(fileTags.get(j))) {
				tagMatches.add(1);
				j++;
			} else {
				tagMatches.add(0);
			}
			
		}
    	
    	return tagMatches;
    }
    
    private Map<String, ArrayList<String>> getFileToTagListMap(String path) {
        Map<String, ArrayList<String>> fileToTagsMap = new HashMap<>();

        try (FileReader input = new FileReader(path); BufferedReader br = new BufferedReader(input)) {
            String str;
            boolean found = false;
            
            while ((str = br.readLine()) != null) {
                String[] tags = str.split(" ");
                String fileName = tags[0];
                Set<String> tagSet = new HashSet<>();
                for (int i = 1; i < tags.length; ++i) {
                	String tag = tags[i].trim();
                	if (!tag.equals("")) {
                		tagSet.add(tag);
                	}
                }
                ArrayList<String> tagsForFile = new ArrayList<>(tagSet);
                Collections.sort(tagsForFile);
//                System.out.println(fileName);
//                found = found || "0037_61248472.jpg".equals(fileName);
                fileToTagsMap.put(fileName, tagsForFile);
            }
//            System.out.println(found);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileToTagsMap;
    }
    
    public Map<String, ArrayList<String>> getFileToTagListMap() {
    	if (fileToListMap == null) {
    		initializeVariables();
    	}
    	return fileToListMap;
    }
    
    public ArrayList<ImageBean> getSimilarImages(ImageBean query){
    	if(imageBeans == null || imageBeans.isEmpty()){
    		return new ArrayList<>();
    	}
    	
    	for (ImageBean imageBean : imageBeans) {
			imageBean.calculateSimilarity(query);
		}
		imageBeans.sort((s1, s2) -> (int) (s2.simValue - s1.simValue));
		
		ArrayList<ImageBean> simImages = new ArrayList<>();
		
		for (int i = 0; i < 10 && i < imageBeans.size(); i++) {
			simImages.add(imageBeans.get(i));
		}
		return simImages;
		
    }
    
    public ArrayList<String> getAllWords(Map<String, ArrayList<String>> fileToTagsMap) {
        Set<String> allTagsSet = new HashSet<>(); 
        for (String fileName : fileToTagsMap.keySet()) {
            ArrayList<String> fileTags = fileToTagsMap.get(fileName);
            for (String tag : fileTags) {
            	if (!tag.trim().equals("")) {
            		allTagsSet.add(tag);
            	}
            }
        }
        ArrayList<String> allTags = new ArrayList<>(allTagsSet);
        Collections.sort(allTags);
        return allTags;
    }
}
