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
	    private JToggleButton histoJTB, deepJTB, bothJTB;
	    private ArrayList<Image> loadedImages = new ArrayList<>();
	    private String imagePath;
	    private Image browseImg;
	    private ImageDatabase imageDB;
	    private int imageWidth = 300;
	    
	    
	    public SearchUI(){
	    	
	    	browseJB = new JButton("Browse:");
	    	browseJL = new JLabel("");
	    	
	    	optionJP = new JPanel(new FlowLayout());
	    	
	    	histoJTB = new JToggleButton("Colour Histogram");
	    	deepJTB = new JToggleButton("Deep Learning");
	    	bothJTB = new JToggleButton("Both");
	    	loadAllJB = new JButton("Load All");
	    	
	    	
	    	imagesJP = new JPanel(new WrapLayout());
	    	scrollJSP = new JScrollPane(imagesJP);
	    	
	    	initialise();
	    }

		private void initialise() {
			
			getContentPane().setPreferredSize(new Dimension(1000, 1000));;
			this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
			this.setResizable(true);
			
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
			setTitle("Search UI");
			
			getContentPane().add(browseJB);
			browseJB.setAlignmentX(CENTER_ALIGNMENT);
			getContentPane().add(browseJL);
			
			getContentPane().add(optionJP);
			optionJP.add(histoJTB);
			optionJP.add(deepJTB);
			optionJP.add(bothJTB);
			optionJP.add(loadAllJB);
			
			
//			imagesJP.setPreferredSize(new Dimension(400, 400));
//			scrollJSP.setPreferredSize(new Dimension(600, 600));
//			scrollJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			getContentPane().add(scrollJSP);
			scrollJSP.setVisible(false);
			
			loadAllAction();
			browseButtonAction();
			bothJTBAction();
			deepLearningJTBAction();
			histoColourJTBAction();
			
			this.pack();
			this.setVisible(true);
			
		}
		
		private void histoColourJTBAction() {
			histoJTB.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					int state = e.getStateChange();
					if(state== ItemEvent.SELECTED){
						imageDB.setExtractColor(true);
						System.out.println("Color Histogram is Selected");
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
		
		private void deepLearningJTBAction() {
			deepJTB.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					int state = e.getStateChange();
					if(state== ItemEvent.SELECTED){
						imageDB.setExtractFeature(true);
						System.out.println("Deep learning is Selected");
					}
					else{
						imageDB.setExtractFeature(false);
						System.out.println("Deep learning is not selected");
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
					
//					
//					 images = new ImageBean(imagePath, browseImg, imageDB);
					
					browseImg = browseImg.getScaledInstance(imageWidth, -1, browseImg.SCALE_DEFAULT);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				browseJL.setIcon(new ImageIcon(browseImg));
				revalidate();
				repaint();
				
			}
		}
		
		private void loadAllAction() {
			loadAllJB.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					loadAllImages("/Users/WSH/Desktop/test_images/tree");
				}
			});
		}
		
		private void loadAllImages(String path){
			
//			path = currentDirectory;
			dir = new File(path);
			IMAGE_FILTER = new FilenameFilter() {
		
				@Override
		        public boolean accept(final File dir, final String name) {
		            
		            System.out.println(name);
		        	for (final String ext : EXTENSIONS) {
		        		
		                if (name.endsWith("." + ext)) {
		                    return (true);
		                }
		            }
		            return (false);
		        }
			};
			
			 if (dir.isDirectory()) { // make sure it's a directory
		            for (final File f : dir.listFiles(IMAGE_FILTER)) {
		                Image img = null;

		                try {
		                    img = ImageIO.read(f);
		                  
//		                    if(f.isDirectory()){
//		                    	loadAllImages("/Users/WSH/Desktop/test_images/");
//		                    }
//		                    else{
		                    	loadedImages.add(img);
//		                    	images = new ImageBean(dir.getPath(), img, imageDB);
//		                    }
		                    
		                   
		                    
		
		                  
		                    System.out.println(loadedImages.size());
		                } catch (final IOException e) {
		                    // handle errors here
		                }
		            }
		        }
			 else{
				 int trim = path.lastIndexOf("/");
				 loadAllImages(path.substring(0, trim));
			 }
			 scrollJSP.setVisible(true);
			 printPictures();
		}
		 private void printPictures() {
				System.out.println(loadedImages.size());
				for(int i = 0; i < loadedImages.size(); i++){
					imagesJP.add(new JLabel(new ImageIcon(loadedImages.get(i))));
					revalidate();
					repaint();
				}
				
			}
	
		public static void main(String[] args){
			SearchUI demo = new SearchUI();
		}

}
