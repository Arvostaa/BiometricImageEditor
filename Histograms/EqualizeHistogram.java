package Histograms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import convertRGB.RGBimage;

public class EqualizeHistogram extends JPanel {

	private static BufferedImage mainImage, equalizedImage;
	private static JButton button;
	private static String path;

	public static ArrayList<int[]> rgbValues;

	EqualizeHistogram(RGBimage image) {

		button = new JButton("Equalize ");
		path = image.sname;
		System.out.println("sciezka: " + path);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					mainImage = ImageIO.read(new File(path));
					equalizedImage = histogramEqualization(mainImage);
					writeImage("equalizedImage");
				} catch (IOException e1) {

					e1.printStackTrace();
				}

			}
		});

		setLayout(new BorderLayout(4, 4));
		add("North", image.imgContainer);
		add("South", button);
	}

	EqualizeHistogram() {

	}

	private static void writeImage(String output) throws IOException {
		File file = new File(output + ".jpg");
		ImageIO.write(equalizedImage, "jpg", file);
	}

	public static BufferedImage histogramEqualization(BufferedImage original) {

		int red;
		int green;
		int blue;
		int alpha;
		int newPixel = 0;

		// LUT for histogram equalization
		ArrayList<int[]> histLUT = histogramEqualizationLUT(original);
		rgbValues = histLUT;

		BufferedImage histogramEQ = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {

				// pixels by r g b
				alpha = new Color(original.getRGB(i, j)).getAlpha();
				red = new Color(original.getRGB(i, j)).getRed();
				green = new Color(original.getRGB(i, j)).getGreen();
				blue = new Color(original.getRGB(i, j)).getBlue();

				// Set new pixel values using the histogram LUT
				red = histLUT.get(0)[red];
				green = histLUT.get(1)[green];
				blue = histLUT.get(2)[blue];

				// back to original format
				newPixel = colorToRGB(alpha, red, green, blue);

				// write pixels into image
				histogramEQ.setRGB(i, j, newPixel);

			}
		}
		return histogramEQ;
	}

	// Get the histogram equalization lookup table for separate R, G, B channels
	private static ArrayList<int[]> histogramEqualizationLUT(BufferedImage input) {

		// Get an image histogram - calculated values by R, G, B channels
		ArrayList<int[]> imageHist = imageHistogram(input);

		// Create the lookup table
		ArrayList<int[]> imageLUT = new ArrayList<int[]>();

		// Fill the lookup table
		int[] rhistogram = new int[256];
		int[] ghistogram = new int[256];
		int[] bhistogram = new int[256];
		int[] rgbhistogram = new int[256];

		for (int i = 0; i < rhistogram.length; i++)
			rhistogram[i] = 0;
		for (int i = 0; i < ghistogram.length; i++)
			ghistogram[i] = 0;
		for (int i = 0; i < bhistogram.length; i++)
			bhistogram[i] = 0;
		for (int i = 0; i < rgbhistogram.length; i++)
			rgbhistogram[i] = 0;

		long sumr = 0;
		long sumg = 0;
		long sumb = 0;
		long sumrgb = 0;

		// Calculate the scale factor
		float scale_factor = (float) (255.0 / (input.getWidth() * input.getHeight()));

		for (int i = 0; i < rhistogram.length; i++) {

			// calculate r value
			sumr += imageHist.get(0)[i];
			int valr = (int) (sumr * scale_factor); // obliczam
													// dystrybuantê!!!!!!!!!!!
			if (valr > 255) {
				rhistogram[i] = 255;
			} else
				rhistogram[i] = valr;
			// calculate g value
			sumg += imageHist.get(1)[i];
			int valg = (int) (sumg * scale_factor);
			if (valg > 255) {
				ghistogram[i] = 255;
			} else
				ghistogram[i] = valg;

			// calculate b value
			sumb += imageHist.get(2)[i];
			int valb = (int) (sumb * scale_factor);
			if (valb > 255) {
				bhistogram[i] = 255;
			} else
				bhistogram[i] = valb;

			// rgb value
			sumrgb += imageHist.get(3)[i];
			int valrgb = (int) (sumrgb * scale_factor);
			if (valrgb > 255) {
				rgbhistogram[i] = 255;
			} else
				rgbhistogram[i] = valb;
		}

		imageLUT.add(rhistogram);
		imageLUT.add(ghistogram);
		imageLUT.add(bhistogram);
		imageLUT.add(rgbhistogram);

		return imageLUT;

	}

	// Return an ArrayList containing histogram values for separate R, G, B
	// channels
	public static ArrayList<int[]> imageHistogram(BufferedImage input) {

		int[] rhistogram = new int[256];
		int[] ghistogram = new int[256];
		int[] bhistogram = new int[256];
		int[] rgbhistogram = new int[256];

		int rgbAverage;

		for (int i = 0; i < rhistogram.length; i++)
			rhistogram[i] = 0;
		for (int i = 0; i < ghistogram.length; i++)
			ghistogram[i] = 0;
		for (int i = 0; i < bhistogram.length; i++)
			bhistogram[i] = 0;
		for (int i = 0; i < rgbhistogram.length; i++)
			rgbhistogram[i] = 0;

		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {

				int red = new Color(input.getRGB(i, j)).getRed();
				int green = new Color(input.getRGB(i, j)).getGreen();
				int blue = new Color(input.getRGB(i, j)).getBlue();

				rgbAverage = (red + green + blue) / 3;

				// Increase the values of colors
				rhistogram[red]++;
				ghistogram[green]++;
				bhistogram[blue]++;
				rgbhistogram[rgbAverage]++;

			}
		}

		ArrayList<int[]> hist = new ArrayList<int[]>();
		hist.add(rhistogram);
		hist.add(ghistogram);
		hist.add(bhistogram);
		hist.add(rgbhistogram);

		return hist;

	}

	// Convert R, G, B, Alpha to standard 8 bit
	private static int colorToRGB(int alpha, int red, int green, int blue) {

		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;
	}
}