package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.sql.*;
import javax.imageio.ImageIO;
import org.springframework.boot.json.*;
//TODO: modified URL

public class Game{

  ConnectedClient host;
  
  ArrayList<ConnectedClient> gameMembers = new ArrayList<ConnectedClient>();
  //we need some sort of unique game identifier, maybe make gameName have to be unique. for now lets trust end users to do dat
  final String gameName;
  int gameID;
  boolean playing = false;
  String category;
  String currentWord;
  int possiblePoints;
  ArrayList<WordLink> words = new ArrayList<WordLink>();
String hostName;
Imgbreak i;

  public Game(ConnectedClient host, String gameName, String category){
    
    this.host = host;
    hostName = host.getUsername();
    this.gameName = gameName;
    this.category = category;
    gameMembers.add(host);
    this.gameID = host.userID;
    /*
    boolean found = false;

    while (found == false)
    {
    	found=true;
    Random r = new Random(50000);
    for (Game g : Main.server.gamesList)
    {
    	possibleID=r.nextInt();
    	if (g.gameID == possibleID)
    		found=false;
    }
    this.gameID = possibleID;
    
    }
    */
    
  
  }

  public void startGame()
  {
	  playing = true;
	  this.sendStringToAllMembers("START");
      try {
       
          Connection conn1;
          String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sb3";
          String user = "dbu309sb3";
          String password = "Fx3tvTaq";
               conn1 = DriverManager.getConnection(dbUrl, user, password);
          System.out.println("*** Connected to the database ***");
          
          Statement statement = conn1.createStatement();
          ResultSet rs;
          rs = statement.executeQuery("select word, location from Images where category='"+category+"';");
        
          //get them words and links
          while (rs.next()) {
           // for (int x = 0; x < 20; x++) {  
        	  	String word = rs.getString("word");
        	  	String link = rs.getString("location");
        	  	System.out.println("WORD LOADED");
        	  	WordLink wl = new WordLink(word, link);
        	  	words.add(wl);
        	  	
        	  	this.sendStringToAllMembers("WORD "+word);
        	  	System.out.println("WORD SENT");
          }
          
      } catch (SQLException e) {
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());
      }
      //hard coded 3 games
      for (int count = 0; count < 1; count++)
      {
    	  System.out.println("Begin round");
	  this.playRound();
      }
      this.sendStringToAllMembers("GG");
      this.delete();
      
