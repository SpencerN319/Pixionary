import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;

//breaks the image into pixels, transfers them, handles guesses, and more.
public class imgbreak{
    
    BufferedImage img;
   // Game game;
    
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
    public imgbreak (BufferedImage i, String correctword, String[] thesynonyms/*, Game g*/)
    {
    img = i;
    word = correctword;
    synonyms = thesynonyms;
    /*game = g;*/
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

        //Thread.sleep(1);
         // pixel strings start px, end with xp, and have all values separated by a comma, if this is how we want to do it ya know?
          System.out.println("px," + pixx + "," + pixy + "," + pixels[pixy][pixx] + ",xp");
        
        sent++;
        }
        
    }
    /*
        public void getGuess(ConnectedClient c)
    {

        //c.getStringFromClient?????
        Scanner scan = new Scanner(System.in);
        String guess = scan.next();
        if (guess.equals(word))
                {
                    //send string to one player
                    c.sendStringToClient("CORRECT!");
                    //give points or something here
                    int score = (totalpixels - sent) * 100 / totalpixels + 1;
                    //c.incrementScore(score); 
                }
            else 
            {
                for( String s : synonyms)
                {
                
                    if( s.equals(guess))
                            c.sendStringToClient("CLOSE!"); 
                }
                
            
            }
        scan.close();

    }
    */
    public void setGuessed()
    {
    guessed = true;
    }
        
}
