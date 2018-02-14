import java.lang.ProcessBuilder;
import java.lang.Process;
import java.io.IOException;
public class imgtaker
{
    String w;
    public imgtaker(String word)
    {
	w = word;
    }

    public void runScript()
    {
	try {
	ProcessBuilder pb = new ProcessBuilder("imgtaker.bash", w);
	pb.start();
	} catch (IOException e)
	    {
		System.out.println(e.getMessage());
	    }
    }

    
}
