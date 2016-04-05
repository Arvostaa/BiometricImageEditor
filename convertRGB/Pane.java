
package convertRGB;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Histograms.Histograms;
import Histograms.LUTimage;
import Histograms.StretchHistogram;
import binarization.NiblackFrame;
import binarization.ThresholdFrame;

public class Pane extends JPanel {

	public JMenuBar menu;

	private RGBvaluesPane currentRGBPane;
	private RGBvaluesPane newRGBPane;

	private JButton brighten = new JButton("Brighten");
	private JButton darken = new JButton("Darken");

	private int tempRed;
	private int tempGreen;
	private int tempBlue;
	private int tempAlpha;
	private Color tempColor;
	private int tempRGB;
	private JPanel tempPane;
	private LUTimage lut;

	public ImagePanel imagePanel;

	public Pane() {
		menu = new JMenuBar();
		lut = new LUTimage(this);

		// FILE//
		JMenu fileMenu = new JMenu("File");
		JMenuItem save = new JMenuItem("Export image");
		JMenuItem loadImage = new JMenuItem("Load image");
		fileMenu.add(loadImage);
		fileMenu.add(save);

		// HISTOGRAMS //
		JMenu levels = new JMenu("Color levels");
		JMenuItem histogramsFrame = new JMenuItem("Histograms");
		JMenuItem equalize = new JMenuItem("Equalize");
		JMenuItem stretch = new JMenuItem("Stretch");
		levels.add(histogramsFrame);
		levels.add(equalize);
		levels.add(stretch);
		
		//BINARIZATION//
		
		JMenu binarization = new JMenu("Binarization");
		JMenuItem threshold = new JMenuItem("Threshold");
		JMenuItem otsu = new JMenuItem("Otsu");
		JMenuItem niblack = new JMenuItem("Niblack");
		binarization.add(threshold);
		binarization.add(otsu);
		binarization.add(niblack);

		// MENU//
		menu.add(fileMenu);
		menu.add(levels);
		menu.add(binarization);

		JPanel buttons = new JPanel();
		currentRGBPane = new RGBvaluesPane();
		imagePanel = new ImagePanel(); // MAIN IMAGE

		newRGBPane = new RGBvaluesPane();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
	  
		buttons.add(brighten);
		buttons.add(darken);

		currentRGBPane.setLayout(new BoxLayout(currentRGBPane, BoxLayout.LINE_AXIS));
		newRGBPane.setLayout(new BoxLayout(newRGBPane, BoxLayout.LINE_AXIS));

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// add(menu);
		add(buttons);
		add(newRGBPane);
		add(imagePanel);
		add(currentRGBPane);

		// load image...
		loadImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					imagePanel.imageLoad(); // LOAD RESIZED IMAGE

					imagePanel.addMouseMotionListener(new MouseAdapter() {
						@Override
						public void mouseMoved(MouseEvent e) {
							int packedInt = imagePanel.img.getRGB(e.getX(), e.getY());
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
							int packedInt = imagePanel.img.getRGB(evt.getX(), evt.getY());
							Color color = new Color(packedInt, true);
							currentRGBPane.setBackground(color);

							currentRGBPane.Rvalue.setText(Integer.toString(color.getRed()));
							currentRGBPane.Gvalue.setText(Integer.toString(color.getGreen()));
							currentRGBPane.Bvalue.setText(Integer.toString(color.getBlue()));
							tempAlpha = color.getAlpha();
						}

						@Override
						public void mouseClicked(MouseEvent e) {

							int packedInt = imagePanel.img.getRGB(e.getX(), e.getY());
							Color color = new Color(packedInt, true);

							int r = color.getRed();
							int g = color.getGreen();
							int b = color.getBlue();

							System.out.println("Clicked!");
							System.out.println(tempRed + " " + tempGreen + " " + tempBlue);
							System.out.println(r + " " + g + " " + b);
							int rgb = ((tempRed & 0x0ff) << 16) | ((tempGreen & 0x0ff) << 8) | (tempBlue & 0x0ff);
							imagePanel.img.setRGB(e.getX(), e.getY(), rgb);
							imagePanel.img.flush();
							imagePanel.repaint();
							///////////
							int packedInt2 = imagePanel.img.getRGB(e.getX(), e.getY());
							Color color2 = new Color(packedInt, true);

							int r2 = color.getRed();
							int g2 = color.getGreen();
							int b2 = color.getBlue();

							System.out.println("after:   " + r2 + " " + g2 + " " + b2);

						}
					};

					imagePanel.addMouseListener(mouse);

				} catch (IOException ex) {
					ex.printStackTrace();
				}
				// pack();
			}

		});
		/*
		 * exportImages.addActionListener(new ActionListener() { // change //
		 * exportImage
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * 
		 * JFrame frame = new JFrame("Save image");
		 * frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //
		 * frame.getContentPane().add (new SaveImage(imgMain)); frame.pack();
		 * frame.setVisible(true); } });
		 */

		histogramsFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JFrame frame = new JFrame("Color levels");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				frame.getContentPane().add(new Histograms(imagePanel.img));
				frame.setSize(800, 600);

				frame.pack();
				frame.setVisible(true);

			}
		});
		
		  stretch.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {       
	                    new StretchHistogram(lut, imagePanel);	               
	            }
	        });
	       
	       equalize.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	
	                    lut.histogramEqualization();
	                    lut.repaintImage(imagePanel);
	                    imagePanel.repaint();
	              
	            }
	        });
	   //BINARIZATION METHODS//
	       
	       niblack.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {       
	                    new NiblackFrame(lut, imagePanel);	               
	            }
	        });
	       
	       threshold.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {       
	                    new ThresholdFrame(lut, imagePanel);	               
	            }
	        });
	       
	       otsu.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                
	                    lut.otsu();
	                    imagePanel.repaint();
	               
	            }
	        });
		
		brighten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				lut.expLUT(0.7);
				lut.repaintImage(imagePanel);
				imagePanel.repaint();

			}
		});
		
		darken.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				lut.expLUT(1 / 0.7);
				
				lut.repaintImage(imagePanel);
				imagePanel.repaint();
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