package ImageBuilder;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import java.util.Arrays;

import sb_3.pictureguesser.PlayActivity;

/**
 * Created by fastn on 1/30/2018.
 */

public class ImageCreator {

    private int pixarr[];
    private Bitmap image;
    private int imgWidth;
    private int imgHeight;

    public ImageCreator(int width, int height) {
        //Set width and height.
        imgWidth = width;
        imgHeight = height;

        image = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
    }

    public Bitmap getImage() {
        return image;
    }

//    public void updateImage(Pixel pix) {
//        //Set the pixel wee want to change the value of.
//        image.setPixel(pix.getXPosition(), pix.getYPosition(), pix.getColor());
//    }

//    public Pixel randomPixel() {
//
//    }
}
