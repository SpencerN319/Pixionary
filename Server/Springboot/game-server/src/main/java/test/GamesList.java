package test;

import java.util.Hashtable;

public class GamesList{

  private Hashtable<String, Game> namesAndGames = new Hashtable<String, Game>();

  public GamesList(){

  }

  public boolean gameExists(String gameName){
    return namesAndGames.get(gameName) != null;
  }

  public Game getGame(String gameName){
    return namesAndGames.get(gameName);
  }

  public Game createGame(ConnectedClient host, String gameName,  String category){
    if(gameExists(gameName)){
      return null;
    }
    Game newGame = new Game(this, host, gameName, category);
    namesAndGames.put(gameName, newGame);
    System.out.println("Game created: " + gameName);
    System.out.println("# of games existing:" + namesAndGames.size());
    return newGame;
  }

  public void removeGameFromActiveGames(String gameName){
    namesAndGames.remove(gameName);
    System.out.println("Game ended: " + gameName);
    System.out.println("# of games running:" + namesAndGames.size());
  }

}
