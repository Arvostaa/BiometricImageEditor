package fingerprint;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class Thinning {

	public static BufferedImage image;
	private final int width;
	private final int height;
	private int[][] pixels;

	public Thinning(BufferedImage img) {
		image = img;
		width = img.getWidth();
		height = img.getHeight();
	}

	// *****************************SET [][]PIXELS , CONVERT []INT TO
	// []BYTE************************************ //
	
	/*static byte[] integersToBytes(int[] values) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for (int i = 0; i < values.length; ++i) {
			dos.writeInt(values[i]);
		}

		return baos.toByteArray();
	}

	public static int[][] convertToArray(BufferedImage image) throws IOException {

		if (image == null || image.getWidth() == 0 || image.getHeight() == 0)
			return null;

		// This returns bytes of data starting from the top left of the bitmap
		// image and goes down.
		// Top to bottom. Left to right.
		int[] intpixels = new int[image.getWidth() * image.getHeight()];
		try {
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), intpixels, 0, image.getWidth());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// byte[] pixels =
		// ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		byte[] pixels = integersToBytes(intpixels);
		final int width = image.getWidth();
		final int height = image.getHeight();

		int[][] result = new int[height][width];

		boolean done = false;
		boolean alreadyWentToNextByte = false;
		int byteIndex = 0;
		int row = 0;
		int col = 0;
		int numBits = 0;
		byte currentByte = pixels[byteIndex];
		while (!done) {
			alreadyWentToNextByte = false;

			result[row][col] = (currentByte & 0x80) >> 7;
			currentByte = (byte) (((int) currentByte) << 1);
			numBits++;

			if ((row == height - 1) && (col == width - 1)) {
				done = true;
			} else {
				col++;

				if (numBits == 8) {
					currentByte = pixels[++byteIndex];
					numBits = 0;
					alreadyWentToNextByte = true;
				}

				if (col == width) {
					row++;
					col = 0;

					if (!alreadyWentToNextByte) {
						currentByte = pixels[++byteIndex];
						numBits = 0;
					}
				}
			}
		}

		return result;
	}*/
	
	public int[][] getPixelsMatrix( final int[] array, final int rows, final int cols ) {
	    if (array.length != (rows*cols))
	        throw new IllegalArgumentException("Invalid array length");

	    int[][] arr = new int[rows][cols];
	    for ( int i = 0; i < rows; i++ )
	        System.arraycopy(array, (i*cols), arr[i], 0, cols);

	    return arr;
	}
	// *****************************************************************//
	
	private static final boolean[] buildKillsArray(int[] kills) {
		boolean[] ar = new boolean[256];
		Arrays.fill(ar, false);
		for (int i = 0; i < kills.length; ++i)
			ar[kills[i]] = true;
		return ar;
	}

	private static final boolean[] killsRound = buildKillsArray(new int[] { 3, 12, 48, 192, 6, 24, 96, 129, // -
																											// 2
																											// sasiadow
			14, 56, 131, 224, 7, 28, 112, 193, // - 3 sasiadow
			195, 135, 15, 30, 60, 120, 240, 225,// - 4 sasiadow
			// 31, 62, 124, 248, 241, 227, 199, 143,// - 5 sasiadow
			// 63, 126, 252, 249, 243, 231, 207, 159,//- 6 sasiadow
			// 254, 253, 251, 247, 239, 223, 190, 127,//- 7 sasiadow
	});

	private static final boolean canRound(int weight) {
		return killsRound[weight];
	}

	private static final boolean[] killsFinish = buildKillsArray(new int[] {
			// tablica usuniec:jesli suma = wartosc z tablicy, pixel bedzie
			// usuniety

			5, 13, 15, 20, 21, 22, 23, 29, 30, 31, 52, 53, 54, 55, 60, 61, 62, 63, 65, 67, 69, 71, 77, 79, 80, 81, 83,
			84, 85, 86, 87, 88, 89, 91, 92, 93, 94, 95, 97, 99, 101, 103, 109, 111, 113, 115, 116, 117, 118, 119, 120,
			121, 123, 124, 125, 126, 127, 133, 135, 141, 143, 149, 151, 157, 159, 181, 183, 189, 191, 195, 197, 199,
			205, 207, 208, 209, 211, 212, 213, 214, 215, 216, 217, 219, 220, 221, 222, 223, 225, 227, 229, 231, 237,
			239, 240, 241, 243, 244, 245, 246, 247, 248, 249, 251, 252, 253, 254, 255,

			3, 12, 48, 192, // - 2 sasiadow
			14, 56, 131, 224, // - 3 sasiadow'I'
			7, 28, 112, 193, // - 3 sasiadow 'L'
	});

	private static final boolean canKill(int weight) {
		return killsFinish[weight];
	}

	public int[][] getNeighbours(int x, int y) {

		int[][] neighbours = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

		// GORNY WIERSZ
		if (x - 1 < 0 || y - 1 < 0) { // nw
			neighbours[0][0] = 0;
		} else {

			if (pixels[x - 1][y - 1] == Color.WHITE.getRGB()) {
				neighbours[0][0] = 0;
			} else
				neighbours[0][0] = 1;
		}

		if (y - 1 < 0) { // n
			neighbours[0][1] = 0;
		} else {

			if (pixels[x][y - 1] == Color.WHITE.getRGB()) {
				neighbours[0][1] = 0;
			} else
				neighbours[0][1] = 1;
		}

		if (x + 1 >= height || y - 1 < 0) { // ne
			neighbours[0][2] = 0;
		} else {

			if (pixels[x + 1][y - 1] == Color.WHITE.getRGB()) {
				neighbours[0][0] = 0;
			} else
				neighbours[0][0] = 1;
		}

		// SRODKOWY WIERSZ

		if (x - 1 < 0) { // w
			neighbours[1][0] = 0;
		} else {

			if (pixels[x - 1][y] == Color.WHITE.getRGB()) {
				neighbours[1][0] = 0;
			} else
				neighbours[1][0] = 1;
		}
		neighbours[1][1] = 0; // x

		if (x + 1 <= height) { // e
			neighbours[1][2] = 0;
		} else {

			if (pixels[x + 1][y] == Color.WHITE.getRGB()) {
				neighbours[1][2] = 0;
			} else
				neighbours[1][2] = 1;
		}

		// DOLNY WIERSZ

		if (x - 1 < 0 || y + 1 >= width) { // sw
			neighbours[2][0] = 0;
		} else {

			if (pixels[x - 1][y + 1] == Color.WHITE.getRGB()) {
				neighbours[2][0] = 0;
			} else
				neighbours[2][0] = 1;
		}

		if (y + 1 >= width) { // s
			neighbours[2][1] = 0;
		} else {

			if (pixels[x][y + 1] == Color.WHITE.getRGB()) {
				neighbours[2][1] = 0;
			} else
				neighbours[2][1] = 1;
		}

		if (x + 1 >= height || y + 1 >= width) { // sw
			neighbours[2][2] = 0;
		} else {

			if (pixels[x + 1][y + 1] == Color.WHITE.getRGB()) {
				neighbours[2][2] = 0;
			} else
				neighbours[2][2] = 1;
		}

		return neighbours;
	}

	public int countWeight(int x, int y) {

		int[][] weightTable = { { 128, 1, 2 }, { 64, 0, 4 }, { 32, 16, 18 } };
		int[][] neighbours = getNeighbours(x, y);
		int weightValue = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {

				if (neighbours[i][j] == 1) {
					weightValue += neighbours[i][j];
				}
			}
		}
		// System.out.println("WEIGHT = " + weightValue);
		return weightValue;
	}

	public int hasNeighbourPixel(int x, int y, int posX, int posY) {

		if (x + posX >= 0 && x + posX <= width) {
			return 1;
		} else
			return 0;
	}

	public void thin() throws IOException {

		Color w, e, n, s, ne, nw, se, sw, c;
		boolean thinned;
		int posX, posY;
		int counter = 0;
		int[] pixelssmall = new int[width * height];
		try {
			image.getRGB(0, 0, width, height, pixelssmall, 0, width);
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		pixels = getPixelsMatrix(pixelssmall, height, width);
		
		BufferedImage outImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		do {
			thinned = true;

			// BORDER
			for (int i = 0; i < height; i++) { // i = y
				for (int j = 0; j < width; j++) { // j = x
					posX = i;
					posY = j;
				//	 System.out.println("i = " + i + ", j = " + j);

					// GORNY WIERSZ
					if (i - 1 < 0 || j - 1 < 0) {
						nw = Color.WHITE;
					} else
						// nw = new Color(image.getRGB(i - 1, j - 1));
						nw = new Color(pixels[i - 1][j - 1]);

					if (i - 1 < 0) {
						n = Color.WHITE;
					} else
						// n = new Color(image.getRGB(i, j - 1));
						n = new Color(pixels[i-1][j]);

					if (i - 1 < 0  || j + 1 >= width) {
						ne = Color.WHITE;
					} else
						// ne = new Color(image.getRGB(i + 1, j - 1));
						ne = new Color(pixels[i - 1][j + 1]);

					// SRODKOWY WIERSZ
					if (j - 1 < 0) {
						w = Color.WHITE;
					} else
						// w = new Color(image.getRGB(i - 1, j));
						w = new Color(pixels[i][j-1]);

					if (j + 1 >= width) {
						e = Color.WHITE;
					} else
						// e = new Color(image.getRGB(i + 1, j));
						e = new Color(pixels[i][j+1]);

					// DOLNY WIERSZ
					if (i + 1 >= height || j - 1 < 0) {
						sw = Color.WHITE;
					} else
						// sw = new Color(image.getRGB(i - 1, j + 1));
						sw = new Color(pixels[i+1][j - 1]);

					if (i + 1 >= height) {
						s = Color.WHITE;
					} else{
					//	System.out.println("JESTEM TU, ELO: " + i + ", j = " + j + "width:" + width + ", height:" + height);
						// s = new Color(image.getRGB(i, j + 1));
						s = new Color(pixels[i+1][j]);}

					if (i + 1 >= height || j + 1 >= width) {
						se = Color.WHITE;
					} else
						// se = new Color(image.getRGB(i + 1, j + 1));
						se = new Color(pixels[i + 1][j + 1]);
					// System.out.println(
					// "se" + se + ", s " + s + ", se" + se + ", e" + e + ", ne"
					// + ne + ", n" + n + ", nw" + nw);

					if (n.equals(Color.WHITE) || s.equals(Color.WHITE) || e.equals(Color.WHITE)

							|| w.equals(Color.WHITE)) {
						// System.out.println(i + ", " + j + " IS WHITE");
						c = Color.RED;
						// image.setRGB(i, j, c.getRGB());
						pixels[i][j] = c.getRGB();
					} // 3 - is a corner(2-red)}
					else if (ne.equals(Color.WHITE) || nw.equals(Color.WHITE) || se.equals(Color.WHITE)
							|| sw.equals(Color.WHITE)) {
						c = Color.MAGENTA;
						// image.setRGB(i, j, c.getRGB()); // 4 (3 - magenta)
						pixels[i][j] = c.getRGB();
					}
				}
			}

			// ROUND for 2 !!!
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					// c = new Color(image.getRGB(i, j));
					c = new Color(pixels[i][j]);
					if (!c.equals(Color.RED))
						continue;
					if (canRound(countWeight(i, j))) // if has 2/3/4 neighbours,
														// delete
						c = Color.GREEN;
					pixels[i][j] = c.getRGB();
					// image.setRGB(i, j, c.getRGB());

					// p.set(green); // 2 2=>4 (step 4)
				}
			}
			System.out.println("end of round for 2~~");

			// CLEAR
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					// c = new Color(image.getRGB(i, j));
					c = new Color(pixels[i][j]);
					if (c.equals(Color.GREEN)) { // if green - delete ziom
						c = Color.WHITE;
						// image.setRGB(i, j, c.getRGB());
						pixels[i][j] = c.getRGB();
						//// lete(); // delete 4, so 4 =>0 (step 5)
						thinned = false;
					}
				}
			}
			System.out.println("end of round for clear");

			// FINISH A
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					// c = new Color(image.getRGB(i, j));
					c = new Color(pixels[i][j]);
					if (!c.equals(Color.RED))
						continue;
					if (canKill(countWeight(i, j))) // if weight of 2 fits in
													// the table - delete (step
													// 6)
					{
						c = Color.WHITE;
						// image.setRGB(i, j, c.getRGB());
						pixels[i][j] = c.getRGB();
						thinned = false;
					} else
						c = Color.BLACK;
					// image.setRGB(i, j, c.getRGB());
					pixels[i][j] = c.getRGB();
				}
			}
			System.out.println("end of finish a");
			// FINISH B
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					// c = new Color(image.getRGB(i, j));
					c = new Color(pixels[i][j]);
					if (!c.equals(Color.MAGENTA))
						continue;
					if (canKill(countWeight(i, j))) // if weight of 3 fits in
													// the table - delete (step
													// 7)
					{
						c = Color.WHITE;
						// image.setRGB(i, j, c.getRGB());
						pixels[i][j] = c.getRGB();
						thinned = false;
					} else
						c = Color.BLACK;
					// image.setRGB(i, j, c.getRGB());
					pixels[i][j] = c.getRGB();
				}
			}
			System.out.println("end of finish b");

			counter++;
			// image.save();*/
		//} while (!thinned);
		} while (counter < 3);
	
		for (int y = 0; y < height; ++y) {
		    for (int x = 0; x < width; ++x) {
		        outImg.setRGB(x, y, pixels[y][x]);
		    }
		}

File outputfile = new File("nosiemathinned.jpg");
ImageIO.write(outImg, "png", outputfile);

		image = outImg;
		
		
	}
}
