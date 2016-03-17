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

	/*
	 * public static void main(String[] args) throws IOException {
	 * 
	 * File original_f = new File(args[0] + ".jpg"); String output_f = args[1];
	 * mainImage = ImageIO.read(original_f); equalizedImage =
	 * histogramEqualization(mainImage); writeImage(output_f);
	 * 
	 * }
	 */

	EqualizeHistogram(RGBimage image) {
		System.out.println("EJSIEMAEQQQQQQQQQQQQQQQQQQQQQQQ");
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
					// TODO Auto-generated catch block
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

		// Get the Lookup table for histogram equalization
		ArrayList<int[]> histLUT = histogramEqualizationLUT(original);
		rgbValues = histLUT;

		BufferedImage histogramEQ = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {

				// Get pixels by R, G, B
				alpha = new Color(original.getRGB(i, j)).getAlpha();
				red = new Color(original.getRGB(i, j)).getRed();
				green = new Color(original.getRGB(i, j)).getGreen();
				blue = new Color(original.getRGB(i, j)).getBlue();

				// Set new pixel values using the histogram lookup table
				red = histLUT.get(0)[red];
				green = histLUT.get(1)[green];
				blue = histLUT.get(2)[blue];

				// Return back to original format
				newPixel = colorToRGB(alpha, red, green, blue);

				// Write pixels into image
				histogramEQ.setRGB(i, j, newPixel);

			}
		}

		return histogramEQ;

	}

	public static BufferedImage histogramStretching(BufferedImage original) throws IOException {

		int width = original.getWidth();
        int height = original.getHeight();
		
        int min = 100;
		int max = 255;
	
	//	BufferedImage histogramEQ = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

		int[] r = new int[width * height];
        int[] g = new int[width * height];
        int[] b = new int[width * height];
		int[] data = new int[width * height];
		int[] e = new int[width * height];
		
		original.getRGB(0, 0, width, height, data, 0, width);

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
              
              //System.out.println(r[i]+" "+g[i]+" "+b[i]);

              //stretch them to 0 to 255
              r[i] = (int) (1.0*( r[i] - min) / (max - min) * 255);
              g[i] = (int) (1.0*( g[i] - min) / (max - min) * 255);
              b[i] = (int) (1.0*( b[i] - min) / (max - min) * 255);
              
              if(r[i]> 255) r[i]=255;
              if(g[i]> 255) g[i]=255;
              if(b[i]> 255) b[i]=255;
              
              if(r[i]<0) r[i]=0;
              if(g[i]<0) g[i]=0;
              if(b[i]<0) b[i]=0;

              //System.out.println(r[i]+" "+g[i]+" "+b[i]);
              
              hist_after_r[r[i]]++;
              hist_after_g[g[i]]++;
              hist_after_b[b[i]]++;

              //convert it back
              e[i] = (r[i] << 16) | (g[i] << 8) | b[i];

			}
			
		 original.setRGB(0, 0, width, height, e, 0, width);
		 ImageIO.write(original, "jpeg" /* "png" "jpeg" ... format desired */,
                 new File("kurwa3.jpg") /* target */);
		 

         printhistogram(hist_refore_r, "hist_before_r.txt"); //before stretchig ie original
         printhistogram(hist_refore_g, "hist_before_g.txt");
         printhistogram(hist_refore_b, "hist_before_b.txt");
         printhistogram(hist_after_r, "hist_after_r.txt");  //after stretching ie modified ones
         printhistogram(hist_after_g, "hist_after_g.txt");
         printhistogram(hist_after_b, "hist_after_b.txt");
		return original;

	}
	
	 static void printhistogram(int[] hist, String file) {
	        try {
	            FileWriter op = new FileWriter(""+file);

	            for (int i = 0; i < hist.length; ++i) {
	                op.write("[" + i + "]=" + hist[i]+"\n");
	            }
	            op.close();
	        } catch (Exception e) {
	            System.err.println("Error2: " + e);
	            Thread.dumpStack();
	        }
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

		for (int i = 0; i < rhistogram.length; i++)
			rhistogram[i] = 0;
		for (int i = 0; i < ghistogram.length; i++)
			ghistogram[i] = 0;
		for (int i = 0; i < bhistogram.length; i++)
			bhistogram[i] = 0;

		long sumr = 0;
		long sumg = 0;
		long sumb = 0;

		// Calculate the scale factor
		float scale_factor = (float) (255.0 / (input.getWidth() * input.getHeight()));

		for (int i = 0; i < rhistogram.length; i++) {
			sumr += imageHist.get(0)[i];
			int valr = (int) (sumr * scale_factor); // obliczam
													// dystrybuantê!!!!!!!!!!!
			if (valr > 255) {
				rhistogram[i] = 255;
			} else
				rhistogram[i] = valr;

			sumg += imageHist.get(1)[i];
			int valg = (int) (sumg * scale_factor);
			if (valg > 255) {
				ghistogram[i] = 255;
			} else
				ghistogram[i] = valg;

			sumb += imageHist.get(2)[i];
			int valb = (int) (sumb * scale_factor);
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

	private static ArrayList<int[]> histogramStretchingLUT(BufferedImage input) {

		int min = 105;
		int max = 230;

		// int width = input.getWidth();
		// int height = input.getHeight();

		// Get an image histogram - calculated values by R, G, B channels
		ArrayList<int[]> imageHist = imageHistogram(input);

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

		//// ALGORYTM...////
		// Calculate the scale factor
		// float scale_factor = (float) (255.0 / (input.getWidth() *
		//// input.getHeight()));

		for (int i = 0; i < rhistogram.length; i++) {
			// sumr += imageHist.get(0)[i];
			// int valr = (int) (sumr * scale_factor); //obliczam
			// dystrybuantê!!!!!!!!!!!
			int valr = (int) (1.0 * (imageHist.get(0)[i] - min) / (max - min) * 255);
			if (valr > 255) {
				rhistogram[i] = 255;
			} else if (valr < 0) {
				rhistogram[i] = 0;
			} else
				rhistogram[i] = valr;
			////// g
			int valg = (int) (1.0 * (imageHist.get(1)[i] - min) / (max - min) * 255);
			if (valg > 255) {
				ghistogram[i] = 255;
			} else if (valg < 0) {
				ghistogram[i] = 0;
			} else
				ghistogram[i] = valg;
			/// b
			int valb = (int) (1.0 * (imageHist.get(0)[i] - min) / (max - min) * 255);
			if (valb > 255) {
				bhistogram[i] = 255;
			} else if (valb < 0) {
				bhistogram[i] = 0;
			} else
				bhistogram[i] = valr;
		}
		/////////////////////////////
		imageLUT.add(rhistogram);
		imageLUT.add(ghistogram);
		imageLUT.add(bhistogram);

		return imageLUT;

	}

	// Return an ArrayList containing histogram values for separate R, G, B
	// channels
	public static ArrayList<int[]> imageHistogram(BufferedImage input) {

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

	public static BufferedImage imageStretchHistogram(BufferedImage input) {
		int min = 100;
		int max = 255;

		int[] rhistogram = new int[256];
		int[] ghistogram = new int[256];
		int[] bhistogram = new int[256];

		int width = input.getWidth();
		int height = input.getHeight();
		int[] e = new int[width * height];

		BufferedImage histogramStr = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

		for (int i = 0; i < rhistogram.length; i++)
			rhistogram[i] = 0;
		for (int i = 0; i < ghistogram.length; i++)
			ghistogram[i] = 0;
		for (int i = 0; i < bhistogram.length; i++)
			bhistogram[i] = 0;

		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {

				int red = new Color(input.getRGB(i, j)).getRed();
				red = (int) (1.0 * (red - min) / (max - min) * 255);
				if (red > 255) {
					red = 255;
				}
				if (red < 0) {
					red = 0;
				}
				rhistogram[red]++;
				////// g
				int green = new Color(input.getRGB(i, j)).getGreen();
				green = (int) (1.0 * (green - min) / (max - min) * 255);
				if (green > 255) {
					green = 255;
				}
				if (green < 0) {
					green = 0;
				}
				ghistogram[green]++;
				/// b
				int blue = new Color(input.getRGB(i, j)).getBlue();
				blue = (int) (1.0 * (blue - min) / (max - min) * 255);
				if (blue > 255) {
					blue = 255;
				}
				if (blue < 0) {
					blue = 0;
				}
				bhistogram[blue]++;

				e[i] = (red << 16) | (green << 8) | blue;
			}
		}

		ArrayList<int[]> hist = new ArrayList<int[]>();
		hist.add(rhistogram);
		hist.add(ghistogram);
		hist.add(bhistogram);

		histogramStr.setRGB(0, 0, width, height, e, 0, width);

		return histogramStr;

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