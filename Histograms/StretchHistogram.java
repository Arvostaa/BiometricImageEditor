package Histograms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import convertRGB.Pane;
import convertRGB.ImagePanel;

public class StretchHistogram extends JFrame {

    private JPanel menuPanel;
    private JPanel helpPanel;
    private JTextField entera;
    private JTextField enterb;
    private JButton enter;
 
    public StretchHistogram(LUTimage lut, ImagePanel image) {
        super("Stretch values:");
      
        menuPanel = new JPanel();
        helpPanel = new JPanel(new GridLayout(2, 1));
        menuPanel.add(new JLabel("min:"));
        entera = new JTextField();
        entera.setPreferredSize(new Dimension(60, 20));
        menuPanel.add(entera);
        menuPanel.add(new JLabel("max:"));
        enterb = new JTextField();
        enterb.setPreferredSize(new Dimension(60, 20));
        enter = new JButton("Stretch histogram");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tryParseInt((entera.getText()).replace(',', '.')) && tryParseInt((enterb.getText()).replace(',', '.'))) {
                    if (!entera.getText().equals("") || !enterb.getText().equals("")) {
                        int a = Integer.parseInt((entera.getText()).replace(',', '.'));
                        int b = Integer.parseInt((enterb.getText()).replace(',', '.'));
                        if (a > 0 && b > 0 && a < 256 && b < 256) {
                            lut.stretchingHistogram(a, b);
                            lut.repaintImage(image);
                            image.repaint();
                            dispose();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(image, "Wartość pól nie może być pusta i musi być większa od 0");
                }
            }
        });
        menuPanel.add(enterb);
        menuPanel.add(enter);
        helpPanel.add(menuPanel);
        add(helpPanel, BorderLayout.NORTH);
        setVisible(true);
        pack();
    }

    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
