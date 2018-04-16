package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.sql.*;
import javax.imageio.ImageIO;
import org.springframework.boot.json.*;


//websocket has been closed and no method
public class Game implements Runnable{
//TODO : if gameSize = 1, play AI

  ConnectedClient host;
  //TODO: score stays with username not client. I think I fixed this already but we will see...
  ArrayList<ConnectedClient> gameMembers = new ArrayList<ConnectedClient>();
  ArrayList<ConnectedClient> rescuedOrphans = new ArrayList<ConnectedClient>();
  ArrayList<ConnectedClient> playAgains = new ArrayList<ConnectedClient>();
 
  final String gameName;
  int gameID;
  boolean playing = false;
  String category;
  String currentWord;
  int possiblePoints;
  int gameSize;
  int numPlayers;
 
  ArrayList<WordLink> words = new ArrayList<WordLink>();
  ArrayList<ConnectedClient> orphans = new ArrayList<ConnectedClient>();

  String hostName;
Imgbreak i;
int correctGuesses;

  public Game(ConnectedClient host, String gameName, String category, int size){
    
    this.host = host;
    hostName = host.getUsername();
    this.gameName = gameName;
    this.category = category;
    this.gameSize = size;
    gameMembers.add(host);
   this.gameID = host.userID;
   gameSize = 1;
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

  public void run()
  {
	  this.startGame();
  }
  public void startGame()
  {
	  playing = true;
	  this.sendStringToAllMembers("START");
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
          rs = statement.executeQuery("select word, location from Images where category='"+category+"';");
          System.out.println("select word, location from Images where category='"+category+"';");
          //get them words and links
          while (rs.next()) {
        
        	  	String word = rs.getString("word");
        	  	String link = rs.getString("location");
        	  	System.out.println("WORD LOADED");
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
      //TODO: update table with incremented games_played value
      //TODO: fix reconnecting
      this.sendStringToAllMembers("GG");
      int bestscore = 0;
	  String bestplayer = "nobody";
      for (ConnectedClient c: gameMembers)
      {
    	if (c.getLocalScore() > bestscore)
    	{
    		bestscore = c.getLocalScore();
    		bestplayer = c.getUsername();
    	}
    	
      }
      this.sendStringToAllMembers("Winner: "+bestplayer);
    //10 seconds to play again. only will play again if the host says yes
      try {
   		  
   	Thread.sleep(10000);
   	System.out.println("WOKE");
	}catch (InterruptedException e)
	{
		
	}
   boolean playNewGame = false;
  
      for (ConnectedClient c : playAgains)
      {
    	  if (c.equals(host))
    	  {
    		  playAgains.remove(c);
    		  playNewGame = true;
    		  break;
    	  }
      }
      if (playNewGame)
      {
    	  this.sendStringToAllMembers("NEWGAME");
      Game newgame = new Game(this.host, this.gameName, this.category, this.gameSize);

      for (ConnectedClient c : playAgains)
      {
    	 newgame.addMember(c);
      }
      newgame.startGame();
      }
      //just see how this ends up working
      this.sendStringToAllMembers("NONEWGAME");
      this.delete();
      
      
  }
  
  public void addMember(ConnectedClient joiningMember){
	  numPlayers+=2;
    gameMembers.add(joiningMember);
    joiningMember.setGameSession(this);
  }

  public void addRescuedOrphan(ConnectedClient joiningMember){
	  
	  for (ConnectedClient c: this.orphans)
	  {
		  if (joiningMember.getUsername().equals(c.getUsername()))
				  {
			  		System.out.println("Orphan score updated");
				  joiningMember.setLocalScore(c.getLocalScore());
				  }
	  }
	    rescuedOrphans.add(joiningMember);
	    System.out.println("Orphan has rejoined");
	    joiningMember.setGameSession(this);
	  }
  public void removeMemberFromMembersList(ConnectedClient leavingMember){
 /*   if(leavingMember == host){
      delete();
      return;
    }*/
	  //deletes game if nobody left in it
    gameMembers.remove(leavingMember);
    if (gameMembers.size() == 0)
    	System.out.println("Game empty, deleting game");
    	this.delete();

    orphans.add(leavingMember);
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
  
  public boolean findOrphan(String username)
  {
	  for (ConnectedClient c :orphans)
	  {
		  if (c.getUsername().equals(username))
		  {
			orphans.remove(c);
			return true;
		  }
	  }
	  return false;
  }
  
  public void addPlayAgain(ConnectedClient c)
  {
	  this.playAgains.add(c);
  }
  public void playRound()
  {
	  //get rejoining players back in the action
	  for (ConnectedClient c: rescuedOrphans)
	  {
		  gameMembers.add(c);
		  rescuedOrphans.remove(c);
	  }
	 
	  this.sendStringToAllMembers("ROUNDBEGIN");

	  //reset correct guess status and points
	  System.out.println("Preparing to reset");
	  for(int i = 0; i < gameMembers.size(); i++){
	      gameMembers.get(i).setGuessed(false);
	      gameMembers.get(i).resetRoundScore();
	      }
	  correctGuesses = 0;
	  
	  System.out.println("Round starting");
	  Random r = new Random();

	  int choice = r.nextInt(words.size() - 1);
	  

	  WordLink solution =words.get(choice);
	 

	String linkURL ="http://proj-309-sb-3.cs.iastate.edu/" + solution.getLink();
			currentWord = solution.getWord();

	//  currentWord = solution.getWord();
	//  System.out.println("5");

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
					  if (correctGuesses >=numPlayers)
						  break;
				 
				  Thread.sleep(1000);
				  this.sendStringToAllMembers("PING");
				  System.out.println("a second has passed");
				  System.out.println(correctGuesses);
				  System.out.println(numPlayers);
				  possiblePoints--;
				  }
				  }catch (InterruptedException e) {}
		  
		  this.sendStringToAllMembers("ROUNDEND");
		  System.out.println("Round has ended");
//	  } catch (IOException e) {
//
//	      System.out.println("Failed to load image: ");
//		  e.printStackTrace();
//	  }

	  
	  //update mysql with points from the round
	  for(int j = 0; j < gameMembers.size(); j++){
	      int roundscore = gameMembers.get(j).getRoundScore();
	      if (gameMembers.get(j).getUsername().length() == 5 &&gameMembers.get(j).getUsername().substring(0, 5).equals("guest"))
	      {
	    	  System.out.println("Scoreboard not updated for guest");
	      }else
	      {
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
	         rs.next();
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
	  }
	  

	for (ConnectedClient c :gameMembers)
	{
		this.sendStringToAllMembers("CURRENTSCORES");
		this.sendStringToAllMembers("USERSCORE " + c.getUsername()+ " " + c.getLocalScore());
		this.sendStringToAllMembers("ENDSCORES");
	}
	  try {
		  Thread.sleep(3000);
		  }catch (InterruptedException e) {}
  }

  
 	public void getGuess(ConnectedClient c, String guess)
	{
  		//ignore guesses from people who already guessed.
  	if (!c.getGuessed())
  	{
	   
		
		
  		
		if (guess != null)
		{
		
		if (guess.equals(currentWord))
				{
					System.out.println("Right guess made by " + c.getUsername());
				    //send string to one player
					c.sendStringToClient("CORRECT!");
					correctGuesses++;
					int score = possiblePoints;
					c.incrementScore(score); 
		
				}
		else
		{
			System.out.println(currentWord +" is right, but the user guessed "+ guess);
			System.out.println("Wrong guess made by " + c.getUsername());
			c.sendStringToClient("INCORRECT!");
		}
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
