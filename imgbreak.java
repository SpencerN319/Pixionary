import java.awt.image.BufferedImage;
//breaks the image into 100 chunks of equal size, except for maybe the ones on the bottom row and right column. 
public class imgbreak{
    
   BufferedImage img;
    BufferedImage[][] chunks = new BufferedImage[10][10];
    
    public imgbreak (BufferedImage i)
    {
	img = i; 
    }

    public void breakImage()
    {   //get some dimensions
	int height = img.getHeight();
	int width = img.getWidth();
	//maybe not declare this here
	int chunkheight = height / 10;
	int chunkwidth = width / 10;

	for (int y = 0; y < 10; y++)
	    {
		for (int x = 0; x < 10; x++)
		    {
			BufferedImage b = new BufferedImage(chunkwidth, chunkheight, img.getType());
			int chunkxstart = x * chunkwidth;
			int chunkystart = y * chunkwidth;
			int localchunkwidth = chunkwidth;
			int localchunkheight = chunkheight;

			//make sure image doesn't get cut off FIX
			if (x == 9)
			    localchunkwidth = width - (chunkwidth * 9);
	       
			if (y == 9)
			    localchunkheight = height - (chunkheight * 9);
	        

			b = img.getSubimage(chunkxstart, chunkystart, chunkwidth, chunkheight);
			chunks[y][x] = b;
			
							   
		    }
	    }
	
    }


    
    
}
