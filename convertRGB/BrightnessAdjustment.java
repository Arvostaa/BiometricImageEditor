package convertRGB;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class BrightnessAdjustment extends JFrame {
	DisplayPanel displayPanel;

	JButton brightenButton, darkenButton, resetButton;

	public BrightnessAdjustment(RGBimage image) {
		super();
		Container container = getContentPane();

		displayPanel = new DisplayPanel(image);
		container.add(displayPanel);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.setBorder(new TitledBorder("Adjust Brightness"));

		brightenButton = new JButton("Brightness >>");
		brightenButton.addActionListener(new ButtonListener());
		darkenButton = new JButton("Darkness >>");
		darkenButton.addActionListener(new ButtonListener());

		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ButtonListener());

		panel.add(brightenButton);
		panel.add(darkenButton);
		panel.add(resetButton);

		container.add(BorderLayout.SOUTH, panel);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(displayPanel.getWidth(), displayPanel.getHeight() + 25);
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
			} else if (button.equals(resetButton)) {
				displayPanel.reset();
				displayPanel.repaint();
			}
		}
	}
}

class DisplayPanel extends JPanel {
	Image displayImage;

	BufferedImage bi;

	Graphics2D big;

	LookupTable lookupTable;

	DisplayPanel(RGBimage image) {
		setBackground(Color.black); // panel background color
		loadImage(image);
		setSize(displayImage.getWidth(this), displayImage.getWidth(this)); // panel
		createBufferedImage(image);
	}

	public void loadImage(RGBimage image) {
		displayImage = image.img;
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(displayImage, 1);
		try {
			mt.waitForAll();
		} catch (Exception e) {
			System.out.println("Exception while loading.");
		}

		if (displayImage.getWidth(this) == -1) {
			System.out.println("No jpg file");
			System.exit(0);
		}
	}

	public void createBufferedImage(RGBimage image) {
		bi = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);

		big = bi.createGraphics();
		big.drawImage(displayImage, 0, 0, this);
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

			// brighten[i] = (byte)(Math.sqrt((float)i/255.0) * 255);
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

	public void contrastIncLUT() {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i * 1.2);
			if (pixelValue > 255)
				pixelValue = 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;
		}
		lookupTable = new ShortLookupTable(0, brighten);
	}

	public void contrastDecLUT() {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i / 1.2);
			if (pixelValue > 255)
				pixelValue = 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;
		}
		lookupTable = new ShortLookupTable(0, brighten);
	}

	public void reverseLUT() {
		byte reverse[] = new byte[256];
		for (int i = 0; i < 256; i++) {
			reverse[i] = (byte) (255 - i);
		}
		lookupTable = new ByteLookupTable(0, reverse);
	}

	public void reset() {
		big.setColor(Color.black);
		big.clearRect(0, 0, bi.getWidth(this), bi.getHeight(this));
		big.drawImage(displayImage, 0, 0, this);
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
