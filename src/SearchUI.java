import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;

public class SearchUI extends JFrame{

	// File representing the folder that you select using a FileChooser
	static File dir;
	private String currentDirectory = System.getProperty("user.dir");
	// array of supported extensions (use a List if you prefer)
	static final String[] EXTENSIONS = new String[]{
			"jpg","gif", "png", "bmp" // and other formats you need
	};

	static FilenameFilter IMAGE_FILTER;

	private JFrame myFrameJF;
	private JScrollPane scrollJSP;
	private JButton browseJB, loadAllJB;
	private JLabel browseJL;
	private JPanel optionJP, imagesJP;
	private JToggleButton histoJTB, deepJTB, textJTB, bothJTB;
	private ArrayList<Image> loadedImages = new ArrayList<>();
	private String imagePath;
	private Image browseImg, img;
	private ImageDatabase imageDB, browseDB;
	private int imageWidth = 300;
	ImageBean query;
	private ArrayList<ImageBean> imageBeans = new ArrayList<>();
	private ArrayList<ImageBean> browseBeans = new ArrayList<>();
	private  ArrayList<String> imagePaths = new ArrayList<>();
	private int spc_count=-1;

	public SearchUI() throws IOException{

		browseJB = new JButton("Browse:");
		browseJL = new JLabel("");

		optionJP = new JPanel(new FlowLayout());

		histoJTB = new JToggleButton("Colour Histogram");
		deepJTB = new JToggleButton("Deep Learning");
		textJTB = new JToggleButton("Text Extraction");
		bothJTB = new JToggleButton("All");
		loadAllJB = new JButton("Load All");
		
		imagesJP = new JPanel(new WrapLayout());
		scrollJSP = new JScrollPane(imagesJP);

		loadAllImages(Utils.getTestPath("data").toString());
		
		imageDB = new ImageDatabase(imageBeans);
		browseDB = new ImageDatabase(browseBeans);
		initialise();
	}

	private void initialise() {

		getContentPane().setPreferredSize(new Dimension(1000, 1000));;
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setResizable(true);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setTitle("Search UI Test");

		getContentPane().add(browseJB);
		browseJB.setAlignmentX(CENTER_ALIGNMENT);
		getContentPane().add(browseJL);
		browseJL.setAlignmentX(CENTER_ALIGNMENT);

		getContentPane().add(optionJP);
		optionJP.add(histoJTB);
		optionJP.add(deepJTB);
		optionJP.add(textJTB);
		optionJP.add(bothJTB);
		optionJP.add(loadAllJB);

		getContentPane().add(scrollJSP);
		scrollJSP.setVisible(false);

		loadAllAction();
		browseButtonAction();
		bothJTBAction();
		
		histoColourJTBAction();
		

		this.pack();
		this.setVisible(true);
		
		

	}

