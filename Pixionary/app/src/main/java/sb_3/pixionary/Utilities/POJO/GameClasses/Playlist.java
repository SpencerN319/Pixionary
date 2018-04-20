package sb_3.pixionary.Utilities.POJO.GameClasses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fastn on 3/12/2018.
 */

public class Playlist {

    private String name;
    private String url;

    public Playlist() {
        //empty constructor
    }

    public Playlist(String name) {
        this.name = name;
    }

    public Playlist(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

