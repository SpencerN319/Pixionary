package sb_3.pixionary.ImageBuilder;

import android.content.Context;
import android.graphics.Bitmap;

import android.os.Handler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;



/**
 * Created by fastn on 1/30/2018.
 */

public class ImageCreator {

    private ImageBreakdown breaker;
    private Bitmap image;
    private int imgWidth;
    private int imgHeight;
    private boolean[] pixelUsed;
    private int[] pixels;

    private Handler handler;
    private Runnable runnable;

    //Local Image creator
    public ImageCreator(Context context, InputStream inputStream) {

        //Creates a instance of class ImageBreakdown to get pixel array, move to server-side.
        breaker = new ImageBreakdown();
        breaker.breakDownImage(context, inputStream);

        //Set width and height.
        imgWidth = breaker.getWidth();
        imgHeight = breaker.getHeight();

        //Initializes blank mutable Bitmap
        image = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);

    }

    //Building image from the data on server.
    public ImageCreator(int imageWidth, int imageHeight) {

        //Create
        this.imgWidth = imageWidth;
        this.imgHeight = imageHeight;

        //Create Bitmap
        image = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
    }

    public Bitmap getImage() {
        return image;
    }


    //This is for local demo.
    public void updateImage() {
        pixels = breaker.getPixels();
        pixelUsed = new boolean[pixels.length];
        Arrays.fill(pixelUsed, false);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Pixel pix = getUnusedPixel();
                postPixel(image, pix);
                handler.postDelayed(runnable, 3);

            }
        };
        
        handler.postDelayed(runnable, 3);

    }

    //Currently adding randomly without checking if its been used.
    private Pixel getUnusedPixel() {
        Random random = new Random();
        boolean used;
        Pixel pix;
        do {
            int x = random.nextInt(breaker.getWidth());
            int y = random.nextInt(breaker.getHeight());
            used = pixelUsed[x*y];
            pix = new Pixel(x, y, pixels[x*y]);
        } while (used);

        return pix;
    }

    private void postPixel(Bitmap bitmap, Pixel pixel) {
        int x = pixel.getXPosition();
        int y = pixel.getYPosition();
        int pixIndex = (y*imgWidth) + x;
        bitmap.setPixel(x, y, pixels[pixIndex]);
    }




}
