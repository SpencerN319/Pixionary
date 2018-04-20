package sb_3.pixionary.Utilities.POJO.GameClasses;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by spencern319 on 3/14/18.
 */

public class Bot {

    private ArrayList<String> word_list;
    private int difficulty;
    private Random rand = new Random();
    private String key, name;
    private int score;
    private boolean correct = false;

    public Bot(String name, int difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public Bot(String name, ArrayList<String> words, int difficulty){
        this.name = name;
        this.score = 0;
        this.word_list = words;
        this.difficulty = difficulty;

    }

    public void set_key_word(String key){
        this.key = key;
    }

    public String guess(){
        int size = word_list.size();
        if (size > 0) {
            int num = rand.nextInt(size); //random pick from word list
            String guess = word_list.get(num);
            word_list.remove(num);
            return guess;
        }
        return null;

    }

    public void increment_score(int points){
        this.score += points;
    }

    public void set_word_list(ArrayList<String> list){
        this.word_list = list;
    }

    public ArrayList<String> getWord_list() {
        return word_list;
    }

    public void setWord_list(ArrayList<String> word_list) {
        this.word_list = word_list;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean gotCorrect) {
        this.correct = gotCorrect;
    }
}