	/**
	 * Creates an item listener event for when the histoColourJTB is selected
	 */
	private void histoColourJTBAction() {
		
		histoJTB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if(state== ItemEvent.SELECTED){
					imageDB.setExtractColor(true);
					System.out.println("Color Histogram selected");
				}
				else{
					imageDB.setExtractColor(false);
					System.out.println("Color Histogram is Not selected");
				}
			}
		});
	}

	private void bothJTBAction() {
		bothJTB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if(state== ItemEvent.SELECTED){
					histoJTB.setEnabled(false);
					deepJTB.setEnabled(false);
					imageDB.setExtractFeature(true);
					imageDB.setExtractColor(true);
					System.out.println("both features are Selected");
				}
				else{
					histoJTB.setEnabled(true);
					deepJTB.setEnabled(true);
					imageDB.setExtractFeature(false);
					imageDB.setExtractColor(false);
					System.out.println("both features have been deselected");
				}

			}
		});
	}

	private void deepLearningJTBAction(ImageBean query) {
		deepJTB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if(state== ItemEvent.SELECTED){
					imageDB.setExtractFeature(true);
					imageDB.getSimilarImages(query);
					printPictures(imageDB.getSimilarImages(query));
					System.out.println("Deep learning is Selected");
					scrollJSP.setVisible(true);
				}
				else{
					imageDB.setExtractFeature(false);
					System.out.println("Deep learning is not selected");
				}
				

			}
		});
	}
	
	private void textJTBAction(ImageBean query) {
		textJTB.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if(state == ItemEvent.SELECTED){
					imageDB.setExtractingText(true);
					System.out.println(imageDB.getSimilarImages(query));
					imageDB.getSimilarImages(query);
					printPictures(imageDB.getSimilarImages(query));
					System.out.println("Text Extraction is selected");
					scrollJSP.setVisible(true);
				} else {
					imageDB.setExtractingText(false);
					System.out.println("Text extraction is not selected");
				}
				
				
			}
		});
	}

	private void browseButtonAction() {
		//opens and loads an image when the browse button is pressed
		browseJB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadBrowseImage();
			}
		});
	}

	private void loadBrowseImage() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Please select a sample image");
		String path = "";
		int returnVal =  fileChooser.showOpenDialog(SearchUI.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			imagePath = fileChooser.getSelectedFile().getAbsolutePath();
			browseImg = null;
			try {
				browseImg = ImageIO.read(new File(imagePath));
				
				Path p = Paths.get(imagePath);
				String file = p.getFileName().toString();
				query = new ImageBean(file, imagePath, browseImg, imageDB);
				browseBeans.add(query);
				ArrayList<ImageBean> sims = imageDB.getSimilarImages(query);
				
				textJTBAction(query);
				deepLearningJTBAction(query);
				
				browseImg = browseImg.getScaledInstance(imageWidth, -1, browseImg.SCALE_DEFAULT);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			browseJL.setIcon(new ImageIcon(browseImg));
		}
	}

	private void loadAllAction() {
		loadAllJB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadAllImages(Utils.getTestPath("data").toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	private void loadAllImages(String path) throws IOException{
	    imagePaths.clear();
		dir = new File(path);
		System.out.println(path);

		spc_count++;
		String spcs = "";
		for (int i = 0; i < spc_count; i++)
			spcs += " ";

		iterateFiles(spcs);
		scrollJSP.setVisible(true);
//		printPictures();
	}

	private void iterateFiles(String spcs) {
		if(dir.isFile()){
			for(final String ext : EXTENSIONS){
				if(dir.getName().endsWith("."+ ext)){
					imagePaths.add(dir.getAbsolutePath());
					Image img = null;
					try{
						img = ImageIO.read(dir);
						loadedImages.add(img);
						for (int i = 0; i< imagePaths.size(); i++) {
							String imageP = imagePaths.get(i);
							Path p = Paths.get(imageP);
							String file = p.getFileName().toString();
							ImageBean temp = new ImageBean(file, dir.getAbsolutePath(), img, imageDB);
							imageBeans.add(temp);
//							imageDB.setImageBeans(temp);
						}
					} catch (final IOException e){
						
					}
				}
			}
		}
		else if (dir.isDirectory()) {

			File[] listOfFiles = dir.listFiles();
			if(listOfFiles!=null) {
				for (int i = 0; i < listOfFiles.length; i++)
					iterateDirectory(listOfFiles[i]);
			} else {
				System.out.println(spcs + " [ACCESS DENIED]");
			}
		}
		spc_count--;
	}


	private void iterateDirectory(File aFile){

		spc_count++;
		String spcs = "";
		for (int i = 0; i < spc_count; i++)
			spcs += " ";


		if(aFile.isFile()){
			for(final String ext : EXTENSIONS){
				if(aFile.getName().endsWith("."+ ext)){
					imagePaths.add(aFile.getAbsolutePath());
				}
			}
		}
		else if (aFile.isDirectory()) {

			File[] listOfFiles = aFile.listFiles();
			if(listOfFiles!=null) {
				for (int i = 0; i < listOfFiles.length; i++)
					iterateDirectory(listOfFiles[i]);
			} else {
				System.out.println(spcs + " [ACCESS DENIED]");
			}
		}
		spc_count--;
	}


	public String getBrowsePath(){
		return imagePath;
	}

	private void printPictures(ArrayList<ImageBean> printImages) {
		System.out.println(imagePaths.size());
		for(int i = 0; i < imagePaths.size(); i++){
			
			imagesJP.add(new JLabel(new ImageIcon(printImages.get(i).getFilePath())));
			revalidate();
			repaint();
		}
	}


	public static void main(String[] args) throws IOException{
		SearchUI demo = new SearchUI();
	
	}

}