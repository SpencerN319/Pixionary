package sb_3.pixionary.ImageBuilder;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fastn on 2/1/2018.
 */

public class ImageBreakdown {

    private Bitmap image;
    private int[] pixels;
    private int width;
    private int height;



    //Used to local breakdown an image currently.
    public void breakDownImage (Context context, String imageName) {


        AssetManager manager = context.getAssets();
        InputStream stream;
        try {
            stream = manager.open(imageName);
            image = BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width*height];
        image.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
