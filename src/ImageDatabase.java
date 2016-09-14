
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
    
    public ImageDatabase(ArrayList<ImageBean> images) {
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
    
    private void initializeVariables() {
        mImages = new ArrayList<>();
        Path trainPath = Utils.getTestPath("train_text_tags.txt");
        Path testPath = Utils.getTestPath("../test/test_text_tags.txt");
        String path = trainPath.toString();
        fileToListMap = getFileToTagListMap(path);
        allWords = getAllWords(fileToListMap);
    }
    
    public Map<String, ArrayList<String>> getFileToTagListMap(String path) {
        Map<String, ArrayList<String>> fileToTagsMap = new HashMap<>();

        try (FileReader input = new FileReader(path); BufferedReader br = new BufferedReader(input)) {
            String str;
            
            while ((str = br.readLine()) != null) {
                String[] tags = str.split(" ");
                String fileName = tags[0];
                Set<String> tagSet = new HashSet<>();
                for (int i = 1; i < tags.length; ++i) {
                    tagSet.add(tags[i]);
                }
                ArrayList<String> tagsForFile = new ArrayList<>(tagSet);
                Collections.sort(tagsForFile);
                fileToTagsMap.put(fileName, tagsForFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileToTagsMap;
    }
    
    public ArrayList<String> getAllWords(Map<String, ArrayList<String>> fileToTagsMap) {
        Set<String> allTagsSet = new HashSet<>(); 
        for (String fileName : fileToTagsMap.keySet()) {
            ArrayList<String> fileTags = fileToTagsMap.get(fileName);
            for (String tag : fileTags) {
                allTagsSet.add(tag);
            }
        }
        ArrayList<String> allTags = new ArrayList<>(allTagsSet);
        Collections.sort(allTags);
        return allTags;
    }
}