      //something to indicate that the game has ended
  }
  
  public void addMember(ConnectedClient joiningMember){
    gameMembers.add(joiningMember);
    joiningMember.setGameSession(this);
  }

  public void removeMemberFromMembersList(ConnectedClient leavingMember){
    if(leavingMember == host){
      delete();
      return;
    }
    gameMembers.remove(leavingMember);
  }

  public void kickMember(ConnectedClient kicked){
    if(hasMember(kicked)){
      kicked.leaveGame();
    }
  }

  public boolean hasMember(ConnectedClient member){
    return gameMembers.contains(member);
  }

  public void sendStringToAllMembers(String output){
    for(int i = 0; i < gameMembers.size(); i++){
      gameMembers.get(i).sendStringToClient(output);
    }
  }

  
  /* we aren't treating the host any different
  private void sendStringToHost(String output){
    host.sendStringToClient(output);
  }

  private void sendStringToNonHostMembers(String output){
    for(int i = 0; i < gameMembers.size(); i++){
      if(gameMembers.get(i) != host){
        gameMembers.get(i).sendStringToClient(output);
      }
    }
  }
  */

  public String getName(){
    return gameName;
  }
  
  public String getHostName()
	  {
		  return hostName;
	  }
  
  public String getCategory(){
	  return category;
  }
	  

  
  public int getID()
  {
	  return gameID; 
  }
  
  public boolean getGameStatus()
  {
	  return playing;
  }
  
  public void playRound()
  {
	 
	  this.sendStringToAllMembers("ROUNDBEGIN");

	  //reset correct guess status and points
	  System.out.println("Preparing to reset");
	  for(int i = 0; i < gameMembers.size(); i++){
	      gameMembers.get(i).setGuessed(false);
	      gameMembers.get(i).resetRoundScore();
	      }
	  
	  System.out.println("Round starting");
	  Random r = new Random();
	 System.out.println(words.size());
	  int choice = r.nextInt(words.size() - 1);
	  
	  	System.out.println(choice);
	  WordLink solution =words.get(choice);
	  System.out.println("3");

	String linkURL ="http://proj-309-sb-3.cs.iastate.edu/" + solution.getLink();
	  System.out.println("4");

	//  currentWord = solution.getWord();
	  System.out.println("5");

	  //hard coded now just to make sure this part works
	  String URL = linkURL;
	//  Imgloader il = new Imgloader(URL);
	 // il.runScript();
	  //BufferedImage img = null;
	  /*
	  try {
		  try {
	   		  System.out.println("SLEEPY TIME");
	   	Thread.sleep(500);
	   	System.out.println("WOKE");
		}catch (InterruptedException e)
		{
			
		}
	     BufferedImage img = ImageIO.read(new File("/home/kwswesey/image.jpg"));
	      System.out.println("Image has been loaded");
		  System.out.println("Getting and sending dimensions");
		  int height = 642;
		  //System.out.println(img.getHeight());
		  int width = 500;
		  //System.out.println(img.getWidth());
		  this.sendStringToAllMembers("HEIGHT "+height+" WIDTH " + width);
		  
		  i = new Imgbreak(img, "volvo",null, this);
		  /*
		  System.out.println("breaking image");
		  i.breakImage();
		  System.out.println("SEnding pixels");
		  i.sendPixels();
		  */
		  this.sendStringToAllMembers("URL "+ URL);
		   possiblePoints = 100;
		  try {
		  for (int seconds = 120; seconds > 0; seconds--)
		  {
		  Thread.sleep(1000);
		  possiblePoints--;
		  }
		  }catch (InterruptedException e) {}
		  
		  this.sendStringToAllMembers("ROUNDEND");
//	  } catch (IOException e) {
//
//	      System.out.println("Failed to load image: ");
//		  e.printStackTrace();
//	  }

	  
	  //update mysql with points from the round
	  for(int j = 0; j < gameMembers.size(); j++){
	      int roundscore = gameMembers.get(j).getRoundScore();
	      try {
	        
	          Connection conn1;
	          String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sb3";
	          String user = "dbu309sb3";
	          String password = "Fx3tvTaq";
	               conn1 = DriverManager.getConnection(dbUrl, user, password);
	          System.out.println("*** Connected to the database ***");
	          
	          Statement statement = conn1.createStatement();
	          ResultSet rs;
	          rs = statement.executeQuery("select score from User where username='"+gameMembers.get(j).getUsername()+"';");
	          int totalScore = rs.getInt("Score");
	          totalScore+=roundscore;
	          statement.executeUpdate("UPDATE User SET score ="+totalScore+" WHERE username ='"+gameMembers.get(j).getUsername()+"';");
	          
	      } catch (SQLException e) {
	          System.out.println("SQLException: " + e.getMessage());
	          System.out.println("SQLState: " + e.getSQLState());
	          System.out.println("VendorError: " + e.getErrorCode());
	      }
	      //JDBC query. I forget why I put this comment here, hopefully i just misplaced it.
	      }
	  //TODO: update client with every player's score
  }

  
 	public void getGuess(ConnectedClient c, String guess)
	{
  		//ignore guesses from people who already guessed.
  	if (!c.getGuessed())
  	{
	   
		//TODO: GET GUESS SOMEHOW
		//String guess = c.readInputLine();
  		
		if (guess != null)
		{
			guess = guess.toLowerCase();
		if (guess.equals(currentWord))
				{
				    //send string to one player
					c.sendStringToClient("CORRECT!");
					//give points or something here
					int score = possiblePoints;
					c.incrementScore(score); 
		
				}
		/* commented out since this feature is no longer in scope
			else 
			{
				for( String s : synonyms)
				{
				
					if( s.equals(guess))
							c.sendStringToClient("CLOSE!");	
				}
				
			
			}
			*/
		}
	
  	}
	}
  public void delete(){
    for(int i = 0; i < gameMembers.size(); i++){
      if(gameMembers.get(i) != host){
        kickMember(gameMembers.get(i));
      }
    }
    //All except host is gone, and host is currently leaving.
    Main.server.gamesList.remove(this);
    return;
  }

}
