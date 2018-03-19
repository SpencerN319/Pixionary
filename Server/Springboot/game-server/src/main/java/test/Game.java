package test;
//TODO: correct jdbc connection
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
  }

  public void startGame()
  {
	  playing = true;
	 
      try {
          // TODO: set this correctly
          Connection conn1;
          String dbUrl = "jdbc:mysql://localhost:3306/db363kwswesey";
          String user = "root";
          String password = "";
               conn1 = DriverManager.getConnection(dbUrl, user, password);
          System.out.println("*** Connected to the database ***");
          
          Statement statement = conn1.createStatement();
          ResultSet rs;
          rs = statement.executeQuery("select Word, Link from Images where Category="+category+";");
        
          //get them words and links
          while (rs.next()) {
              
        	  	String word = rs.getString("Word");
        	  	String link = rs.getString("Link");
        	  	WordLink wl = new WordLink(word, link);
        	  	words.add(wl);
          }
          
      } catch (SQLException e) {
          System.out.println("SQLException: " + e.getMessage());
          System.out.println("SQLState: " + e.getSQLState());
          System.out.println("VendorError: " + e.getErrorCode());
      }
      //hard coded 3 games
      for (int count = 0; count < 3; count++)
	  this.playRound();
      
      //something to indicate that the game has ended
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
	  //hard coded now just to make sure this part works
	  String URL = "https://i.imgur.com/AEWms1M.jpg";
	  Imgloader il = new Imgloader(URL);
	  il.runScript();
	  BufferedImage img = null;
	  
	  try {
	      img = ImageIO.read(new File("image.jpg"));
	  } catch (IOException e) {

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
	          // TODO: set this correctly
	          Connection conn1;
	          String dbUrl = "jdbc:mysql://localhost:3306/db363kwswesey";
	          String user = "root";
	          String password = "";
	               conn1 = DriverManager.getConnection(dbUrl, user, password);
	          System.out.println("*** Connected to the database ***");
	          
	          Statement statement = conn1.createStatement();
	          ResultSet rs;
	          rs = statement.executeQuery("select score from Players where Name="+gameMembers.get(j).getUsername()+";");
	          int totalScore = rs.getInt("Score");
	          totalScore+=roundscore;
	          statement.executeQuery("UPDATE Customers SET Score ="+totalScore+" WHERE Name ="+gameMembers.get(j).getUsername()+";");
	          
	      } catch (SQLException e) {
	          System.out.println("SQLException: " + e.getMessage());
	          System.out.println("SQLState: " + e.getSQLState());
	          System.out.println("VendorError: " + e.getErrorCode());
	      }
	      //JDBC query
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
