package Histograms;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PaintPicture extends JPanel {
   
    Dimension dimFrame;
    File picture;
   
    public PaintPicture(Dimension d, File pic)
    {
        dimFrame = d;
        picture = pic;
    }
      
    public void paintComponent(Graphics g)
    {
        BufferedImage inputPicture = null;
        try {
            super.paintComponent(g);
            inputPicture = ImageIO.read(picture);
        } catch (IOException ex) {
            Logger.getLogger(PaintPicture.class.getName()).log(Level.SEVERE, null, ex);
        }
        g.drawImage(inputPicture, 0, 0,dimFrame.width/2, dimFrame.height/2, this);
                             
    }
   
}

