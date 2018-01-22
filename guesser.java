// No guarentee this compiles, I haven't wrote java in over a year and I am doing this in VIM 
import java.util.scanner;
public class guesser{
	String word;
	ArrayList synonyms;
	public guesser (String correctword, ArrayList thesynonyms)
	{
		word = correctword;
		synonyms = thesynonyms;
	}

	public void getGuess(String guess)
	{
		Scanner scan = new Scanner(System.in);
		String guess = scan.next();
		if (guess.equals(word)
				{
					System.out.println("CORRECT!");
					//give points or something
				}
			else if (thesynonyms.contains(guess)
			{
				System.out.println("CLOSE!");
			}

	}

