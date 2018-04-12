package sb_3.pixionary.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by fastn on 3/27/2018.
 */
//TODO Move back to GameActivity and Create an AsyncTask for adding pixels to the Bitmap.
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Bitmap bitmapImage;

    public DownloadImageTask(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        bitmapImage = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmapImage = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmapImage;
    }

    public Bitmap getBitmap() {
        return bitmapImage;
    }
}