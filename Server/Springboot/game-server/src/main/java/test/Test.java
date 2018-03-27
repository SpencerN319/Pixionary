package test;
//used for debugging, should NEVER be called during a regular game
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

//import org.springframework.web.socket.WebSocketSession;	
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Test {

    @RequestMapping("/test")
    public String index() {
    	/*
    	try {
    		ProcessBuilder pb = new ProcessBuilder("/home/kwswesey/imgloader.sh", "https://i.imgur.com/AEWms1M.jpg");
    		pb.start();
    		} catch (IOException e)
    		    {
    			System.out.println(e.getMessage());
    		    }
    	*/
    	 String URL = "https://i.imgur.com/AEWms1M.jpg";
   	  Imgloader il = new Imgloader(URL);
   	  il.runScript();
   	  try {
   		  System.out.println("SLEEPY TIME");
   	Thread.sleep(500);
   	System.out.println("WOKE");
	}catch (InterruptedException e)
	{
		
	}
    	try {
    	 BufferedImage img = ImageIO.read(new File("/home/kwswesey/image.jpg"));
	      System.out.println("Image has been loaded");
		  System.out.println("Getting and sending dimensions");
		  int height = 642;
		  System.out.println(img.getHeight());
    	}catch (IOException e) {

  	      System.out.println("Failed to load image: ");
  		  e.printStackTrace();
  	  }
    	 try {
		       
	          Connection conn1;
	          String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sb3";
	          String user = "dbu309sb3";
	          String password = "Fx3tvTaq";
	               conn1 = DriverManager.getConnection(dbUrl, user, password);
	          System.out.println("*** Connected to the database ***");
	          
	          Statement statement = conn1.createStatement();
	          
	          //this probably isn't right
	         // statement.executeUpdate("INSERT INTO Games (Host, Category, Name, ID) VALUES "
	          //		+ "('" + g.getHostName() +"', '" + g.getCategory() + "', '" +g.getName() + "', '" + g.getID() + "');");
	         // statement.executeUpdate("DELETE FROM Active WHERE host_id ='0';");
	       
	          
	      } catch (SQLException e) {
	          System.out.println("SQLException: " + e.getMessage());
	          System.out.println("SQLState: " + e.getSQLState());
	          System.out.println("VendorError: " + e.getErrorCode());
	      }
    	
        return "Server is at least working this much.";
    }

}