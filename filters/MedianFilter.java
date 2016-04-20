package filters;
import java.awt.Color;
import java.awt.image.*;

public class MedianFilter{
    
    private int size; //size of the square filter
    
    public MedianFilter(int s) {
        if ((s%2 == 0)||(s < 3))
        {   //check if the filter size is an odd number > = 3
            System.out.println(s+"is not a valid filter size.");
            System.out.println("Filter size is now set to 3");
            s = 3;
        }
        size = s;
    }
    
    public int getFilterSize() {
        return size;
    }
    
    //sort the array, and return the median
    public int median(int[] a) { // a = np. 3x3
        int temp;
        int asize = a.length;
        //sort the array in increasing order
        for (int i = 0; i < asize ; i++)
            for (int j = i+1; j < asize; j++)
                if (a[i] > a[j]) {
                    temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
        //if it's odd
        if (asize%2 == 1)
            return a[asize/2];
        else
            return ((a[asize/2]+a[asize/2 - 1])/2); //zwróæ œrodkowy
    }
    
    public int[] getArray(BufferedImage image, int x, int y){
        int[] n; //store the pixel values of position(x, y) and its neighbors
        int h = image.getHeight();
        int w = image.getWidth();
        int xmin, xmax, ymin, ymax; //the limits of the part of the image on which the filter operate on
        xmin = x - size/2;
        xmax = x + size/2;
        ymin = y - size/2;
        ymax = y + size/2;
        
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
        int nsize = (xmax-xmin+1)*(ymax-ymin+1);
        n = new int[nsize];
        int k = 0;
        for (int i = xmin; i <= xmax; i++)
            for (int j = ymin; j <= ymax; j++){
                n[k] = image.getRGB(i, j); //get pixel value
                k++;
            }
        return n;
    }
    
    public BufferedImage filter(BufferedImage srcImage) {
        int height = srcImage.getHeight();
        int width = srcImage.getWidth();
        Color c;
        BufferedImage tempImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        
        int[] a; //the array that gets the pixel value at (x, y) and its neightbors
        
        for (int k = 0; k < height; k++){
            for (int j = 0; j < width; j++) {
                a = getArray(srcImage, j, k); // mam kwadracik
                int[] red, green, blue;
                red = new int[a.length];
                green = new int[a.length];
                blue = new int[a.length];
                //get the red,green,blue value from the pixel
                for (int i = 0; i < a.length; i++) {
                	//red[i] = Pixel.getRed( a[i] );
                	c = new Color(a[i]);
                    red[i] =c.getRed();
                    green[i] = c.getGreen();
                    blue[i] = c.getBlue();
                }
                //find the median for each color
                int R = median(red);
                int G = median(green);
                int B = median(blue);
                c = new Color(R,G,B);
                //set the new pixel value using the median just found
               // int spixel = Pixel.createRGB(R, G, B);
                int spixel = c.getRGB();
               tempImage.setRGB(j, k, spixel);
            }
        }
        return tempImage;
    }
    
}