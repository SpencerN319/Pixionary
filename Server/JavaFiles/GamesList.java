import java.util.Hashtable;

public class GamesList{

  private Hashtable<String, Game> namesAndGames = new Hashtable<String, Game>();

  public GamesList(){

  }

  public boolean gameExists(String gameName){
    return !(namesAndGames.get(gameName) == null);
  }

  public Game getGame(String gameName){
    return namesAndGames.get(gameName);
  }

  public Game startGame(ConnectedClient host, String gameName){
    if(gameExists(gameName)){
      return null;
    }
    return namesAndGames.put(gameName, new Game(this, host));
  }

  public void endGame(String gameName){
    if(gameExists(gameName)){
      namesAndGames.get(gameName).delete();
    }
    namesAndGames.put(gameName, null);
  }

}
