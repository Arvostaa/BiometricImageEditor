package convertRGB;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RGBvaluesPane extends JPanel{
	
	public JPanel RGBvaluesContainer;
	public JTextField Rvalue;
	public JTextField Gvalue;
	public JTextField Bvalue;

	public RGBvaluesPane() {
		RGBvaluesContainer = new JPanel();
		RGBvaluesContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		Rvalue = new JTextField(3);
		Gvalue = new JTextField(3);
		Bvalue = new JTextField(3);
		RGBvaluesContainer.add(Rvalue);
		RGBvaluesContainer.add(Gvalue);
		RGBvaluesContainer.add(Bvalue);
		/*
		 * currentImageLabel = new JLabel(); currentRLabel = new JLabel();
		 * currentGLabel = new JLabel(); currentBLabel = new JLabel();
		 * 
		 * try{ img = ImageIO.read(this.getClass().getResource("tifimg.tif"));
		 * imgR = ImageIO.read(this.getClass().getResource("tifimg.tif")); imgG
		 * = ImageIO.read(this.getClass().getResource("tifimg.tif")); imgB =
		 * ImageIO.read(this.getClass().getResource("tifimg.tif"));
		 * 
		 * currentImageLabel.setIcon(new ImageIcon(img));
		 * currentRLabel.setIcon(new ImageIcon(imgR)); currentGLabel.setIcon(new
		 * ImageIcon(imgG)); currentBLabel.setIcon(new ImageIcon(imgB)); }
		 * 
		 * catch (IOException ex) { ex.printStackTrace(); }
		 * 
		 * //Img
		 */

	}
}
