package Histograms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.synth.SynthSeparatorUI;

import convertRGB.RGBimage;

public class StretchHistogram extends JPanel {

	BufferedImage img;
	BufferedImage imgAfterS;
	JButton button;
	JPanel slidersPanel;
	SliderPanel sliderMin;
	SliderPanel sliderMax;
	String path;

	public StretchHistogram(RGBimage image) throws IOException {

		button = new JButton("Stretch ");
		path = image.sname;
		System.out.println("sciezka: " + path);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					img = ImageIO.read(new File(path));
					writeColorImageValueToFile(img);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		slidersPanel = new JPanel();
		setSize(900, 900);
		setVisible(true);

		sliderMin = new SliderPanel(0, 255, 255);
		sliderMax = new SliderPanel(0, 255, 255);

		slidersPanel.setLayout(new GridLayout(2, 1, 1, 3));
		slidersPanel.add(sliderMin);
		slidersPanel.add(sliderMax);

		setLayout(new BorderLayout(4, 4));
		add("North", image.imgContainer);
		add("Center", slidersPanel);
		add("South", button);

	}

	public void writeColorImageValueToFile(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int min = sliderMin.value; // stretch min level
		int max = sliderMax.value; // stretch max level

		System.out.println("HistMinValue: " + min + ", max: " + max);
		System.out.println("width=" + width + " height=" + height);
		try {

			int[] r = new int[width * height];
			int[] g = new int[width * height];
			int[] b = new int[width * height];
			int[] e = new int[width * height];
			int[] data = new int[width * height];
			image.getRGB(0, 0, width, height, data, 0, width);

			int[] hist_refore_r = new int[256];
			int[] hist_refore_g = new int[256];
			int[] hist_refore_b = new int[256];

			int[] hist_after_r = new int[256];
			int[] hist_after_g = new int[256];
			int[] hist_after_b = new int[256];

			for (int i = 0; i < (height * width); i++) {
				r[i] = (int) ((data[i] >> 16) & 0xff);
				g[i] = (int) ((data[i] >> 8) & 0xff);
				b[i] = (int) (data[i] & 0xff);

				hist_refore_r[r[i]]++;
				hist_refore_g[g[i]]++;
				hist_refore_b[b[i]]++;

				// System.out.println(r[i]+" "+g[i]+" "+b[i]);

				// stretch them to 0 to 255
				r[i] = (int) (1.0 * (r[i] - min) / (max - min) * 255);
				g[i] = (int) (1.0 * (g[i] - min) / (max - min) * 255);
				b[i] = (int) (1.0 * (b[i] - min) / (max - min) * 255);

				if (r[i] > 255)
					r[i] = 255;
				if (g[i] > 255)
					g[i] = 255;
				if (b[i] > 255)
					b[i] = 255;

				if (r[i] < 0)
					r[i] = 0;
				if (g[i] < 0)
					g[i] = 0;
				if (b[i] < 0)
					b[i] = 0;

				// System.out.println(r[i]+" "+g[i]+" "+b[i]);

				hist_after_r[r[i]]++;
				hist_after_g[g[i]]++;
				hist_after_b[b[i]]++;

				// convert it back
				e[i] = (r[i] << 16) | (g[i] << 8) | b[i];

			}
			// convert e back to say jpg
			image.setRGB(0, 0, width, height, e, 0, width);
			ImageIO.write(image, "jpeg" /* "png" "jpeg" ... format desired */, new File("newout.jpg") /* target */);

			printhistogram(hist_refore_r, "hist_before_r.txt"); // before
																// stretchig ie
																// original
			printhistogram(hist_refore_g, "hist_before_g.txt");
			printhistogram(hist_refore_b, "hist_before_b.txt");
			printhistogram(hist_after_r, "hist_after_r.txt"); // after
																// stretching ie
																// modified ones
			printhistogram(hist_after_g, "hist_after_g.txt");
			printhistogram(hist_after_b, "hist_after_b.txt");

		} catch (Exception e) {
			System.err.println("Error: " + e);
			Thread.dumpStack();

		}
	}

	public void printhistogram(int[] hist, String file) {
		try {
			FileWriter op = new FileWriter("" + file);

			for (int i = 0; i < hist.length; ++i) {
				op.write("[" + i + "]=" + hist[i] + "\n");
			}
			op.close();
		} catch (Exception e) {
			System.err.println("Error2: " + e);
			Thread.dumpStack();
		}
	}
}
