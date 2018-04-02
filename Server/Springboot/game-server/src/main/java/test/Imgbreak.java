package test;
import java.awt.image.BufferedImage;
import java.util.Random;


//breaks the image into pixels, transfers them, handles guesses, and more.
//currently not needed since this is being done on the client right now. might be done on the server in the future
public class Imgbreak{
    
    BufferedImage img;
    Game game;
    
    int[][] pixels;
    boolean[][] used;

    int totalpixels;
    int sent;
    int height;
    int width;

    String word;
    String[] synonyms;

    Random rand = new Random();
    boolean guessed = false;
    
  
    //get the game too, sendstringtoclient,sendstingtoallmembers
    public Imgbreak (BufferedImage i, String correctword, String[] thesynonyms, Game g)
    {
	img = i;
	word = correctword.toLowerCase();
	//synonyms = thesynonyms;
	game = g;
    }

    public void breakImage()
    {   //get some dimensions
	 height = 642;
	 width = 500;
	 
	
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

		
		for(int i = 0; i < game.gameMembers.size(); i++){
		      getGuess(game.gameMembers.get(i));
		    }
		try {
		Thread.sleep(1);
		}catch (InterruptedException e)
		{
			
		}
		// pixel strings start px, end with xp, and have all values separated by a comma, if this is how we want to do it ya know?
	      game.sendStringToAllMembers("px " + pixx + " " + pixy + " " + pixels[pixy][pixx]);
		
		sent++;
	    }
		
    }
    	//TODO: maybe fix this
    	public void getGuess(ConnectedClient c)
	{
    		//ignore guesses from people who already guessed.
    	if (!c.getGuessed())
    	{
	   
		//TODO: GET GUESS SOMEHOW
		//String guess = c.readInputLine();
    		String guess="wrong";
		if (guess != null)
		{
			guess = guess.toLowerCase();
		if (guess.equals(word))
				{
				    //send string to one player
					c.sendStringToClient("CORRECT!");
					//give points or something here
					int score = (totalpixels - sent) * 100 / totalpixels + 1;
					c.incrementScore(score); 
		
				}
		/* commented out since this feature is no longer in scope
			else 
			{
				for( String s : synonyms)
				{
				
					if( s.equals(guess))
							c.sendStringToClient("CLOSE!");	
				}
				
			
			}
			*/
		}
	
    	}
	}
    /* if we want the game to stop when one person guesses it
    public void setGuessed()
    {
	guessed = true;
    }
    */
        
}