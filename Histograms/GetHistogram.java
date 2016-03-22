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
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GetHistogram extends JPanel {

	// Dimension dimPic;
	// BufferedImage picture;
	String option;
	int[] samples;
	int dimension;
	Boolean stretched;

	public GetHistogram(String color, BufferedImage picture, int dim) {
		dimension = dim;
		
		//setBorder(BorderFactory.createLineBorder(Color.GRAY));

		option = color;
		getHistogram(picture);
	}

	public void getHistogram(BufferedImage picture) {
		samples = new int[256];
		stretched = false;
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

					averageRGBvalue = (ired + igreen + iblue) / 3;

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

		// repaint();
		if (option.equals("red")) {
			g.setColor(Color.RED);
		} else if (option.equals("green")) {
			g.setColor(Color.GREEN);
		} else if (option.equals("blue")) {
			g.setColor(Color.BLUE);
		} else if (option.equals("all")) {
			g.setColor(Color.BLACK);
		}
		if (stretched == false) {
			
			
			System.out.println("NOT STRETCHED");
			
			for (int i = 0; i < 255; i++) {
				// g.drawLine(i, 0, i, samples[i]);
				g.drawLine(i, 2 * dimension - 100, i, 2 * dimension - 100 - samples[i]); // REMOVE
																						// *2
																						// FOR
																						// STRETCHING
			}
		} else {

			System.out.println("STRETCHED");
			for (int i = 0; i < 255; i++) {
				// g.drawLine(i, 0, i, samples[i]);
				g.drawLine(i, dimension - 100, i, dimension - 100 - samples[i]); // REMOVE
																				// *2
																				// FOR
																				// STRETCHING
			}
			stretched = false;
		}
		

	}

	public void updateHistogram(int[] newSamples, String color) {
		System.out.println("UPDATE HIST " + color);
		samples = newSamples;
		option = color;
	}

	public void drawNewHistogram(Graphics g, String color) {
		System.out.println("RYSOWANY NOWY " + color);
		paintComponent(g);
	}

}