package Histograms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import convertRGB.RGBimage;

public class GenHistogram extends JFrame {

	public GenHistogram(RGBimage image) throws IOException {
		super("Generating Histogram");
		JPanel histograms = new JPanel();
		histograms.setLayout(new GridLayout(1, 3, 1, 3));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 900);
		setVisible(true);

		System.out.println("HIST DIMESTIONS " + image.d);

		GetHistogram rHistogram = new GetHistogram(image.d, "red", image.selectedFile, image.d.height);
		GetHistogram gHistogram = new GetHistogram(image.d, "green", image.selectedFile, image.d.height);
		GetHistogram bHistogram = new GetHistogram(image.d, "blue", image.selectedFile, image.d.height);
		GetHistogram rgbHistogram = new GetHistogram(image.d, "all", image.selectedFile, image.d.height);
		histograms.add(rHistogram);
		histograms.add(gHistogram);
		histograms.add(bHistogram);
		histograms.add(rgbHistogram);

		setLayout(new BorderLayout(4, 4));
		add("Center", histograms);

	}

}