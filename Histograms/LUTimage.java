package Histograms;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

import convertRGB.Pane;
import convertRGB.ImagePanel;

public class LUTimage {

	private int[] lutR;
	private int[] lutG;
	private int[] lutB;
	private Pane mainPanel;
	private LookupTable lookupTable;

	public LUTimage(Pane mainWindow) {
		this.mainPanel = mainWindow;
	}

	public void expLUT(double x) { // darken/lightne
		int[] lut = new int[256];
		double xx = x;
		if (x > 3) {
			xx = 3;
		}
		if (x < 0.7) {
			xx = 0.7;
		}
		for (int i = 0; i < 256; i++) {
			lut[i] = (int) Math.round(Math.pow(i, xx));
		}
		normalizeLUT(lut);

		lutR = lutG = lutB = lut;

	}

	public void repaintImage(ImagePanel image) {
		Color temp;
		Color c;
		for (int i = 0; i < image.img.getWidth(); i++) {
			for (int j = 0; j < image.img.getHeight(); j++) {
				temp = new Color(image.img.getRGB(i, j));
				c = new Color(lutR[temp.getRed()], lutG[temp.getGreen()], lutB[temp.getBlue()]);
				image.img.setRGB(i, j, c.getRGB());
				image.img.setRGB(i, j, c.getRGB());
			}
		}
	}

	public void stretchingHistogram(int a, int b) {
		int[] lut = new int[256];
		if (a > b) {
			int temp = b;
			b = a;
			a = temp;
		}

		for (int i = 0; i < lut.length; i++) {
			if (i <= a) {
				lut[i] = 0;
			} else if (i >= b) {
				lut[i] = 255;
			} else {
				lut[i] = (int) ((255.0 / (b - a)) * (i - a));
			}
		}
		lutR = lutG = lutB = lut;
	}

	public void histogramEqualization() {
		lutR = new int[256];
		lutG = new int[256];
		lutB = new int[256];
		BufferedImage bf = mainPanel.imagePanel.img;
		for (int i = 0; i < bf.getWidth(); i++) {
			for (int j = 0; j < bf.getHeight(); j++) {
				Color c = new Color(bf.getRGB(i, j));
				lutR[Math.round(c.getRed())]++;
				lutG[Math.round(c.getGreen())]++;
				lutB[Math.round(c.getBlue())]++;
			}
		}
		for (int i = 0; i < 255; i++) {
			lutR[i + 1] += lutR[i];
			lutG[i + 1] += lutG[i];
			lutB[i + 1] += lutB[i];
		}
		int R = 0, G = 0, B = 0;
		for (int i = 0; i < 256; i++) {
			if (lutR[i] > 0 && R == 0) {
				R = lutR[i];
			}
			if (lutG[i] > 0 && G == 0) {
				G = lutG[i];
			}
			if (lutB[i] > 0 && B == 0) {
				B = lutB[i];
			}
			if (R != 0 && G != 0 && B != 0) {
				break;
			}
		}
		for (int i = 0; i < 256; i++) {
			lutR[i] = (int) (255.0 * (lutR[i] - R)) / (bf.getHeight() * bf.getWidth() - R);
			lutG[i] = (int) (255.0 * (lutG[i] - G)) / (bf.getHeight() * bf.getWidth() - G);
			lutB[i] = (int) (255.0 * (lutB[i] - B)) / (bf.getHeight() * bf.getWidth() - B);
		}
	}

	public void normalizeLUT(int[] lut) {
		double minValue = 0;
		for (int i = 0; i < lut.length; i++) {
			if (lut[i] < minValue) {
				minValue = lut[i];
			}
		}
		for (int i = 0; i < lut.length; i++) {
			lut[i] -= minValue;
		}
		double maxValue = 0;
		for (int i = 0; i < lut.length; i++) {
			if (lut[i] > maxValue) {
				maxValue = lut[i];
			}
		}
		if (maxValue == 0) {
			return;
		}
		double scale = 255.0 / maxValue;
		for (int i = 0; i < lut.length; i++) {
			lut[i] = (int) Math.round(lut[i] * scale);
			if (lut[i] > 255) {
				lut[i] = 255;
			}
			if (lut[i] < 0) {
				lut[i] = 0;
			}
		}
	}

