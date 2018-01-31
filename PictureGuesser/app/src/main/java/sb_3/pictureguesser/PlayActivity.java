package sb_3.pictureguesser;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import ImageBuilder.ImageCreator;

public class PlayActivity extends AppCompatActivity {

    private Button btnGuess;
    private EditText etGuess;
    private ImageView picGuess;
    private Bitmap image;
    private Button btnUpdate;

    //Temporary for testing
    private int tempwidth = 0;
    private int tempheight = 0;
    private int color = 0xFFACED21;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        btnGuess = (Button) findViewById(R.id.btnSendGuess);
        etGuess = (EditText) findViewById(R.id.etGuess);
        picGuess = (ImageView) findViewById(R.id.imgGame);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        presentImage(picGuess);

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGuess(etGuess.getText().toString());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempheight++;
                tempwidth++;
                image.setPixel(tempwidth, tempheight, color);
            }
        });
    }

    protected void sendGuess(String guess) {
        Log.i("PlayActivity", guess);

        if (etGuess.getText().toString().equals("blank")) {
            AlertDialog.Builder endBuilder = new AlertDialog.Builder(PlayActivity.this);
            endBuilder.setTitle("Game Over!");
            endBuilder.setMessage("You Won!");
            //Figure out how to make changes to
            //the look of the alert dialog.
            //endBuilder.setView();
            endBuilder.setPositiveButton("Exit to Main Menu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(PlayActivity.this, MainMenuActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            endBuilder.create();
            endBuilder.show();
        }
    }
    //TEST
//    protected void presentImage(ImageView pic) {
//
//        //testing
//        String imageName = "cat.jpg";
//
//        try {
//            InputStream stream = getAssets().open(imageName);
//            Drawable d = Drawable.createFromStream(stream,null);
//            pic.setImageDrawable(d);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void presentImage(ImageView pic) {

        ImageCreator creator = new ImageCreator(480, 900);
        image = creator.getImage();

        pic.setImageBitmap(image);
    }
}


