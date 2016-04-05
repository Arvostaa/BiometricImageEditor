package convertRGB;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
		RGBvaluesContainer.setLayout(new BoxLayout(RGBvaluesContainer, BoxLayout.LINE_AXIS));
		//RGBvaluesContainer.setLayout(new FlowLayout());
		RGBvaluesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		RGBvaluesContainer.setBackground(new Color(23,23,23));
	//	RGBvaluesContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		Rvalue = new JTextField(3);
		Gvalue = new JTextField(3);
		Bvalue = new JTextField(3);
		
		Rvalue.setMaximumSize(Rvalue.getPreferredSize());
        Rvalue.setMinimumSize(Rvalue.getPreferredSize());
        Gvalue.setMaximumSize(Gvalue.getPreferredSize());
        Gvalue.setMinimumSize(Gvalue.getPreferredSize());
        Bvalue.setMaximumSize(Bvalue.getPreferredSize());
        Bvalue.setMinimumSize(Bvalue.getPreferredSize());

		RGBvaluesContainer.add(Rvalue);
		RGBvaluesContainer.add(Gvalue);
	
		RGBvaluesContainer.add(Bvalue);
		
		//setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setLayout(new FlowLayout());
		add(RGBvaluesContainer);

	}
}
