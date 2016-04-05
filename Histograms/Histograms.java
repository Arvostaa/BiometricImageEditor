package Histograms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Histograms extends JFrame {

    private Chart graph;

    public Histograms(BufferedImage image) {
        super("Histogram:");
        graph = new Chart(image);
        add(graph, BorderLayout.CENTER);
        setVisible(true);
        pack();

    }
}
