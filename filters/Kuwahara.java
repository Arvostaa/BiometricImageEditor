package filters;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Kuwahara {

	private int windowSize = 5;

	public Kuwahara() {
	}

	public BufferedImage kuwaharaFilter(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		int size2 = (windowSize + 1) / 2;
		int offset = (windowSize - 1) / 2; // CENTER PIXEL

		BufferedImage tempImage = image;
		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		int width2 = width + offset;
		int height2 = height + offset;
		Color c;
		float[][][] mean = new float[width2][height2][3];
		float[][][] variance = new float[width2][height2][3];
		double sumR, sum2R;
		double sumG, sum2G;
		double sumB, sum2B;
		int n, r, g, b, xbase, ybase;
		for (int y1 = 0 - offset; y1 < 0 + height; y1++) {
			for (int x1 = 0 - offset; x1 < 0 + width; x1++) {
				sumR = sumG = sumB = 0;
				sum2R = sum2G = sum2B = 0; //sum^2
				n = 0;
				for (int x2 = x1; x2 < x1 + size2; x2++) { // get sum of color values in area of 1/4 windowsSize
					for (int y2 = y1; y2 < y1 + size2; y2++) {
						if (x2 > 0 && x2 < width && y2 > 0 && y2 < height) {
							// System.out.println("Y2 : " + y2 + ", X2: " + x2);
							c = new Color(tempImage.getRGB(y2, x2));
							r = c.getRed();
							g = c.getGreen();
							b = c.getBlue();

							sumR += r;
							sum2R += r * r;

							sumG += g;
							sum2G += g * g;

							sumB += b;
							sum2B += b * b;

							n++;
						} else { // ????
							n++;
						}
					}
				}
				mean[x1 + offset][y1 + offset][0] = (float) (sumR / n); // get mean of color values in area of 1/4 windowsSize (center pixel)
				mean[x1 + offset][y1 + offset][1] = (float) (sumG / n);
				mean[x1 + offset][y1 + offset][2] = (float) (sumB / n);

				variance[x1 + offset][y1 + offset][0] = (float) (sum2R - sumR * sumR / n);
				variance[x1 + offset][y1 + offset][1] = (float) (sum2G - sumG * sumG / n);
				variance[x1 + offset][y1 + offset][2] = (float) (sum2B - sumB * sumB / n);
			}
		}

		int xbase2 = 0, ybase2 = 0;
		float var, min;
		for (int y1 = 0; y1 < 0 + height; y1++) {
			for (int x1 = 0; x1 < 0 + width; x1++) {

				// Red channel
				min = Float.MAX_VALUE;
				xbase = x1;
				ybase = y1;
				var = variance[xbase][ybase][0];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				xbase = x1 + offset;
				var = variance[xbase][ybase][0];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				ybase = y1 + offset;
				var = variance[xbase][ybase][0];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				xbase = x1;
				var = variance[xbase][ybase][0];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}

				r = (int) (mean[xbase2][ybase2][0] + 0.5);

				// Green channel
				min = Float.MAX_VALUE;
				xbase = x1;
				ybase = y1;
				var = variance[xbase][ybase][1];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				xbase = x1 + offset;
				var = variance[xbase][ybase][1];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				ybase = y1 + offset;
				var = variance[xbase][ybase][1];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				xbase = x1;
				var = variance[xbase][ybase][1];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}

				g = (int) (mean[xbase2][ybase2][1] + 0.5);

				// Blue channel
				min = Float.MAX_VALUE;
				xbase = x1;
				ybase = y1;
				var = variance[xbase][ybase][2];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				xbase = x1 + offset;
				var = variance[xbase][ybase][2];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				ybase = y1 + offset;
				var = variance[xbase][ybase][2];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}
				xbase = x1;
				var = variance[xbase][ybase][2];
				if (var < min) {
					min = var;
					xbase2 = xbase;
					ybase2 = ybase;
				}

				b = (int) (mean[xbase2][ybase2][2] + 0.5);
				c = new Color(r, g, b);
				outputImage.setRGB(y1, x1, c.getRGB());
			}
		}
		return outputImage;
	}

}