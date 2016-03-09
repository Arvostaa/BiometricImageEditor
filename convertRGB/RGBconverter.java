package convertRGB;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class RGBconverter extends Component {

	// public static void main(String[] foo) {
	// new RGBconverter();
	// }

	public void printPixelARGB(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);

	}

	private void marchThroughImage(BufferedImage image) {

		Scanner keyboard = new Scanner(System.in);
		System.out.println("Rzeknij liczbê");
		int x1 = keyboard.nextInt();

		int w = image.getWidth();
		int h = image.getHeight();
		System.out.println("width, height: " + w + ", " + h);

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				System.out.println("x,y: " + j + ", " + i);
				int pixel = image.getRGB(j, i);

				int r = (pixel >> 16) & 0xff + x1;
				int g = (pixel >> 8) & 0xff + x1;
				int b = (pixel) & 0xff + x1;
				int a = (pixel >> 24) & 0xff + x1;
				int col = (a << 24) | (r << 16) | (g << 8) | b;
				image.setRGB(j, i, col);

				printPixelARGB(pixel);
				System.out.println("");
			}
		}

	}

	public RGBconverter() {

		/*
		 * 
		 * try { // the line that reads the image file BufferedImage image =
		 * ImageIO.read(new File("/Users/al/some-picture.jpg")); // work with
		 * the image here ... } catch (IOException e) { // log the exception //
		 * re-throw if desired }
		 */

		try {
			// get the BufferedImage, using the ImageIO class
			BufferedImage image = ImageIO.read(this.getClass().getResource("test.jpg"));
			marchThroughImage(image);
			File f = new File("MyFile.png");
			ImageIO.write(image, "PNG", f);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}