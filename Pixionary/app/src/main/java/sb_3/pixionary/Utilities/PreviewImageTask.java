package sb_3.pixionary.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by fastn on 3/27/2018.
 */

public class PreviewImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView image;
    private Bitmap bitmapImage;


    public PreviewImageTask(ImageView image) {
        this.image = image;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        bitmapImage = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmapImage = BitmapFactory.decodeStream(in);
            return bitmapImage;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }
}