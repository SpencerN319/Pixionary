package sb_3.pixionary.Utilities.POJO.GameClasses;

import org.json.JSONObject;

/**
 * Created by fastn on 3/12/2018.
 */

public class ShortGame {

    private String host, name;
    private int id;

    public ShortGame() {
        //empty constructor
    }

    public ShortGame(String host, String name, int id) {
        this.host = host;
        this.name = name;
        this.id = id;
    }

    public ShortGame(JSONObject object) {
        if(object != null) {
            try {
                this.host = object.getString("host");
                this.name = object.getString("name");
                this.id = object.getInt("id");
            } catch (Exception e) {
                e.printStackTrace();
                this.host = "error";
                this.name = "error";
                this.id = -1;
            }
        } else {
            this.host = "error";
            this.name = "error";
            this.id = -1;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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
}
