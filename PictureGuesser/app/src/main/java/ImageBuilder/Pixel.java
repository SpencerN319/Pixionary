package ImageBuilder;

/**
 * Created by fastn on 1/30/2018.
 */

public class Pixel {
    private int argb;
    private int xpos;
    private int ypos;

    public Pixel(int color, int x, int y) {

        //Create pixel values.
        argb = color;
        xpos = x;
        ypos = y;
    }

    public int getXPosition() {
        return xpos;
    }

    public int getYPosition() {
        return ypos;
    }

    public void setXPosition(int x) {
        xpos = x;
    }

    public void setYPosition(int y) {
        ypos = y;
    }

    public int getColor() {
        return argb;
    }




}
