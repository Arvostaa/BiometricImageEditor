package Histograms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import convertRGB.RGBimage;
import convertRGB.SaveImage;

public class GenHistogram2 extends JPanel {

	public GenHistogram2(RGBimage image) throws IOException {
		//super("Generating Histogram");
		JPanel histograms = new JPanel();
		JPanel buttons = new JPanel();
		JButton stretchButton = new JButton("stretch");
		JButton eqButton = new JButton("equalize");
		
		buttons.setLayout(new GridLayout(1, 2, 1, 3));
				
		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 900);
		setVisible(true);

		System.out.println("HIST DIMESTIONS " + image.d);

		GetHistogram rHistogram = new GetHistogram(image.d, "red", image.selectedFile, image.d.height);
		GetHistogram gHistogram = new GetHistogram(image.d, "green", image.selectedFile, image.d.height);
		GetHistogram bHistogram = new GetHistogram(image.d, "blue", image.selectedFile, image.d.height);
		GetHistogram rgbHistogram = new GetHistogram(image.d, "all", image.selectedFile, image.d.height);
		
		buttons.add(stretchButton);
		buttons.add(eqButton);
		histograms.setLayout(new GridLayout(1, 3, 1, 3));
		histograms.add(buttons);
		histograms.add(rHistogram);
		histograms.add(gHistogram);
		histograms.add(bHistogram);
		histograms.add(rgbHistogram);
		

		setLayout(new BorderLayout(4, 4));
		add("Center", histograms);
		add("South", buttons);
		stretchButton.addActionListener(new ActionListener() {
			 @Override
		        public void actionPerformed(ActionEvent e) {
		            JFrame frame = new JFrame ("Stretch");
		            frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		         
						try {
							System.out.println("BEFOREPASS: " + image.selectedPath);
							frame.getContentPane().add (new StretchHistogram(image) );
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
		            frame.pack();
		            frame.setVisible (true);

		        }
	    });
		


	}
	
	

}