package sb_3.pixionary.Utilities.POJO.GameClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fastn on 3/8/2018.
 */

//TODO this is a work in progress dependent upon what we need from the server.
public class Game {

    private String host, title;
    private int maxPlayers;
    private List<Player> players;
    private int numOfImages;

    public Game() {
        //Empty game
    }

    public Game(String host, String title, int maxPlayers, int numOfImages) {
        this.host = host;
        this.title = title;
        this.maxPlayers = maxPlayers;
        this.numOfImages = numOfImages;
        this.players = new ArrayList<>();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getNumOfImages() {
        return numOfImages;
    }

    public void setNumOfImages(int numOfImages) {
        this.numOfImages = numOfImages;
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void removePlayer(Player p) {
        players.remove(p);
    }
}
