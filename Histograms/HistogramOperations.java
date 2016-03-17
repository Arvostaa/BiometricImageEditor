package Histograms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import convertRGB.RGBimage;

public class HistogramOperations extends JFrame {
	DisplayPanel displayPanel;
	GetHistogram rHistogram, gHistogram, bHistogram, rgbHistogram;
	HistogramLUT histogramLut;

	JButton brightenButton, darkenButton, resetButton, equalizeButton, stretchButton;
	
	StretchHistogram stretch;

	EqualizeHistogram equalize;
	//StretchHistogram2 stretch;

	public HistogramOperations(RGBimage image) throws IOException {
		super();
		Container container = getContentPane();
		equalize = new EqualizeHistogram();
		//stretch = new StretchHistogram2();

		displayPanel = new DisplayPanel(image);

		JPanel buttons = new JPanel();

		buttons.setLayout(new GridLayout(1, 5));
		buttons.setBorder(new TitledBorder("Adjust Brightness"));

		brightenButton = new JButton("Brightness >>");
		brightenButton.addActionListener(new ButtonListener());
		darkenButton = new JButton("Darkness >>");
		darkenButton.addActionListener(new ButtonListener());
		buttons.setBorder(new TitledBorder("Equalize image"));
		equalizeButton = new JButton("Equalization");
		equalizeButton.addActionListener(new ButtonListener());

		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ButtonListener());

		stretchButton = new JButton("Stretch");
		stretchButton.addActionListener(new ButtonListener());

		buttons.add(brightenButton);
		buttons.add(darkenButton);

		buttons.add(equalizeButton);
		// brightnessButtons.setBorder(new TitledBorder("Reset operations"));
		buttons.add(resetButton);
		buttons.add(stretchButton);

		// *GENERATE HISTOGRAMS*//

		JPanel histograms = new JPanel();
		histograms.setLayout(new GridLayout(1, 4));
		rHistogram = new GetHistogram("red", displayPanel.bi, displayPanel.bi.getHeight());
		gHistogram = new GetHistogram("green", displayPanel.bi, displayPanel.bi.getHeight());
		bHistogram = new GetHistogram("blue", displayPanel.bi, displayPanel.bi.getHeight());
		rgbHistogram = new GetHistogram("all", displayPanel.bi, displayPanel.bi.getHeight());

		histograms.add(rHistogram);
		histograms.add(gHistogram);
		histograms.add(bHistogram);
		histograms.add(rgbHistogram);

		container.setLayout(new BorderLayout(4, 4));
		container.add(displayPanel);
		container.add("Center", histograms);
		container.add("South", buttons);
		
		 JFrame frame = new JFrame ("Stretch");
         frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      
				try {
					frame.getContentPane().add (stretch = new StretchHistogram(displayPanel.bi, displayPanel.path) );
				}catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
         frame.pack();
         frame.setVisible (true);
		
		
		
		// container.add(BorderLayout.NORTH,displayPanel);
		// container.add(BorderLayout.SOUTH, buttons);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(displayPanel.getWidth(), displayPanel.getHeight() + 600);
		show();
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();

			if (button.equals(brightenButton)) {
				displayPanel.brightenLUT();
				displayPanel.applyFilter();
				displayPanel.repaint();
			} else if (button.equals(darkenButton)) {
				displayPanel.darkenLUT();
				displayPanel.applyFilter();
				displayPanel.repaint();
			} else if (button.equals(equalizeButton)) {
				// mainImage = ImageIO.read(new File(path));
				displayPanel.bi = equalize.histogramEqualization(displayPanel.bi);
				// writeImage("equalizedImage");
				displayPanel.repaint();
				
				/*
				 * displayPanel.darkenLUT(); displayPanel.applyFilter();
				 * displayPanel.repaint();
				 */
			} else if (button.equals(stretchButton)) {
				displayPanel.scaleBufferedImage(stretch.returnImage()); 
				displayPanel.repaint();
			}

			else if (button.equals(resetButton)) {
				displayPanel.reset();
				displayPanel.repaint();
			}
		}
	}
}

class DisplayPanel extends JPanel {
	Image displayImage, startImage;

	BufferedImage bi;
	String path;

	Graphics2D big;

	LookupTable lookupTable;

	DisplayPanel(RGBimage image) {
		// setBackground(Color.black); // panel background color
		
		path = image.sname;
		loadImage(image);
		createBufferedImage(image);
		setSize(bi.getWidth(), bi.getWidth()); // panel
	}

	public void loadImage(RGBimage image) {
		startImage = image.img;
		displayImage = image.img;
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(displayImage, 1);
		try {
			mt.waitForAll();
		} catch (Exception e) {
			System.out.println("Exception while loading.");
		}

		/*
		 * if (displayImage.getWidth(this) == -1) { System.out.println(
		 * "No jpg file"); System.exit(0); } if (startImage.getWidth(this) ==
		 * -1) { System.out.println("No jpg file"); System.exit(0); }
		 */
	}

	public void createBufferedImage(RGBimage image) {
		bi = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);
		System.out.println("WI + HE: " + bi.getHeight() + "" + bi.getWidth());

		big = bi.createGraphics();
		big.drawImage(displayImage, 0, 0, this);
	}
	public void scaleBufferedImage(BufferedImage image) {
		
		//BufferedImage before = getBufferedImage(encoded);
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(0.75, 0.75);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(image, after);
		
		big = bi.createGraphics();
		big.drawImage(after, 0, 0, this);
		
		
	}

	public void brightenLUT() {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i + 10);
			if (pixelValue > 255)
				pixelValue = (short) 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;

			System.out.println(brighten[i]);
		}
		lookupTable = new ShortLookupTable(0, brighten);

	}

	public void darkenLUT() {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i - 10);
			if (pixelValue > 255)
				pixelValue = (byte) 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;

			/*
			 * // Darken the image by 10% float scaleFactor = .9f; // out of 100
			 * ... obviously RescaleOp op = new RescaleOp(scaleFactor, 0, null);
			 * bufferedImage = op.filter(bufferedImage, null);
			 * 
			 */
			// brighten[i] = (byte)((Math.sqrt((float)i/255.0) * 255));

			// brighten[i] = (byte)(Math.sqrt((float)i/255.0) *i); //DZIA£A tak
			// jakby :<

			System.out.println(brighten[i]);
		}
		lookupTable = new ShortLookupTable(0, brighten);
	}

	public void reset() {

		bi = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);

		big = bi.createGraphics();
		// big.drawImage(displayImage, 0, 0, this);

		// big.setColor(Color.black);
		big.clearRect(0, 0, bi.getWidth(this), bi.getHeight(this));
		big.drawImage(startImage, 0, 0, this);
	}

	public void applyFilter() {
		LookupOp lop = new LookupOp(lookupTable, null);
		lop.filter(bi, bi);
	}

	public void update(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		paintComponent(g);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(bi, 0, 0, this);
	}
}
