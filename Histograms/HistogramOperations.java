package Histograms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import convertRGB.RGBimage;

public class HistogramOperations extends JFrame {
	DisplayPanel displayPanel; // image panel
	GetHistogram rHistogram, gHistogram, bHistogram, rgbHistogram;

	JButton brightenButton, darkenButton, resetButton, equalizeButton, stretchButton;

	StretchHistogram stretch;
	EqualizeHistogram equalize;

	public HistogramOperations(RGBimage image) throws IOException {
		super();
		Container container = getContentPane();
		equalize = new EqualizeHistogram();

		displayPanel = new DisplayPanel(image);
		JPanel buttons = new JPanel();

		buttons.setLayout(new GridLayout(1, 5));
		buttons.setBorder(new TitledBorder("Improve image quality"));

		brightenButton = new JButton("Brightness >>");
		brightenButton.addActionListener(new ButtonListener());
		darkenButton = new JButton("<< Darkness");
		darkenButton.addActionListener(new ButtonListener());

		equalizeButton = new JButton("Equalization");
		equalizeButton.addActionListener(new ButtonListener());

		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ButtonListener());

		stretchButton = new JButton("Stretching");
		stretchButton.addActionListener(new ButtonListener());

		buttons.add(darkenButton);
		buttons.add(brightenButton);
		buttons.add(equalizeButton);
		buttons.add(stretchButton);
		buttons.add(resetButton);

		// *GENERATE HISTOGRAMS* //

		JPanel histograms = new JPanel();
		histograms.setLayout(new GridLayout(1, 4));
		histograms.setBackground(new Color(248, 245, 238));

		rHistogram = new GetHistogram("red", displayPanel.bi, displayPanel.bi.getHeight());
		
		Dimension d = new Dimension(260,260);
		
		rHistogram.setPreferredSize(d);
		

		gHistogram = new GetHistogram("green", displayPanel.bi, displayPanel.bi.getHeight());

		bHistogram = new GetHistogram("blue", displayPanel.bi, displayPanel.bi.getHeight());
		
		gHistogram.setPreferredSize(d);
		
		bHistogram.setPreferredSize(d);
		
		rgbHistogram = new GetHistogram("all", displayPanel.bi, displayPanel.bi.getHeight());
		rgbHistogram.setPreferredSize(d);

		rHistogram.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		bHistogram.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		histograms.add(rHistogram);
		histograms.add(gHistogram);
		histograms.add(bHistogram);
		histograms.add(rgbHistogram);

		container.setLayout(new BorderLayout());
		container.add(displayPanel);
		container.add(histograms, BorderLayout.CENTER);

		container.add(buttons, BorderLayout.SOUTH);

