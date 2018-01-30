package sb_3.pictureguesser;

import android.content.DialogInterface;
import android.content.Intent;
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

public class PlayActivity extends AppCompatActivity {

    private Button btnGuess;
    private EditText etGuess;
    private ImageView picGuess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        btnGuess = (Button) findViewById(R.id.btnSendGuess);
        etGuess = (EditText) findViewById(R.id.etGuess);
        picGuess = (ImageView) findViewById(R.id.imgGame);

        presentImage(picGuess);

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGuess(etGuess.getText().toString());
            }
        });
    }

    protected void sendGuess(String guess) {
        Log.i("PlayActivity", guess);

        if (etGuess.getText().toString().equals("cat")) {
            AlertDialog.Builder endBuilder = new AlertDialog.Builder(PlayActivity.this);
            endBuilder.setTitle("Game Over!");
            endBuilder.setMessage("You Won!");
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

    protected void presentImage(ImageView pic) {

        //testing
        String imageName = "cat.jpg";

        try {
            InputStream stream = getAssets().open(imageName);
            Drawable d = Drawable.createFromStream(stream,null);
            pic.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


