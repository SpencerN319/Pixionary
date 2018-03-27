package test;
//we might not ever need to run this sever side but idk
import java.lang.ProcessBuilder;
import java.io.IOException;
public class Imgtaker
{
    String w;
    public Imgtaker(String word)
    {
	w = word;
    }

    public void runScript()
    {
	try {
	ProcessBuilder pb = new ProcessBuilder("imggrabber.sh", w);
	pb.start();
	} catch (IOException e)
	    {
		System.out.println(e.getMessage());
	    }
    }

    
}