package sb_3.pixionary.Utilities.POJO.GameClasses;

import java.util.Random;

/**
 * Created by spencern319 on 3/14/18.
 */

public class Bot {

    private String[] word_list;
    private int level;
    private Random rand = new Random();
    private String key, name;
    private int score;

    public Bot(String name, String[] words, int difficulty){
        this.name = name;
        this.score = 0;
        this.word_list = words;
        this.level = difficulty;

    }

    public void set_key_word(String key){
        this.key = key;
    }

    public String guess(){
        int size = word_list.length;
        int num = rand.nextInt(size); //random pick from word list
        return word_list[num];
    }

    public void increment_score(int points){
        this.score += points;
    }

    public void set_word_list(String[] list){
        this.word_list = list;
    }

    public int get_difficulty(){
        return this.level;
    }

}
