package sb_3.pixionary.Utilities.POJO.GameClasses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fastn on 3/12/2018.
 */

public class ShortGame {

    private int IDERROR = -1;
    private int IDREQUEST = 0;
    private int GAMENOTSET = -1;
    private int GAMEAI = 0;
    private int GAME1V1 = 1;
    private int GAMETYPEFFA = 2;

    private String host;
    private int players, max;
    private Playlist playlist;

    /**
     * This ShortGame constructor is with no parameters.
     */
    public ShortGame() {
        this.host = null;
        this.playlist = null;
        this.players = 0;
        this.max = 0;
    }

    /**
     * This ShortGame constructor is used for a ShortGame with all parameters filled.
     *
     * @param host The username of the host.
     * @param playlist The playlist of the game.
     * @param players The number of players in the game.
     */
    public ShortGame(String host, Playlist playlist, int players, int max) {
        this.host = host;
        this.playlist = playlist;
        this.players = players;
        this.max = max;
    }

    /**
     * This method is used to create a ShortGame object that is used by host to request a game.
     * @param host The username of the host.
     * @param playlist The playlist of the game.
     * @return A ShortGame object for a host request.
     */
    public ShortGame shortGameForHostRequest(String host, Playlist playlist, int players, int max) {
        return new ShortGame(host, playlist, players, max);
    }

    /**
     * Method is used to parse a JSONobject to create a ShortGame
     * @param object JSONObject to be parsed.
     * @return A ShortGame object created from a JSONobject.
     */
    public void setShortGameFromJSON(JSONObject object) {

        if(object == null) {
            return;
        }
        try{
            setHost(object.getString("host"));
        } catch(JSONException e) {
            setHost(null);
        }
        try{
            Playlist playlist = new Playlist();
            playlist.setName(object.getString("playlist_name"));
            setPlaylist(playlist);
        } catch(JSONException e) {
            setPlaylist(null);
        }
        try{
            setPlayers(object.getInt("players"));
        } catch(JSONException e) {
            setPlayers(0);
        }
        try{
            setMax(object.getInt("max"));
        } catch(JSONException e) {
            setMax(0);
        }
    }

    public JSONObject createJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("host", getHost());
            obj.put("playlistName", getPlaylist().getName());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
