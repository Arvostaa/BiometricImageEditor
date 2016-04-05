package Histograms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Chart extends JPanel {

    private BufferedImage image;
    private double[] xValues;

    private JPanel chartPanel;

    public Chart(BufferedImage img) {
        super();
        xValues = new double[256];
        image = img;
        createHistogram();
        setVisible(true);
    }

    public void createHistogram() {
        setxValues(true, false, false);
        JPanel chartR = createPanelChart("R");
        setxValues(false, true, false);
        JPanel chartG = createPanelChart("G");
        setxValues(false, false, true);
        JPanel graph3 = createPanelChart("B");
        setxValues(true, true, true);
        JPanel chartRGB = createPanelChart("RGB");
        chartR.setPreferredSize(new Dimension(400, 300));
        chartG.setPreferredSize(new Dimension(400, 300));
        graph3.setPreferredSize(new Dimension(400, 300));
        chartRGB.setPreferredSize(new Dimension(400, 300));
        chartPanel = new JPanel(new GridLayout(2, 2));
        chartPanel.add(chartR);
        chartPanel.add(chartG);
        chartPanel.add(graph3);
        chartPanel.add(chartRGB);
        add(chartPanel);
    }

    private void setxValues(boolean R, boolean G, boolean B) {
        for(int i = 0 ; i < xValues.length; i ++){
            xValues[i] = 0;
        }
       
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color c = new Color(image.getRGB(i, j));
                if (R && B && G) {
                    xValues[Math.round(c.getRed() + c.getGreen() + c.getBlue()) / 3]++;
                }
                if (R) {
                    xValues[Math.round(c.getRed())]++;
                }
                if (G) {
                    xValues[Math.round(c.getGreen())]++;
                }
                if (B) {
                    xValues[Math.round(c.getBlue())]++;
                }
            }
        }
    }

    private IntervalXYDataset createDataset() {
        XYSeries chartData = new XYSeries("",false,false);
        for (int i = 0; i < xValues.length; i++) {
            chartData.add(i, xValues[i]);
        }
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        dataset.addSeries(chartData);
        return dataset;
    }

    private JFreeChart createChart(IntervalXYDataset dataset, String title) {
        JFreeChart chart;
        chart = ChartFactory.createHistogram(
                title,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        if (title.equals("R")) {
            renderer.setSeriesPaint(0, Color.red);
        }
        if (title.equals("G")) {
            renderer.setSeriesPaint(0, Color.green);
        }
        if (title.equals("B")) {
            renderer.setSeriesPaint(0, Color.blue);
        }
        if (title.equals("RGB")) {
            renderer.setSeriesPaint(0, Color.black);
        }
        renderer.setDrawBarOutline(false);
        return chart;
    }

    public JPanel createPanelChart(String title) {
        JFreeChart chart = createChart(createDataset(), title);
        return new ChartPanel(chart);
    }

    public JPanel getGraphGridPanel() {
        return chartPanel;
    }
}
