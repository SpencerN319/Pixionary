package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Game{

  ConnectedClient host;
  GamesList gamesList;
  ArrayList<ConnectedClient> gameMembers = new ArrayList<ConnectedClient>();
  //we need some sort of unique game identifier, maybe make gameName have to be unique. for now lets trust end users to do dat
  final String gameName;
  boolean playing = false;
  String category;

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
	  this.playRound();
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
	  //reset correct guess status
	  for(int i = 0; i < gameMembers.size(); i++){
	      gameMembers.get(i).setGuessed(false);
	      }
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
