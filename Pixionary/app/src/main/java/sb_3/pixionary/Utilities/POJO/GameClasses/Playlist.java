package sb_3.pixionary.Utilities.POJO.GameClasses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fastn on 3/12/2018.
 */

public class Playlist  {

    private String name;
    private int id;
    private String creator;
    private boolean success;

    public Playlist() {
        //empty constructor
    }

    public Playlist(int id, String name, String creator) {
        this.name = name;
        this.id = id;
        this.creator = creator;
        this.success = true;
    }

    public Playlist(JSONObject object) {
        if (object != null) {
            try {
                this.id = object.getInt("id");
                this.name = object.getString("name");
                this.creator = object.getString("creator");
                this.success = true;
            } catch(JSONException e) {
                e.printStackTrace();
                this.id = -1;
                this.name = "error";
                this.creator = "error";
                this.success = false;
            }
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
