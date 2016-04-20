package filters;
import java.awt.Color;
import java.awt.image.*;

public class MedianFilter{
    
    private int squareSize; //size of the square filter
    
    public MedianFilter(int squaresize) {
        if ((squaresize%2 == 0)||(squaresize < 3))
        {            
            squaresize = 3;
        }
        squareSize = squaresize;
    }
    
    public int getFilterSize() {
        return squareSize;
    }
    
    //sort the array, return the median
    public int median(int[] pixelsSquare) { // a = np. 3x3
        int temp;
        int pixelsCount = pixelsSquare.length;
        //sort the array in increasing order
        for (int i = 0; i < pixelsCount ; i++)
            for (int j = i+1; j < pixelsCount; j++)
                if (pixelsSquare[i] > pixelsSquare[j]) {
                    temp = pixelsSquare[i];
                    pixelsSquare[i] = pixelsSquare[j];
                    pixelsSquare[j] = temp;
                }
        
        if (pixelsCount%2 == 1)
            return pixelsSquare[pixelsCount/2];
        else
            return ((pixelsSquare[pixelsCount/2]+pixelsSquare[pixelsCount/2 - 1])/2); //get pixel in the middle
    }
    
    public int[] getSquareOfPixels(BufferedImage image, int x, int y){
        int[] pixelsSquare; //store the pixel values of position(x, y) and its neighbors
        int h = image.getHeight();
        int w = image.getWidth();
        int xmin, xmax, ymin, ymax; //the limits of the part of the image on which the filter operate on
        xmin = x - squareSize/2;
        xmax = x + squareSize/2;
        ymin = y - squareSize/2;
        ymax = y + squareSize/2;
        
        //special edge cases
        if (xmin < 0)
            xmin = 0;
        if (xmax > (w - 1))
            xmax = w - 1;
        if (ymin < 0)
            ymin = 0;
        if (ymax > (h - 1))
            ymax = h - 1;
        //the actual number of pixels to be considered
        int pixelsAmount = (xmax-xmin+1)*(ymax-ymin+1);
        pixelsSquare = new int[pixelsAmount];
        int k = 0;
        for (int i = xmin; i <= xmax; i++)
            for (int j = ymin; j <= ymax; j++){
                pixelsSquare[k] = image.getRGB(i, j); //get pixel value
                k++;
            }
        return pixelsSquare;
    }
    
    public BufferedImage medianFilter(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        Color c;
        BufferedImage tempImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      
        int[] squareOfPixels; //the array that gets the pixel value at (x, y) and its neightbors
        
        for (int k = 0; k < height; k++){
            for (int j = 0; j < width; j++) {
                squareOfPixels = getSquareOfPixels(image, j, k); // mam kwadracik
                int[] red, green, blue;
                red = new int[squareOfPixels.length];
                green = new int[squareOfPixels.length];
                blue = new int[squareOfPixels.length];
              
                for (int i = 0; i < squareOfPixels.length; i++) {
       
                	c = new Color(squareOfPixels[i]);
                    red[i] =c.getRed();
                    green[i] = c.getGreen();
                    blue[i] = c.getBlue();
                }

                int R = median(red);
                int G = median(green);
                int B = median(blue);
                c = new Color(R,G,B);
          
                int spixel = c.getRGB();
               tempImage.setRGB(j, k, spixel);
            }
        }
        return tempImage;
    }
    
}