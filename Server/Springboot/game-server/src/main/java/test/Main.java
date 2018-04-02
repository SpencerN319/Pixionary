package test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
	
	public static PixionaryServer server;
	public static void main (String[] args){
	    //creates a server and then lets springboot do its thing
		server = new PixionaryServer(1337);
		SpringApplication.run(Main.class, args);
		
		
	    
		
	}
	
}
