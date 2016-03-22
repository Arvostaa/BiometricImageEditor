
package convertRGB;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


import Histograms.HistogramOperations;


public class Pane extends JPanel {

	private RGBimage imgR;
	private RGBimage imgG;
	private RGBimage imgB;
	private RGBimage imgMain;

	private RGBvaluesPane currentRGBPane;
	private RGBvaluesPane newRGBPane;

	private JButton loadFile = new JButton("Load image");
	//private JButton colorLevels = new JButton("Color levels");
	private JButton colorLevels = new JButton("Color levels");
	private JButton exportImages = new JButton("Export imgs");

	private int tempRed;
	private int tempGreen;
	private int tempBlue;
	private int tempAlpha;
	private Color tempColor;
	private int tempRGB;
	private JPanel tempPane;

	public Pane() {

		imgMain = new RGBimage();
		imgR = new RGBimage();
		imgG = new RGBimage();
		imgB = new RGBimage();

		System.out.println("MAIN: w = " + imgMain.imgContainer.getWidth());

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		currentRGBPane = new RGBvaluesPane();
		newRGBPane = new RGBvaluesPane();

		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(imgR.imgContainer, gbc);

		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(imgG.imgContainer, gbc);

		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 2;
		gbc.gridy = 0;
		add(imgB.imgContainer, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.ipady = 20;
		gbc.weightx = 0.0;
		// gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(newRGBPane.RGBvaluesContainer, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.ipady = 60;
		gbc.weightx = 0.0;
		// gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(imgMain.imgContainer, gbc);

		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.ipady = 20;
		gbc.weightx = 1.0;
		// gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(currentRGBPane.RGBvaluesContainer, gbc);

		// gbc.fill = GridBagConstraints.EAST;

		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(loadFile, gbc);

		gbc.weightx = 1.0;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(colorLevels, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 2;
		gbc.gridy = 4;
		add(exportImages, gbc);

		imgMain.imgContainer.setMinimumSize(new Dimension(600, 400));
		imgMain.imgContainer.setPreferredSize(new Dimension(600, 400));
		imgMain.imgContainer.setMaximumSize(new Dimension(600, 400));

		imgR.imgContainer.setMinimumSize(new Dimension(300, 200));
		imgR.imgContainer.setPreferredSize(new Dimension(300, 200));
		imgR.imgContainer.setMaximumSize(new Dimension(300, 200));

		imgG.imgContainer.setMinimumSize(new Dimension(300, 200));
		imgG.imgContainer.setPreferredSize(new Dimension(300, 200));
		imgG.imgContainer.setMaximumSize(new Dimension(300, 200));

		imgB.imgContainer.setMinimumSize(new Dimension(300, 200));
		imgB.imgContainer.setPreferredSize(new Dimension(300, 200));
		imgB.imgContainer.setMaximumSize(new Dimension(300, 200));
		
		 exportImages.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		         
		        	  JFrame frame = new JFrame ("Save image");		
		        	  frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            frame.getContentPane().add (new SaveImage(imgMain)); 
			           
			            frame.pack();
			            frame.setVisible (true);

		        }
		    });
		 
