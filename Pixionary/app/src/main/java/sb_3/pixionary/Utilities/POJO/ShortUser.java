package sb_3.pixionary.Utilities.POJO;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fastn on 3/8/2018.
 */

public class ShortUser {

    private String username;
    private int score;
    private boolean success;

    public ShortUser() {
        //empty ShortUser
    }

    public ShortUser(String username, int score) {
        this.score = score;
        this.username = username;
        this.success = true;
    }

    public ShortUser(JSONObject jsonObject) {
        if(jsonObject != null) {
            try {
                this.username = jsonObject.getString("username");
                this.score = jsonObject.getInt("score");
                this.success = true;
            } catch(Exception e) {
                e.printStackTrace();
                this.username = null;
                this.score = -1;
                this.success = false;
            }
        } else {
            this.username = null;
            this.score = -1;
            this.success = false;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
