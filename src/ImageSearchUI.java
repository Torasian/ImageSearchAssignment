import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ImageSearchUI extends JFrame{
	
	private JPanel topJP, middleJP;
	private JButton browseButton;
	private JToggleButton histoColourJTB, deepLearningJTB, bothJTB;
	private BorderLayout mainLayout;
	private FlowLayout topLayoutFL;
	private GridLayout middleLayout;
	private JFrame myFrame;
	
	
	public ImageSearchUI(){
		
		myFrame = new JFrame("Image Search");
		myFrame.setSize(1200, 600);
		
		
		topJP = new JPanel();
		middleJP = new JPanel();
		
		
		mainLayout = new BorderLayout();
		topLayoutFL = new FlowLayout();
		middleLayout = new GridLayout(1, 3);
		
		histoColourJTB = new JToggleButton("Colour Histogram");
		deepLearningJTB = new JToggleButton("Deep Learning");
		bothJTB = new JToggleButton("Both");
		
		browseButton = new JButton("Browse");
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();

		
	}
	
	public void init(){
		
		
		myFrame.setLayout(mainLayout);
		topJP.setLayout(topLayoutFL);
		myFrame.add(topJP, BorderLayout.NORTH);
		
		topJP.add(browseButton);
		
		middleJP.setLayout(middleLayout);
		myFrame.add(middleJP, BorderLayout.CENTER);
		
		middleJP.add(histoColourJTB);
		middleJP.add(deepLearningJTB);
		middleJP.add(bothJTB);
		
		
		
		myFrame.pack();
		myFrame.setVisible(true);
		
		
		
		
	}
	
	public static void main(String[] args){
		ImageSearchUI demo = new ImageSearchUI();
	}
	
	
}
