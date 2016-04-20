package filters;

import java.awt.Graphics2D;
/*  
 **************************************************************************  
 *  
 *   Sobel operator  
 *  
 **************************************************************************  
 */   
import java.awt.event.*;   
import java.awt.image.BufferedImage;   
import java.io.*;   
   
import javax.imageio.ImageIO;   
import javax.swing.ImageIcon;   
import javax.swing.JFrame;   
import javax.swing.JLabel;   
   
public class Sobel {   
   
  public static void main(String[] args) throws IOException{   
         
      int     i, j;   
      double  Gx[][], Gy[][], G[][];   
      FileInputStream inFile = new FileInputStream("aragorn.jpg");   
        BufferedImage inImg = ImageIO.read(inFile);   
        int width = inImg.getWidth();   
        int height = inImg.getHeight();   
        int[] pixels = new int[width * height];   
        int[][] output = new int[width][height];   
        Graphics2D big = inImg.createGraphics();
		//big.drawImage(inImg, 0, 0, this);
      //  inImg.getRaster().getPixels(0,0,width,height,pixels);   
       
        int counter = 0;   
           
        for(i = 0 ; i < width ; i++ )   
        {   
            for(j = 0 ; j < height ; j++ )   
            {   
                //System.out.println(counter);   
                   
                output[i][j] = pixels[counter];   
                counter = counter + 1;   
            }              
        }   
       
      
    Gx = new double[width][height];   
    Gy = new double[width][height];   
    G  = new double[width][height];   
   
    for (i=0; i<width; i++) {   
      for (j=0; j<height; j++) {   
        if (i==0 || i==width-1 || j==0 || j==height-1)   
          Gx[i][j] = Gy[i][j] = G[i][j] = 0; // Image boundary cleared   
        else{   
          Gx[i][j] = output[i+1][j-1] + 2*output[i+1][j] + output[i+1][j+1] -   
          output[i-1][j-1] - 2*output[i-1][j] - output[i-1][j+1];   
          Gy[i][j] = output[i-1][j+1] + 2*output[i][j+1] + output[i+1][j+1] -   
          output[i-1][j-1] - 2*output[i][j-1] - output[i+1][j-1];   
          G[i][j]  = Math.abs(Gx[i][j]) + Math.abs(Gy[i][j]);   
        }   
      }   
    }   
    counter = 0;   
    for(int ii = 0 ; ii < width ; ii++ )   
    {   
        for(int jj = 0 ; jj < height ; jj++ )   
        {   
            //System.out.println(counter);   
               
            pixels[counter] = (int) G[ii][jj];   
            counter = counter + 1;   
        }              
    }   
       
    BufferedImage outImg = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);   
    outImg.getRaster().setPixels(0,0,width,height,pixels);   
    FileOutputStream outFile = new FileOutputStream("copyTop.hpg");   
    ImageIO.write(outImg,"JPG",outFile);   
       
    JFrame TheFrame = new JFrame("Ÿvš³¥GŸe " + width + " °ª " + height);   
   
    JLabel TheLabel = new JLabel(new ImageIcon(outImg));   
    TheFrame.getContentPane().add(TheLabel);   
     
    
      TheFrame.setSize(600, 600);   
       
      TheFrame.addWindowListener(new WindowAdapter() {   
          public void windowClosing(WindowEvent e) {   
            System.exit(0);                 
          }   
        });           
      TheFrame.setVisible(true);   
  }   
   
}   
   