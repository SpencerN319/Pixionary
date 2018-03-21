package sb_3.pixionary.Utilities.POJO.GameClasses;

import org.json.JSONObject;

/**
 * Created by fastn on 3/11/2018.
 */

public class Image {

    private int width;
    private int height;
    private int[] pixels;
    private String answer; //This should not be filled until someone guesses correctly.

    public Image() {
        //Empty image
    }

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[height*width];
    }

    public Image(JSONObject jsonObject) {
        try{
            this.width = jsonObject.getInt("width");
            this.height = jsonObject.getInt("height");
            this.pixels = new int[this.width*this.height];
        } catch(Exception e) {
            this.width = -1;
            this.height = -1;
            this.pixels = null;
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
