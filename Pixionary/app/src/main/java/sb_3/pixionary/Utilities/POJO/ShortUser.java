package sb_3.pixionary.Utilities.POJO;

import org.json.JSONObject;

/**
 * Created by fastn on 3/8/2018.
 */

public class ShortUser {

    private String username;
    private int score;

    public ShortUser() {
        //empty ShortUser
    }

    public ShortUser(String username, int score) {
        this.score = score;
        this.username = username;
    }

    public ShortUser(JSONObject jsonObject) {
        if(jsonObject != null) {
            try {
                this.username = jsonObject.getString("username");
                this.score = jsonObject.getInt("score");
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            this.username = null;
            this.score = -1;
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
