package ImageBuilder;

import android.graphics.Bitmap;
import android.widget.ImageView;

import android.os.Handler;

import java.util.Random;

import ImageBuilder.Pixel;

/**
 * Created by fastn on 1/30/2018.
 */

public class ImageCreator {

    private Bitmap image;
    private int imgWidth;
    private int imgHeight;

    //Temporary for testing
    private int tempwidth = 10;
    private int tempheight = 10;
    private int color = 0xFFFFCCED;

    public ImageCreator(int width, int height) {
        //Set width and height.
        imgWidth = width;
        imgHeight = height;

        image = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
    }

    public Bitmap getImage() {
        return image;
    }

    //Normally need to pass a Pixel in.
    public void updateImage(final Bitmap bitImage) {

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            Random random = new Random();
            @Override
            public void run() {
                bitImage.setPixel(random.nextInt(imgWidth), random.nextInt(imgHeight), 0xFFCCED21);
                handler.postDelayed(this, 100);

            }
        };
        handler.postDelayed(runnable, 1);


    }


}