		// * GENERATE STRETCH PANEL *//
		JFrame frame = new JFrame("Stretch");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		try {
			frame.getContentPane().add(stretch = new StretchHistogram(displayPanel.bi, displayPanel.path));
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		frame.pack();
		frame.setVisible(true);

		///////////////////////////////////////////

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(displayPanel.getWidth(), displayPanel.getHeight() + 600);
		show();
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();

			if (button.equals(brightenButton)) { // ADD HISTOGRAM GENERATING
				displayPanel.brightenLUT();
				displayPanel.applyFilter();
				displayPanel.repaint();
				repaint();
				repaint();

			} else if (button.equals(darkenButton)) { // ADD HISTOGRAM
														// GENERATING
				displayPanel.darkenLUT();
				displayPanel.applyFilter();
				displayPanel.repaint();
				repaint();
			} else if (button.equals(equalizeButton)) {

				displayPanel.bi = equalize.histogramEqualization(displayPanel.bi);
				displayPanel.repaint();
				System.out.println("equalizeButton");
				rHistogram.updateHistogram(equalize.rgbValues.get(0), "red");
				gHistogram.updateHistogram(equalize.rgbValues.get(1), "green");
				bHistogram.updateHistogram(equalize.rgbValues.get(2), "blue");
				rgbHistogram.updateHistogram(equalize.rgbValues.get(3), "all");

				rHistogram.drawNewHistogram(rHistogram.getGraphics(), "red");
				gHistogram.drawNewHistogram(gHistogram.getGraphics(), "green");
				bHistogram.drawNewHistogram(bHistogram.getGraphics(), "blue");
				rgbHistogram.drawNewHistogram(rgbHistogram.getGraphics(), "all");
				displayPanel.repaint();
				repaint();

			} else if (button.equals(stretchButton)) {
				displayPanel.scaleBufferedImage(stretch.returnImage());
				displayPanel.repaint();

				rHistogram.stretched = true;
				gHistogram.stretched = true;
				bHistogram.stretched = true;
				rgbHistogram.stretched = true;

				rHistogram.updateHistogram(stretch.rValue, "red");
				gHistogram.updateHistogram(stretch.gValue, "green");
				bHistogram.updateHistogram(stretch.bValue, "blue");
				rgbHistogram.updateHistogram(stretch.rgbValue, "all");

				rHistogram.drawNewHistogram(rHistogram.getGraphics(), "red");
				gHistogram.drawNewHistogram(gHistogram.getGraphics(), "green");
				bHistogram.drawNewHistogram(bHistogram.getGraphics(), "blue");
				rgbHistogram.drawNewHistogram(rgbHistogram.getGraphics(), "all");

				displayPanel.repaint();
				repaint();
			}

			else if (button.equals(resetButton)) {

				displayPanel.reset();

				rHistogram.getHistogram(displayPanel.bi);
				gHistogram.getHistogram(displayPanel.bi);
				bHistogram.getHistogram(displayPanel.bi);
				rgbHistogram.getHistogram(displayPanel.bi);

				rHistogram.drawNewHistogram(rHistogram.getGraphics(), "red");
				gHistogram.drawNewHistogram(gHistogram.getGraphics(), "green");
				bHistogram.drawNewHistogram(bHistogram.getGraphics(), "blue");
				rgbHistogram.drawNewHistogram(rgbHistogram.getGraphics(), "all");

				displayPanel.repaint();
				repaint();

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
		this.setBackground(new Color(255, 255, 255));
		path = image.sname;
		loadImage(image);

		createBufferedImage(image);
		setSize(bi.getWidth(), bi.getWidth()); // panel

	}

	public void getRGBfromLUT() { // set new r g b values from br/dar image to
									// genHistogram object

		int height = bi.getHeight();
		int width = bi.getWidth();

		int[] e = new int[width * height]; // ?
		int[] data = new int[width * height]; // ?
		bi.getRGB(0, 0, width, height, data, 0, width);

		int[] r = new int[width * height];
		int[] g = new int[width * height];
		int[] b = new int[width * height];
		int[] rgbAverage = new int[width * height];

		for (int i = 0; i < (height * width); i++) {
			r[i] = (int) ((data[i] >> 16) & 0xff);
			g[i] = (int) ((data[i] >> 8) & 0xff);
			b[i] = (int) (data[i] & 0xff);
			rgbAverage[i] = (r[i] + g[i] + b[i]) / 3;

			if (r[i] > 255)
				r[i] = 255;
			if (g[i] > 255)
				g[i] = 255;
			if (b[i] > 255)
				b[i] = 255;

			if (rgbAverage[i] > 255)
				rgbAverage[i] = 255;

			if (r[i] < 0)
				r[i] = 0;
			if (g[i] < 0)
				g[i] = 0;
			if (b[i] < 0)
				b[i] = 0;

			if (rgbAverage[i] < 0)
				rgbAverage[i] = 0;

		}

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

		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(0.75, 0.75);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
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
			System.out.println(brighten[i]);
		}
		lookupTable = new ShortLookupTable(0, brighten);
	}

	public void reset() {

		bi = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);
		big = bi.createGraphics();
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
