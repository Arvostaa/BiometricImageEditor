package convertRGB;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RGBimage {

	public BufferedImage img;
	public String sname;
	public JLabel imgContainer;
	public File selectedFile;
	public String selectedPath;
	public Dimension d;

	public void saveImage(String imageName) throws IOException {

		ImageInputStream iis = ImageIO.createImageInputStream(selectedFile);
		
		// get all currently registered readers that recognize the image format

		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

		if (!iter.hasNext()) {

			throw new RuntimeException("No readers found!");

		}

		// get the first reader

		ImageReader reader = iter.next();
		String format = reader.getFormatName();
		iis.close();
		File f = new File(imageName+"."+format);
		ImageIO.write(img, format, f);
	}

	public static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		return bi;
	}

	public void scaleImage(File file) throws IOException {
		d = imgContainer.getPreferredSize();

		img = ImageIO.read(file);
		img.getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH); // mam
																		// preferowany
																		// wymiar

		if (img.getHeight() > d.getHeight()) {
			System.out.println("BEFORE: " + img.getHeight() + ", " + img.getWidth());
			Double scale = new Double(img.getHeight() / d.getHeight());
			int newWidth = new Double(img.getWidth() / scale).intValue();
			BufferedImage resizedImage = resize(img, newWidth, new Double(d.getHeight()).intValue());
			d.width = newWidth;
			img = resizedImage;
			System.out.println("AFTER: " + img.getHeight() + ", " + img.getWidth());
		}

	}
	public static BufferedImage deepCopy(BufferedImage bi) {
	    ColorModel cm = bi.getColorModel();
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
	    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	public void affineScaleImage(){
		
		BufferedImage before = deepCopy(img);
		d = imgContainer.getPreferredSize();
		double scale = d.getHeight()/(double)img.getHeight();
			int w = img.getWidth();
			int h = img.getHeight();
			BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			AffineTransform at = new AffineTransform();
			at.scale(scale, scale);
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			img = scaleOp.filter(before, after);
			
			//img = after;

			//big = bi.createGraphics();
			//big.drawImage(after, 0, 0, this);

	}
	
	

	public void setNewMainImage() throws IOException {

		d = imgContainer.getPreferredSize();
		System.out.println("dddddd: " + d.width + d.height);
		
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			selectedFile = fc.getSelectedFile();
			 
			sname = selectedFile.getAbsolutePath();
			img = ImageIO.read(selectedFile);
		//BufferedImage scaledImG = affineScaleImage(img);
		//img = scaledImG;
		affineScaleImage();
			
			System.out.println("IMMMGGG" + img.getWidth() + " " +img.getHeight());
			System.out.println("wynik" + d.getWidth()*(d.getHeight()/(double)img.getHeight()));
			//SCALE IMAGE //
			
		//	imgContainer.setIcon(new ImageIcon(img.getScaledInstance((int)(d.getWidth()*(d.getHeight()/(double)img.getHeight())), d.height, Image.SCALE_SMOOTH)));
		imgContainer.setIcon(new ImageIcon(img));
			imgContainer.setHorizontalAlignment(JLabel.LEFT);
			imgContainer.setVerticalAlignment(JLabel.NORTH);

		}
	}

	public void copyCreatedNewMainImage(File file) throws IOException {

		img = ImageIO.read(file);
		sname = file.getAbsolutePath();
		selectedFile = file; 
		
		affineScaleImage();
		
		//scale image //

		imgContainer.setIcon(new ImageIcon(img));
		imgContainer.setHorizontalAlignment(JLabel.LEFT);
		imgContainer.setVerticalAlignment(JLabel.NORTH);

		System.out.println(sname);
	}

	public RGBimage() {

		imgContainer = new JLabel();
		imgContainer.setOpaque(true);
		imgContainer.setBackground(new Color(255, 200, 255));

	}

	public void imageLoad() throws IOException {

		imgContainer = new JLabel();

		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			String sname = selectedFile.getAbsolutePath();
			img = ImageIO.read(selectedFile);
			imgContainer.setIcon(new ImageIcon(img));
		}
	}

	public void updateRvalue(int r) { // jako argument podajê wartoœæ R
		int w = img.getWidth();
		int h = img.getHeight();
		int pixel;
		int rgb;
		Color color;
		int g;
		int b;

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				pixel = img.getRGB(j, i);
				color = new Color(pixel, true); // tworzê kolor pixela
				g = color.getGreen();// zapamiêtujê g i b - bo tych wartoœci NIE
										// ZMIENIAM
				b = color.getBlue();
				rgb = ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
				img.setRGB(j, i, rgb);

			}
		}
	}

	public void updateGvalue(int g) {
		int w = img.getWidth();
		int h = img.getHeight();
		int pixel;
		int rgb;
		Color color;
		int r;
		int b;

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				pixel = img.getRGB(j, i);
				color = new Color(pixel, true);
				r = color.getRed();
				b = color.getBlue();
				rgb = ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
				img.setRGB(j, i, rgb);

			}
		}
	}

	public void updateBvalue(int b) {
		int w = img.getWidth();
		int h = img.getHeight();
		int pixel;
		int rgb;
		Color color;
		int r;
		int g;

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				pixel = img.getRGB(j, i);
				color = new Color(pixel, true);
				r = color.getRed();
				g = color.getGreen();
				rgb = ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
				img.setRGB(j, i, rgb);

			}
		}
	}
}