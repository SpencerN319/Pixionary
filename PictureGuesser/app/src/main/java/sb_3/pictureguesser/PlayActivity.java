package sb_3.pictureguesser;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.Snackbar;
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

    private EditText guess;
    private Button sendGuess;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        guess = (EditText) findViewById(R.id.etGuess);
        sendGuess = (Button) findViewById(R.id.btnGuess);
        picture = (ImageView) findViewById(R.id.pixImage);

        presentImage(picture);

        sendGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitGuess(guess.getText().toString(), view);
            }
        });

    }

    //Will be used to submit a guess to the server, the server will then return the result.
    protected void submitGuess(String guess, View view)
    {
        //Send string to the log. ---Debugging
        Log.i("PlayActivity",guess);
        if (guess == "cat")
        {
            Snackbar.make(view, "You got it right!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void presentImage(ImageView pic)
    {
        String imageName = "cat";
        try {
            InputStream input = getAssets().open(imageName + ".png");
            Drawable d = Drawable.createFromStream(input, null);
            pic.setImageDrawable(d);
        }catch (IOException b) {
            b.printStackTrace();
        }

    }
}
