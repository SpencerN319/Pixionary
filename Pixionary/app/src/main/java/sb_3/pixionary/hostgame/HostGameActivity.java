package sb_3.pixionary.hostgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sb_3.pixionary.R;

public class HostGameActivity extends AppCompatActivity {

    private static final String TAG = HostGameActivity.class.getSimpleName();
    private Context context;
    private Activity activity;
    private int GETCATEGORYID = 5;

    private String nameGame;
    private String gameID;
    private static final String url = ""; //Declare some url here.


    private EditText nameET;
    private Button categorySelection;
    private Button playAI;
    private Button play1v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);
        context = HostGameActivity.this;


        //Buttons for the activity.
        nameET = (EditText) findViewById(R.id.et_game_name);
        categorySelection = (Button) findViewById(R.id.button_category);
        playAI = (Button) findViewById(R.id.button_play_ai);
        play1v1 = (Button) findViewById(R.id.button_play_1v1);

        categorySelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CategoriesSelectActivity
                Intent i = new Intent(HostGameActivity.this, CategoriesSelectActivity.class);
                startActivityForResult(i, GETCATEGORYID);
            }
        });

    }

}
