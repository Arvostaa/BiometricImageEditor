package Histograms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GetHistogram extends JPanel {

	Dimension dimPic;
	BufferedImage picture;
	String option;
	int[] samples;
	int dimension;
	
	public GetHistogram(Dimension d, String color, File image, int dim) {
		dimension = dim;
		try {
			// to dimension the picture
			dimPic = d;
			option = color;
			picture = ImageIO.read(image);
		} catch (IOException ex) {
			Logger.getLogger(GetHistogram.class.getName()).log(Level.SEVERE, null, ex);
		}
		getHistogram();
	}

	public void getHistogram() {
		samples = new int[256];
		int maxNumSamples = 0;

		int allSamples = 0;

		int averageRGBvalue = 0;

		for (int i = 0; i < 255; i++) {
			samples[i] = 0;
		}

		for (int w = 0; w < (picture.getWidth()); w++) {

			for (int h = 0; h < (picture.getHeight()); h++) {

				int alpha = (0xff & (picture.getRGB(w, h) >> 24));
				int ired = (0xff & (picture.getRGB(w, h) >> 16));
				int igreen = (0xff & (picture.getRGB(w, h) >> 8));
				int iblue = (0xff & picture.getRGB(w, h));

				if (option.equals("red")) {
					samples[ired]++;
					if (samples[ired] > maxNumSamples) {
						maxNumSamples = samples[ired];
					}

				} else if (option.equals("green")) {
					samples[igreen]++;
					if (samples[igreen] > maxNumSamples) {
						maxNumSamples = samples[igreen];
					}
				} else if (option.equals("blue")) {
					samples[iblue]++;
					if (samples[iblue] > maxNumSamples) {
						maxNumSamples = samples[iblue];
					}
				} else if (option.equals("all")) {
					samples[iblue]++;
					samples[igreen]++;
					samples[ired]++;

					averageRGBvalue = (ired +igreen + iblue)/3;
					System.out.println(averageRGBvalue + "eloaverageeee");
					//System.out.println(samples[averageRGBvalue] + "samples[a]eeee");
					if (samples[averageRGBvalue] > maxNumSamples) {
						maxNumSamples = samples[averageRGBvalue];
						
					}
					
				}
			} // h
		} // w
			// normalizing

		for (int i = 0; i < 255; i++) {
			samples[i] = (int) ((samples[i] * 200) / (maxNumSamples));
		}
	}// getH

	public void paintComponent(Graphics g) {
		if (option.equals("red")) {
			g.setColor(Color.RED);
		} else if (option.equals("green")) {
			g.setColor(Color.GREEN);
		} else if (option.equals("blue")) {
			g.setColor(Color.BLUE);
		} else if (option.equals("all")) {
			g.setColor(Color.BLACK);
		}

		for (int i = 0; i < 255; i++) {
			// g.drawLine(i, 0, i, samples[i]);
			g.drawLine(i, dimension + 2, i, dimension + 2 - samples[i]);

		}
	}
}