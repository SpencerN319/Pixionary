package sb_3.pixionary.hostgame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.Playlist;
import sb_3.pixionary.Utilities.POJO.GameClasses.ShortGame;
import sb_3.pixionary.Utilities.PreviewImageTask;
import sb_3.pixionary.gameplay.GameActivity;

public class HostGameActivity extends AppCompatActivity {

    private static final String TAG = HostGameActivity.class.getSimpleName();
    private static final String HOSTGAME_URL = "http://proj-309-sb-3.cs.iastate.edu:80/somethingToDoWithLeaderboards"; //TODO set actual URL
    private int GETPLAYLISTID = 5;

    private Context context;

    private TextView tvPlaylistSelected;
    private EditText etNumOfPlayers;
    private EditText etNumOfRounds;
    private EditText etDifficulty;
    private Button playlistSelection;
    private Button playAI;
    private Button playMultiplayer;
    private ImageView previewImage;

    private ShortGame accessGame;
    private String playlistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);
        context = HostGameActivity.this;

        tvPlaylistSelected = (TextView) findViewById(R.id.tv_playlist);
        etNumOfPlayers = (EditText) findViewById(R.id.et_num_players);
        etNumOfRounds = (EditText) findViewById(R.id.et_rounds);
        etDifficulty = (EditText) findViewById(R.id.et_difficulty);
        playlistSelection = (Button) findViewById(R.id.button_category);
        playAI = (Button) findViewById(R.id.button_play_ai);
        playMultiplayer = (Button) findViewById(R.id.button_play_multiplayer);
        previewImage = (ImageView) findViewById(R.id.image_preview);

        accessGame = new ShortGame();
        accessGame.setHost(getUsernameFromExtra());

        playlistSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start PlaylistSelectActivity
                Intent i = new Intent(HostGameActivity.this, PlaylistSelectActivity.class);
                startActivityForResult(i, GETPLAYLISTID);
            }
        });

        playAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessGame.setPlayers(1);
                directToGame(accessGame.getPlayers());
            }
        });

        playMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessGame.setPlayers(2);
                directToGame(accessGame.getPlayers());
            }
        });

    }

    /**
     * Receives data from the Playlist Select Activity to be used in determining which playlist to start a game for.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GETPLAYLISTID) {
            playlistName = data.getStringExtra("PlaylistName");
            String imageURL = "http://proj-309-sb-3.cs.iastate.edu/" + data.getStringExtra("ImagePreview");
            Log.i(TAG, imageURL);
            tvPlaylistSelected.setText(getString(R.string.playlist_dynamic, playlistName));

            Log.i("DEBUG:" + TAG, "PlaylistID = " + data.getIntExtra("PlaylistID", -1));

            Playlist playlist = new Playlist(playlistName);
            accessGame.setPlaylist(playlist);

            //Image Stuff
            PreviewImageTask imageTask = new PreviewImageTask(previewImage);
            imageTask.execute(imageURL);
        }
    }

    private String getUsernameFromExtra() {
        return getIntent().getStringExtra("username");
    }

    /**
     * Used to start the next activity, bundles all the necessary data and selects the activity based
     * off of the game type.
     * @param gameType used to determine what activity is next.
     */
    private void directToGame(int gameType) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("id", accessGame.getHost());
        int players = 0;
        int rounds = 0;
        int difficulty = 0;
        if (!etNumOfPlayers.getText().toString().matches("")) {
            players = Integer.valueOf(etNumOfPlayers.getText().toString());
        }
        if (!etNumOfRounds.getText().toString().matches("")) {
            rounds = Integer.valueOf(etNumOfRounds.getText().toString());
        }
        if (!etDifficulty.getText().toString().matches("")) {
            difficulty = Integer.valueOf(etDifficulty.getText().toString());
        }
        boolean playersCheck = (players > 1);
        boolean roundsCheck = ((rounds > 0) && (rounds <= 10));
        boolean difficultyCheck = ((difficulty > 0) && (difficulty <= 10));
        if (playlistName != null ) {
            if (gameType == 1 && roundsCheck && difficultyCheck) {
                players = 1;
                intent.putExtra("players", players);
                intent.putExtra("playlist", playlistName);
                intent.putExtra("rounds", rounds);
                intent.putExtra("bot_difficulty", difficulty);
                startActivity(intent);
                finish();
            } else if (gameType == 2 && roundsCheck && playersCheck) {
                intent.putExtra("players", players);
                intent.putExtra("playlist", playlistName);
                intent.putExtra("rounds", rounds);
                startActivity(intent);
                finish();
            }
        }
    }
}
