package sb_3.pixionary.Utilities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.util.Random;

import sb_3.pixionary.ImageBuilder.Pixel;

/**
 * Created by fastn on 3/30/2018.
 */

public class ChangeImageTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = ChangeImageTask.class.getSimpleName();
    private Activity activity;
    private Bitmap cover;
    private int width;
    private int height;

    public ChangeImageTask(Activity activity, Bitmap cover, int width, int height) {
        this.activity = activity;
        this.cover = cover;
        this.width = width;
        this.height = height;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i(TAG, "Started ChangeImageTask");
        for (int i = 0; i < 100; i++) {
            Pixel pixel = getPixel(width, height);
            cover.setPixel(pixel.getXPosition(), pixel.getYPosition(), pixel.getColor());
        }
    }

    private Pixel getPixel(int width, int height) {
        Random random = new Random();
        Pixel pix;
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        pix = new Pixel(x, y, 0);
        return pix;
    }
}
