package sb_3.pixionary.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

import sb_3.pixionary.ImageBuilder.Pixel;

/**
 * Created by fastn on 3/27/2018.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {


    int width;
    int height;
    int[] pixels;
    Bitmap bitmap;
    ImageView image;

    Handler handler;
    Runnable runnable;

    public DownloadImageTask(ImageView image) {
        this.image = image;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        width = result.getWidth();
        height = result.getHeight();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image.setImageBitmap(bitmap);
        pixels = new int[width * height];
        result.getPixels(pixels, 0, width, 0, 0, width, height);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Pixel pix = getPixel(width, height);
                bitmap.setPixel(pix.getXPosition(), pix.getYPosition(), pix.getColor());
                handler.postDelayed(runnable, 1);
            }
        };
        handler.postDelayed(runnable, 1);
    }


    private Pixel getPixel(int width, int height) {
        Random random = new Random();
        Pixel pix;
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int color = pixels[(y*width) + x];
        pix = new Pixel(x, y, color);
        return pix;
    }

    private void postPixel(Bitmap bitmap, Pixel pixel) {
        int x = pixel.getXPosition();
        int y = pixel.getYPosition();
        bitmap.setPixel(x, y, pixel.getColor());
    }

}