		 colorLevels.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		         
		        	  JFrame frame = new JFrame ("Color levels");	
		        	  frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			            try {
							frame.getContentPane().add (new HistogramOperations(imgMain));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
			           // frame.setSize(900, 600);
			            frame.pack();
			            frame.setVisible (true);

		        }
		    });
		 
		loadFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					imgMain.setNewMainImage();
					imgR.copyCreatedNewMainImage(imgMain.selectedFile);
					imgG.copyCreatedNewMainImage(imgMain.selectedFile);
					imgB.copyCreatedNewMainImage(imgMain.selectedFile);

					imgMain.imgContainer.addMouseMotionListener(new MouseAdapter() {
						@Override
						public void mouseMoved(MouseEvent e) {
							int packedInt = imgMain.img.getRGB(e.getX(), e.getY());
							Color color = new Color(packedInt, true);
							currentRGBPane.RGBvaluesContainer.setBackground(color); // FIELDS
							currentRGBPane.Rvalue.setText(Integer.toString(color.getRed()));
							currentRGBPane.Gvalue.setText(Integer.toString(color.getGreen()));
							currentRGBPane.Bvalue.setText(Integer.toString(color.getBlue()));
							tempAlpha = color.getAlpha();
						}

						@Override
						public void mouseDragged(MouseEvent e) {

						}
					});

					MouseListener mouse = new MouseListener() {
						@Override
						public void mouseReleased(MouseEvent e) {
							System.out.println("Released!");

						}

						@Override
						public void mousePressed(MouseEvent e) {
							System.out.println("Pressed!");
						}

						@Override
						public void mouseExited(MouseEvent e) {
							System.out.println("Exited!");

						}

						@Override
						public void mouseEntered(MouseEvent evt) {

							// System.out.println("Entered! " +x + " " + y +",
							// evt.getx,y: " + evt.getX() + ", " + evt.getY());
							System.out.println("Mouse:  " + MouseInfo.getPointerInfo().getLocation().x + " "
									+ MouseInfo.getPointerInfo().getLocation().y);
							System.out.println(e);
							// int abs = e.get
							int packedInt = imgMain.img.getRGB(evt.getX(), evt.getY());
							Color color = new Color(packedInt, true);
							currentRGBPane.setBackground(color);

							currentRGBPane.Rvalue.setText(Integer.toString(color.getRed()));
							currentRGBPane.Gvalue.setText(Integer.toString(color.getGreen()));
							currentRGBPane.Bvalue.setText(Integer.toString(color.getBlue()));
							tempAlpha = color.getAlpha();
						}

						@Override
						public void mouseClicked(MouseEvent e) {

							int packedInt = imgMain.img.getRGB(e.getX(), e.getY());
							Color color = new Color(packedInt, true);

							int r = color.getRed();
							int g = color.getGreen();
							int b = color.getBlue();

							System.out.println("Clicked!");
							System.out.println(tempRed + " " + tempGreen + " " + tempBlue);
							int rgb = ((tempRed & 0x0ff) << 16) | ((tempGreen & 0x0ff) << 8) | (tempBlue & 0x0ff);
							imgMain.img.setRGB(e.getX(), e.getY(), rgb);

							imgR.updateRvalue(tempRed);
							imgG.updateGvalue(tempGreen);
							imgB.updateBvalue(tempBlue);
							repaint();
						}
					};

					imgMain.imgContainer.addMouseListener(mouse);

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});

		newRGBPane.Rvalue.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				System.out.println();
				if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
					if (newRGBPane.Rvalue.getText() != null) {
						tempRed = Integer.parseInt(newRGBPane.Rvalue.getText());
					}
				}
			}

			public void keyReleased(KeyEvent ke) {
				if (newRGBPane.Rvalue.getText() != null) {
					System.out.println("red: " + newRGBPane.Rvalue.getText());
					tempRed = Integer.parseInt(newRGBPane.Rvalue.getText());
				} else
					tempRed = 0;
				tempRGB = ((tempRed & 0x0ff) << 16) | ((tempGreen & 0x0ff) << 8) | (tempBlue & 0x0ff);
				tempColor = new Color(tempRGB, true);
				newRGBPane.setBackground(tempColor);
			}
		});
		newRGBPane.Gvalue.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
					if (newRGBPane.Gvalue.getText() != null) {
						System.out.println("green: " + newRGBPane.Gvalue.getText());
						tempGreen = Integer.parseInt(newRGBPane.Gvalue.getText());
					}
				}
			}

			public void keyReleased(KeyEvent ke) {
				if (newRGBPane.Gvalue.getText() != null) {
					System.out.println("green: " + newRGBPane.Gvalue.getText());
					tempGreen = Integer.parseInt(newRGBPane.Gvalue.getText());
				} else
					tempGreen = 0;
				tempRGB = ((tempRed & 0x0ff) << 16) | ((tempGreen & 0x0ff) << 8) | (tempBlue & 0x0ff);
				tempColor = new Color(tempRGB, true);
			}
		});
		newRGBPane.Bvalue.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (!(ke.getKeyChar() == 27 || ke.getKeyChar() == 65535)) {
					if (newRGBPane.Bvalue.getText() != null) {

						tempBlue = Integer.parseInt(newRGBPane.Bvalue.getText());
						System.out.println("blue: " + newRGBPane.Bvalue.getText());
					}
				}
			}

			public void keyReleased(KeyEvent ke) {
				if (newRGBPane.Bvalue.getText() != null) {
					System.out.println("blue: " + newRGBPane.Bvalue.getText());
					tempBlue = Integer.parseInt(newRGBPane.Bvalue.getText());
				} else
					tempBlue = 0;
				tempRGB = ((tempRed & 0x0ff) << 16) | ((tempGreen & 0x0ff) << 8) | (tempBlue & 0x0ff);
				tempColor = new Color(tempRGB, true);
				;

			}
		});

	}
}