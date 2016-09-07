import java.util.ArrayList;

/**
 * Class used to store Image Information
 */
public class ImageDatabase {
    private ArrayList<ImageBean> mImages;
    private boolean mIsExtractingColor;
    private boolean mIsExtractingFeatures;
    
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
        return mIsExtractingFeatures;
    }
    
    public void setExtractFeature(boolean set){
        mIsExtractingFeatures = set;
    }
    
    public void setExtractColor(boolean set){
        mIsExtractingFeatures = set;
    }
    
    public ArrayList<ImageBean> getImages() {
        return new ArrayList<ImageBean>();
    }
    
    private void initializeVariables() {
        mImages = new ArrayList<>();
    }
    
//	private void loadBrowseImage() {
//		JFileChooser fileChooser = new JFileChooser();
//		fileChooser.setDialogTitle("Please select a sample image");
//		String path = "";
//		int returnVal =  fileChooser.showOpenDialog(ImageSearchUI.this);
//		if (returnVal == JFileChooser.APPROVE_OPTION) {
//			imagePath = fileChooser.getSelectedFile().getAbsolutePath();
//			browseImg = null;
//			try {
//				browseImg = ImageIO.read(new File(imagePath));
//				browseImg = browseImg.getScaledInstance(width, -1, browseImg.SCALE_DEFAULT);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			browseImageJL.setIcon(new ImageIcon(browseImg));
//		}
//	}
}
