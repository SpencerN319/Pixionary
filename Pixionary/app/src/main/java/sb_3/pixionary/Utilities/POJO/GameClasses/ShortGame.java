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

    private String host, gameName;
    private int gameID;
    private int gameType;
    private Playlist playlist;

    /**
     * This ShortGame constructor is with no parameters.
     */
    public ShortGame() {
        this.gameID = IDERROR;
        this.host = null;
        this.gameName = null;
        this.gameType = GAMENOTSET;
        this.playlist = null;
    }

    /**
     * This ShortGame constructor is used for a ShortGame with all parameters filled.
     *
     * @param gameID The ID key for joining a game or for requesting a game from server.
     * @param host The username of the host.
     * @param gameName The game name assigned by the host.
     * @param gameType The game type, used to determine A.I., 1v1, or FFA game type.
     * @param playlist The playlist of the game.
     */
    public ShortGame(int gameID, String host, String gameName, int gameType, Playlist playlist) {
        this.gameID = gameID;
        this.host = host;
        this.gameName = gameName;
        this.gameType = gameType;
        this.playlist = playlist;
    }

    /**
     * This method is used to create a ShortGame object that is used by host to request a game.
     * @param gameID The ID key for joining a game.
     * @param host The username of the host.
     * @param gameName The game name assigned by the host.
     * @param gameType The game type, used to determine A.I., 1v1, or FFA game type.
     * @param playlist The playlist of the game.
     * @return A ShortGame object for a host request.
     */
    public ShortGame shortGameForHostRequest(int gameID, String host, String gameName, int gameType, Playlist playlist) {
        return new ShortGame(gameID, host, gameName, gameType, playlist);
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
            setGameId(object.getInt("id"));
        } catch(JSONException e) {
            setGameId(IDERROR);
        }
        try{
            setHost(object.getString("host"));
        } catch(JSONException e) {
            setHost(null);
        }
        try{
            setGameName(object.getString("name"));
        } catch(JSONException e) {
            setGameName(null);
        }
        try{
            setGameType(object.getInt("gametype"));
        } catch(JSONException e) {
            setGameType(GAMENOTSET);
        }
        try{
            Playlist playlist = new Playlist();
            playlist.setName(object.getString("PlaylistName"));
            setPlaylist(playlist);
        } catch(JSONException e) {
            setPlaylist(null);
        }
    }

    public JSONObject createJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", getGameId());
            obj.put("host", getHost());
            obj.put("name", getGameName());
            obj.put("type", getGameType());
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String name) {
        this.gameName = name;
    }

    public int getGameId() {
        return gameID;
    }

    public void setGameId(int id) {
        this.gameID = id;
    }

    public void setIDRequest() { this.gameID = IDREQUEST; }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public void setGameTypeAI() { this.gameType = GAMEAI; }

    public void setGameType1V1() { this.gameType = GAME1V1; }

    public void setGameTypeFFA() { this.gameType = GAMETYPEFFA; }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
