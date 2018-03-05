package test;

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
	ProcessBuilder pb = new ProcessBuilder("imgtaker.bash", w);
	pb.start();
	} catch (IOException e)
	    {
		System.out.println(e.getMessage());
	    }
    }

    
}