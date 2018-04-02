package test;
//TODO: gameover
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;
import javax.imageio.ImageIO;

public class Game{

  ConnectedClient host;
  GamesList gamesList;
  ArrayList<ConnectedClient> gameMembers = new ArrayList<ConnectedClient>();
  //we need some sort of unique game identifier, maybe make gameName have to be unique. for now lets trust end users to do dat
  final String gameName;
  boolean playing = false;
  String category;
  ArrayList<WordLink> words = new ArrayList<WordLink>();

  public Game(GamesList gamesList, ConnectedClient host, String gameName, String category){
    this.gamesList = gamesList;
    this.host = host;
    this.gameName = gameName;
    this.category = category;
    gameMembers.add(host);
    this.gameID = host.userID;
    /* if we need to generate a random game ID
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
	 
      try {
          //gets all images and words for the selected playlist
          Connection conn1;
          String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309sb3";
          String user = "dbu309sb3";
          String password = "Fx3tvTaq";
               conn1 = DriverManager.getConnection(dbUrl, user, password);
          System.out.println("*** Connected to the database ***");
          
          Statement statement = conn1.createStatement();
          ResultSet rs;
          rs = statement.executeQuery("select Word, Link from Images where Category'="+category+"';");
        
          //get them words and links
          while (rs.next()) {
              
        	  	String word = rs.getString("Word");
        	  	String link = rs.getString("Link");
        	  	WordLink wl = new WordLink(word, link);
        	  	words.add(wl);

        	  	//sends all words in the playlist to all members
        	  	this.sendStringToAllMembers("WORD "+word);
        	  	System.out.println("WORD SENT");
          }
          
      } catch (SQLException e) {
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());
      }

      //hard coded 2 rounds for now
      for (int count = 0; count < 2; count++)
      {
    	  System.out.println("Begin round");
	  this.playRound();
      }
      
      //endgame

      this.sendStringToAllMembers("GG");
      this.delete();
      
      
  }
  
  public void addMember(ConnectedClient joiningMember){
    gameMembers.add(joiningMember);
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

  


  public String getName(){
    return gameName;
  }
  
  public boolean getGameStatus()
  {
	  return playing;
  }
  
  public void playRound()
  {
	  //reset correct guess status and points
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
	 

	String linkURL ="http://proj-309-sb-3.cs.iastate.edu/" + solution.getLink();
	 

	//  currentWord = solution.getWord();
	  System.out.println("5");

	  //in case we want to break the image serverside again
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
		  //send the image
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

	      System.out.println("Failed to load image: ");
		  e.printStackTrace();
	  }
	  
	  Imgbreak i = new Imgbreak(img, "cat",null, this);
	  i.breakImage();
	  i.sendPixels();
	  
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
	          rs = statement.executeQuery("select Score from Players where Name='"+gameMembers.get(j).getUsername()+"';");
	          int totalScore = rs.getInt("Score");
	          totalScore+=roundscore;
	          statement.executeUpdate("UPDATE Customers SET Score ="+totalScore+" WHERE Name ='"+gameMembers.get(j).getUsername()+"';");
	          
	      } catch (SQLException e) {
	          System.out.println("SQLException: " + e.getMessage());
	          System.out.println("SQLState: " + e.getSQLState());
	          System.out.println("VendorError: " + e.getErrorCode());
	      }
	      //JDBC query. I forget why I put this comment here, hopefully i just misplaced it.
	      }
  }

  public void delete(){
    for(int i = 0; i < gameMembers.size(); i++){
      if(gameMembers.get(i) != host){
        kickMember(gameMembers.get(i));
      }
    }
    //All except host is gone, and host is currently leaving.
    gamesList.removeGameFromActiveGames(gameName);
    return;
  }

}
