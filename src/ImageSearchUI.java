import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ImageSearchUI extends JFrame{
	
	private JPanel topJP, middleJP, bottomJP;
	private JButton browseButton;
	private JToggleButton histoColourJTB, deepLearningJTB, bothJTB;
	private BorderLayout mainLayout;
	private FlowLayout topLayoutFL, bottomLayoutFL;
	private GridLayout middleLayout;
	private JFrame myFrame;
	private String imagePath;
	private Image browseImg;
	private JLabel browseImageJL, imageJL;
	private ImageDatabase imageDB;
	private ArrayList<JLabel> imageArrList;
	
	
	private int width = 300; // the size for each image result
	private int height = 300;
	
	
	public ImageSearchUI(){
		
		myFrame = new JFrame("Image Search");
		myFrame.setSize(1200, 600);
		
		
		topJP = new JPanel();
		middleJP = new JPanel();
		bottomJP = new JPanel();
		
		browseImageJL = new JLabel("");
		
		mainLayout = new BorderLayout();
		topLayoutFL = new FlowLayout();
		middleLayout = new GridLayout(1, 4);
		bottomLayoutFL = new FlowLayout();
		
		histoColourJTB = new JToggleButton("Colour Histogram");
		deepLearningJTB = new JToggleButton("Deep Learning");
		bothJTB = new JToggleButton("Both");
		
		browseButton = new JButton("Browse");
		
		imageDB = new ImageDatabase();
		imageArrList = new ArrayList<>();
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();

		
	}
	
	public void init(){
		
		
		myFrame.setLayout(mainLayout);
		topJP.setLayout(topLayoutFL);
		myFrame.add(topJP, BorderLayout.NORTH);
		
		topJP.add(browseButton);
		topJP.add(browseImageJL);
		
		
		
		middleJP.setLayout(middleLayout);
		myFrame.add(middleJP, BorderLayout.CENTER);
		
		
		middleJP.add(histoColourJTB);
		middleJP.add(deepLearningJTB);
		middleJP.add(bothJTB);
		
		bottomJP.setLayout(bottomLayoutFL);
		myFrame.add(bottomJP, BorderLayout.SOUTH);
		
		for (int i = 0; i < imageDB.getmImages().size(); i++) {
	
			imageArrList.add(imageJL);
			imageJL.setIcon(new ImageIcon((String) imageDB.getmImages().get(i)));
			
		}
		
		//opens and loads an image when the browse button is pressed
		browseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Please select a sample image");
				String path = "";
				int returnVal =  fileChooser.showOpenDialog(ImageSearchUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					imagePath = fileChooser.getSelectedFile().getAbsolutePath();
					browseImg = null;
					try {
						browseImg = ImageIO.read(new File(imagePath));
						
						
						browseImg = browseImg.getScaledInstance(width, -1, browseImg.SCALE_DEFAULT);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					browseImageJL.setIcon(new ImageIcon(browseImg));
				}
			}
		});
		
		histoColourJTB.addItemListener(new ItemListener() {
			
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
		
		deepLearningJTB.addItemListener(new ItemListener() {
			
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
		
		bothJTB.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if(state== ItemEvent.SELECTED){
					histoColourJTB.setEnabled(false);
					deepLearningJTB.setEnabled(false);
					imageDB.setExtractFeature(true);
					imageDB.setExtractColor(true);
					System.out.println("both features are Selected");
				}
				else{
					histoColourJTB.setEnabled(true);
					deepLearningJTB.setEnabled(true);
					imageDB.setExtractFeature(false);
					imageDB.setExtractColor(false);
					System.out.println("both features have been deselected");
				}
				
			}
		});
		
		myFrame.pack();
		myFrame.setVisible(true);
		
		
		
		
	}
	
	public static void main(String[] args){
		ImageSearchUI demo = new ImageSearchUI();
	}
	
	
}
