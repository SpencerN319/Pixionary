package sb_3.pixionary.Utilities.POJO;

/**
 * Created by fastn on 2/16/2018.
 */

public class User {

    private String username, password, user_id, user_type;
    private int gamesPlayed ,score, category_count, image_count;

    public User() {

    }

    public User(String username, String password, String id, String userType, int games_played, int score, int category_count, int image_count) {
        this.username = username;
        this.password = password;
        this.user_id = id;
        this.user_type = userType;
        this.gamesPlayed = games_played;
        this.score = score;
        this.category_count = category_count;
        this.image_count = image_count;

    }

    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.user_id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return user_type;
    }

    public void setUserType(String userType) {
        this.user_type = userType;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCategoryCount(){
        return this.category_count;
    }

    public void setCategoryCount(int newCount){
        this.category_count = newCount;
    }

    public int getImageCount(){
        return this.image_count;
    }

    public void setImageCount(int newCount){
        this.image_count = newCount;
    }
}
