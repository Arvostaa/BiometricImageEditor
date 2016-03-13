package Histograms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderPanel extends JPanel {
	private JSlider slider;
	private JPanel panelSlider;
	private int count = 0;
	public int value = 255;
	
	private ChangeListener changeListener = new ChangeListener() {
		public void stateChanged(ChangeEvent ce) {
			JSlider slider = (JSlider) ce.getSource();
			if (!slider.getValueIsAdjusting()){
				System.out.println(slider.getValue());
			value = slider.getValue();
			
			
			}
		}
	};

	SliderPanel(int min, int max, int defaultValue) {

		System.out.println("SIEMAJESTEMJO£");
		slider = new JSlider(JSlider.HORIZONTAL, min, max, defaultValue);
		panelSlider = new JPanel();
		slider.setMajorTickSpacing(50); // co ile odstêp
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(changeListener);

		panelSlider.setBackground(new Color(235, 77, 68));
		panelSlider.add(slider);

		setLayout(new BorderLayout(4, 4));
		add("Center", panelSlider);
		setVisible(true);

	}
	/*
	 * public static void main(String... args) { SwingUtilities.invokeLater(new
	 * Runnable() { public void run() { new SliderChangeEffect(); } }); }
	 */
}