import java.awt.image.BufferedImage;
import java.util.Random;
//breaks the image into PIXELS and also transfers them
public class imgbreak{
    
   BufferedImage img;
   int[][] pixels;
    boolean[][] used;
    int totalpixels;
    int sent;
    int height;
    int width; 
    Random rand = new Random();
    boolean guessed = false;
  
    
    public imgbreak (BufferedImage i)
    {
	img = i; 
    }

    public void breakImage()
    {   //get some dimensions
	 height = img.getHeight();
	 width = img.getWidth();
	 
	
	pixels = new int[height][width];
	used = new boolean[height][width];
	totalpixels = width * height;
	
	for (int y = 0; y < height; y++)
	    
	    {
		for (int x = 0; x < width; x++)
		    {
		        pixels[y][x] = img.getRGB(x,y);
							   
		    }
	    }
	
    }
    
    public void sendPixels()
    {   
	while (sent < totalpixels && guessed == false)
	    {
		//declared to make compiler happy
		int pixx = 0;
		int pixy = 0;
		boolean found = false;
		while (found == false)
		    {
			pixx = rand.nextInt(width);
			pixy = rand.nextInt(height);
			if (used[pixy][pixx] == false)
			    found = true;
		    }
		used [pixy][pixx] = true;
		/* pseudo code
		   socket.sendInts (pixx, pixy, pixels[pixx][pixy]);
		*/
		sent++;
	    }
		
    }
    public void setGuessed()
    {
	guessed = true;
    }
        
}
