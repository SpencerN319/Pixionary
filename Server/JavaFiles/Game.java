import java.util.ArrayList;

public class Game{

  ConnectedClient host;
  GamesList gamesList;
  ArrayList<ConnectedClient> gameMembers = new ArrayList<ConnectedClient>();
  final String gameName;

  public Game(GamesList gamesList, ConnectedClient host, String gameName){
    this.gamesList = gamesList;
    this.host = host;
    this.gameName = gameName;
    gameMembers.add(host);
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

  private void sendStringToAllMembers(String output){
    for(int i = 0; i < gameMembers.size(); i++){
      gameMembers.get(i).sendStringToClient(output);
    }
  }

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

  public String getName(){
    return gameName;
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
