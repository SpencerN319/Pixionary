import java.awt.image.BufferedImage;
//breaks the image into PIXELS
public class imgbreaker{
    
   BufferedImage img;
   int[][] pixels;
    
    public imgbreaker (BufferedImage i)
    {
	img = i; 
    }

    public void breakImage()
    {   //get some dimensions
	int height = img.getHeight();
	int width = img.getWidth();
	//maybe not declare this here
	pixels = new int[height][width];

	for (int y = 0; y < height; y++)
	    {
		for (int x = 0; x < width; x++)
		    {
		        pixels[y][x] = img.getRGB(x,y);
							   
		    }
	    }
	
    }


    
    
}
