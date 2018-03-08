package sb_3.pixionary.Utilities.POJO.GameClasses;

/**
 * Created by fastn on 3/8/2018.
 */

public class Player {

    private String name;
    private int numCorrect;

    public Player() {
        //Empty Player
    }

    public Player(String username, int correct) {
        this.name = username;
        this.numCorrect = correct;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumCorrect(int numCorrect) {
        this.numCorrect = numCorrect;
    }

    public void addOneCorrect() {
        this.numCorrect += 1;
    }
}
