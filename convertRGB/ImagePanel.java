package convertRGB;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	Image displayImage, startImage;
	
	String path;
	Graphics2D big;
	LookupTable lookupTable;
	
	public BufferedImage img; //bi
	public String sname;
//	public JLabel imgContainer;
	public File selectedFile;
	public String selectedPath;
	public Dimension d;

	ImagePanel(){	
		this.setSize(550,550);	
	    createBufferedImage();
	
	}
	
	
	public void imageLoad() throws IOException {

		JFileChooser fc = new JFileChooser("C:\\Users\\Anna\\Documents\\sem666\\biometria\\BiometriaObrazki\\histogramy");
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
			String sname = selectedFile.getAbsolutePath();
			img = ImageIO.read(selectedFile); 
					
			int w = img.getWidth();
			int h = img.getHeight();
			
			double scale = 550/(double)h;
			
			BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			AffineTransform at = new AffineTransform();
			at.scale(scale, scale);
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			after = scaleOp.filter(img, after);
			
			loadImage(after);
		}
	}
	
	public void createBufferedImage() {
		img = new BufferedImage(550,550, BufferedImage.TYPE_INT_ARGB);
		
	}

	public void loadImage(BufferedImage image) {
	
		int w = image.getWidth();
		int h = image.getHeight();
		
		img = image;
		
		big = image.createGraphics();
		big.drawImage(image, 0, 0, this);
		
	//	startImage = image;
	//	displayImage = image;
		repaint();
	}


	public void update(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		paintComponent(g);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(img, 0, 0, this);
	}
}
