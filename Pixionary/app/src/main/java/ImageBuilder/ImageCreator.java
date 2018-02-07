package ImageBuilder;

import android.content.Context;
import android.graphics.Bitmap;

import android.os.Handler;

import java.util.Arrays;
import java.util.Random;

import Client.BuildImageThread;
import Client.ReceivePixelThread;


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

    //Local Image creator
    public ImageCreator(Context context, String[] imageNames, int index) {

        //Creates a instance of class ImageBreakdown to get pixel array, move to server-side.
        breaker = new ImageBreakdown();
        breaker.breakDownImage(context, imageNames[index]);

        //Set width and height.
        imgWidth = breaker.getWidth();
        imgHeight = breaker.getHeight();

        //Initializes blank mutable Bitmap
        image = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);

    }

    //Getting data from server.
    public ImageCreator(Context context, BuildImageThread builder) {

        //Set width and height
        imgWidth = builder.getImgWidth();
        imgHeight = builder.getImgHeight();

        //Create Bitmap
        image = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
    }

    public Bitmap getImage() {
        return image;
    }


    //Normally need to pass a Pixel in. --to receive from server-side.
    public void updateImageLocal() {
        pixels = breaker.getPixels();
        pixelUsed = new boolean[pixels.length];
        Arrays.fill(pixelUsed, false);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                postPixel(image, getUnusedPixel());
                handler.postDelayed(this, 3);
            }
        };
        handler.postDelayed(runnable, 3);

    }

    //Receiving Pixels from server
    public void updateImage(ReceivePixelThread updater) {
        updater.run();
        if (updater.isReceived()) {
            postPixel(image, updater.getNewPixel());
        }
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

    public void postPixel(Bitmap bitmap, Pixel pixel) {
        int x = pixel.getXPosition();
        int y = pixel.getYPosition();
        int pixIndex = (y*imgWidth) + x;
        bitmap.setPixel(x, y, pixels[pixIndex]);
    }




}
