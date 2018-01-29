// get guess from user and see if it is correct. will need to be modified for networking stuff
import java.util.Scanner;
public class guesser{
	String word;
	String[] synonyms;
	public guesser (String correctword, String[] thesynonyms)
	{
		word = correctword;
		synonyms = thesynonyms;
	}

	public void getGuess()
	{
		Scanner scan = new Scanner(System.in);
		String guess = scan.next();
		if (guess.equals(word))
				{
					System.out.println("CORRECT!");
					//give points or something here
				}
			else 
			{
				for( String s : synonyms)
				{
				
					if( s.equals(guess))
							System.out.println("CLOSE!");	
				}
				
			
			}
		scan.close();

	}

}
