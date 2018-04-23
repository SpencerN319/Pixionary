package sb_3.pixionary.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Random;

import sb_3.pixionary.ImageBuilder.Pixel;

/**
 * Created by fastn on 3/27/2018.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Void> {

    private ImageView image;
    private ImageView cover;
    private Bitmap bitmapCover;
    private Bitmap bitmapImage;
    private boolean firstPress = true;
    private int width;
    private int height;
    private boolean killMe = false;

    public DownloadImageTask(ImageView image, ImageView cover) {
        this.image = image;
        this.cover = cover;
    }

    protected Void doInBackground(String... urls) {
        String urldisplay = urls[0];
        bitmapImage = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream(); //TODO Not sure but didn't work?
            bitmapImage = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        width = bitmapImage.getWidth();
        height = bitmapImage.getHeight();
        bitmapCover = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmapCover.setPixel(x, y, 0xFFFFFFFF);
            }
        }
        publishProgress();
        while(true) {
            if (killMe) {
                break;
            }
            updateImage();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (!firstPress) {
            cover.invalidate();
        } else {
            cover.setImageBitmap(bitmapCover);
            image.setImageBitmap(bitmapImage);
        }
    }

    @Override
    protected void onCancelled() {
        image.setImageResource(android.R.color.transparent);
        cover.setImageResource(android.R.color.transparent);
        killMe = true;
        Log.i("TASK", "CANCELLED");
    }

    private void updateImage() {
        for (int i = 0; i < 100; i++) {
            Pixel pixel = getPixel(width, height);
            bitmapCover.setPixel(pixel.getXPosition(), pixel.getYPosition(), pixel.getColor());
        }
        publishProgress();
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
            onCancelled();
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