	// BINARIZATION//

	public void grayScale(ImagePanel image) {
		for (int i = 0; i < image.img.getWidth(); i++) {
			for (int j = 0; j < image.img.getHeight(); j++) {
				Color temp = new Color(image.img.getRGB(i, j));
				int intColor = Math.round((temp.getRed() + temp.getGreen() + temp.getBlue()) / 3);
				Color c = new Color(intColor, intColor, intColor);
				image.img.setRGB(i, j, c.getRGB());
				image.img.setRGB(i, j, c.getRGB());
			}
		}
	}

	public void localThresholdNiblack(ImagePanel image, double k, int dlugosc) {

		BufferedImage tempImage = new BufferedImage(image.img.getWidth(), image.img.getHeight(), BufferedImage.TYPE_INT_RGB);
				
		int height = image.img.getHeight();
		int width = image.img.getWidth();
		int[][] niblacklut = new int[width][height];
		int square = 0;
		for (int i = 1; i < dlugosc; i = i + 2) {
			square++;
		}
		int[] tempSquare = new int[(int) Math.pow(dlugosc, 2)];
		double sum = 0;
		double average = 0;
		double variance = 0;
		int addToTable = 0;
		double th = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				sum = 0;
				average = 0;
				variance = 0;
				for (int x = i - square; x < i + dlugosc - square; x++) {
					for (int y = j - square; y < j + dlugosc - square; y++) {
						if (x < 0 || y < 0 || x > (width - 1) || y > (height - 1)) {
							addToTable++;
						} else {
							sum += (image.img.getRGB(x, y) >> 16) & 0xFF;
							tempSquare[addToTable] = (image.img.getRGB(x, y) >> 16) & 0xFF;
							addToTable++;
						}
					}
				}
				average = sum / Math.pow(dlugosc, 2);
				for (int z = 0; z < Math.pow(dlugosc, 2); z++) {
					variance += Math.pow((tempSquare[z] - average), 2);
				}
				variance = variance / Math.pow(dlugosc, 2);
				th = average + k * Math.sqrt(variance);
				if (((image.img.getRGB(i, j) >> 16) & 0xFF) < th) {
					tempImage.setRGB(i, j, new Color(0, 0, 0).getRGB());					
					addToTable = 0;
				} else {
					tempImage.setRGB(i, j, new Color(255, 255, 255).getRGB());					
					addToTable = 0;
				}
			}
		}
		image.img = tempImage;
		image.repaint();
	}

	public void threshold(int steps) {
		int[] lut = new int[256];
		for (int i = 0; i < lut.length; i++) {
			if (i < steps) {
				lut[i] = 0;
			}
			if (i >= steps) {
				lut[i] = 255;
			}
		}
		lutR = lutG = lutB = lut;
	}

	public void otsu() {
		int sum = 0;
		int sumB = 0;
		int wB = 0;
		int wF = 0;
		int mB;
		int mF;
		int max = 0;
		int between = 0;
		int threshold = 0;
		
		int width = mainPanel.imagePanel.img.getWidth();
		int height = mainPanel.imagePanel.img.getHeight();
		BufferedImage tempImage = mainPanel.imagePanel.img;
		int amountOfPixels = width*height;
		
		grayScale(mainPanel.imagePanel);
		
		
		//System.out.println(amountOfPixels);
		int[] histogram = new int[256];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Color c = new Color(tempImage.getRGB(i, j));
				histogram[c.getRed()]++;
			}
		}
		for (int i = 0; i < histogram.length; i++) {
			wB += histogram[i];
			if (wB == 0)
				continue;
			wF = amountOfPixels - wB;
			if (wF == 0)
				break;
			sumB += i * histogram[i];
			mB = sumB / wB;
			mF = (sum - sumB) / wF;
			between = wB * wF * (mB - mF) * (mB - mF);
			if (between > max) {
				threshold = i;
				max = between;
			}
		}
		threshold(threshold);
		repaintImage(mainPanel.imagePanel);
	}
	
}
