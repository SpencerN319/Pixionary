package test;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//import org.springframework.web.socket.WebSocketSession;	
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Test {

    @RequestMapping("/test")
    public String index() {
    	
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
	        
	       
	          
	      } catch (SQLException e) {
	          System.out.println("SQLException: " + e.getMessage());
	          System.out.println("SQLState: " + e.getSQLState());
	          System.out.println("VendorError: " + e.getErrorCode());
	      }
    	
        return "Server is at least working this much.";
    }

}