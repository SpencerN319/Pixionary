package test;

import java.lang.ProcessBuilder;
import java.io.IOException;

public class Imgloader {
    String w;
    
    public Imgloader(String word)
    {
	w = word;
    }

    public void runScript()
    {
	try {
	ProcessBuilder pb = new ProcessBuilder("/home/kwswesey/imgloader.sh", w);
	pb.start();
	} catch (IOException e)
	    {
		System.out.println(e.getMessage());
	    }
    }

}
