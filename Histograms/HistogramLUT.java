package Histograms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class HistogramLUT extends JPanel {

	public ArrayList<int[]> imageHistogram;
int height;
	
	HistogramLUT(BufferedImage image){
		
		imageHistogram = histogramLUT(image);
		height = image.getHeight();
	
		
		
	}
	
	
	
	// Get the histogram equalization lookup table for separate R, G, B channels
		public ArrayList<int[]> histogramLUT(BufferedImage input) {

			// Get an image histogram - calculated values by R, G, B channels
			ArrayList<int[]> imageHist = createImageHistogram(input);

			// Create the lookup table
			ArrayList<int[]> imageLUT = new ArrayList<int[]>();

			// Fill the lookup table
			int[] rhistogram = new int[256];
			int[] ghistogram = new int[256];
			int[] bhistogram = new int[256];

			for (int i = 0; i < rhistogram.length; i++)
				rhistogram[i] = 0;
			for (int i = 0; i < ghistogram.length; i++)
				ghistogram[i] = 0;
			for (int i = 0; i < bhistogram.length; i++)
				bhistogram[i] = 0;

			long sumr = 0;
			long sumg = 0;
			long sumb = 0;

			for (int i = 0; i < rhistogram.length; i++) {
				sumr += imageHist.get(0)[i];
				int valr = (int) sumr;
				if (valr > 255) {
					rhistogram[i] = 255;
				} else
					rhistogram[i] = valr;

				sumg += imageHist.get(1)[i];
				int valg = (int) sumg;
				if (valg > 255) {
					ghistogram[i] = 255;
				} else
					ghistogram[i] = valg;

				sumb += imageHist.get(2)[i];
				int valb = (int) sumb;
				if (valb > 255) {
					bhistogram[i] = 255;
				} else
					bhistogram[i] = valb;
			}

			imageLUT.add(rhistogram);
			imageLUT.add(ghistogram);
			imageLUT.add(bhistogram);

			return imageLUT;

		}
	
	
	// creates an ArrayList containing histogram VALUES for separate R, G, B
		
	public ArrayList<int[]> createImageHistogram(BufferedImage input){

			int[] rhistogram = new int[256];
			int[] ghistogram = new int[256];
			int[] bhistogram = new int[256];

			for (int i = 0; i < rhistogram.length; i++)
				rhistogram[i] = 0;
			for (int i = 0; i < ghistogram.length; i++)
				ghistogram[i] = 0;
			for (int i = 0; i < bhistogram.length; i++)
				bhistogram[i] = 0;

			for (int i = 0; i < input.getWidth(); i++) {
				for (int j = 0; j < input.getHeight(); j++) {

					int red = new Color(input.getRGB(i, j)).getRed();
					int green = new Color(input.getRGB(i, j)).getGreen();
					int blue = new Color(input.getRGB(i, j)).getBlue();

					// Increase the values of colors
					rhistogram[red]++;
					ghistogram[green]++;
					bhistogram[blue]++;

				}
			}

			ArrayList<int[]> hist = new ArrayList<int[]>();
			hist.add(rhistogram);
			hist.add(ghistogram);
			hist.add(bhistogram);

			return hist;

		}
	
	public void paintComponent(Graphics g) {
		/*if (option.equals("red")) {
			g.setColor(Color.RED);
		} else if (option.equals("green")) {
			g.setColor(Color.GREEN);
		} else if (option.equals("blue")) {
			g.setColor(Color.BLUE);
		} else if (option.equals("all")) {
			g.setColor(Color.BLACK);
		}*/
		g.setColor(Color.RED);

		for (int i = 0; i < 255; i++) {
			// g.drawLine(i, 0, i, samples[i]);
			g.drawLine(i, 2*height + 2, i, 2*height + 2 - imageHistogram.get(0)[i]);

		}
		
		g.setColor(Color.GREEN);
/*
		for (int i = 270; i < 525; i++) {
			// g.drawLine(i, 0, i, samples[i]);
			g.drawLine(i, 2*height + 2, i, 2*height + 2 - imageHistogram.get(1)[i]);

		}*/
		
	/*	g.setColor(Color.RED);

		for (int i = 0; i < 255; i++) {
			// g.drawLine(i, 0, i, samples[i]);
			g.drawLine(i, 2*height + 2, i, 2*height + 2 - imageHistogram.get(0)[i]);

		}*/
		
		
	}
	
		
		
		
	}

