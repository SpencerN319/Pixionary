package sb_3.pixionary.hostgame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.Playlist;
import sb_3.pixionary.Utilities.POJO.GameClasses.ShortGame;
import sb_3.pixionary.gameplay.GameActivity;

public class HostGameActivity extends AppCompatActivity {

    private static final String TAG = HostGameActivity.class.getSimpleName();
    private static final String HOSTGAME_URL = "http://proj-309-sb-3.cs.iastate.edu:80/somethingToDoWithLeaderboards"; //TODO set actual URL
    private int GETPLAYLISTID = 5;

    private Context context;
    private RequestQueue requestQueue;

    private TextView tvPlaylistSelected;
    private TextView tvCreatorSelected;
    private EditText etName;
    private Button playlistSelection;
    private Button playAI;
    private Button play1v1;

    private ShortGame accessGame;
    private String playlistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);
        context = HostGameActivity.this;
        requestQueue = Volley.newRequestQueue(this);

        tvPlaylistSelected = (TextView) findViewById(R.id.tv_playlist);
        tvCreatorSelected = (TextView) findViewById(R.id.tv_creator);
        etName = (EditText) findViewById(R.id.et_game_name);
        playlistSelection = (Button) findViewById(R.id.button_category);
        playAI = (Button) findViewById(R.id.button_play_ai);
        play1v1 = (Button) findViewById(R.id.button_play_1v1);

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
                accessGame.setGameName(etName.getText().toString());
                accessGame.setGameTypeAI();
                accessGame.setIDRequest();
                //FIXME requestGame(); Just uncomment and remove Test() when server is ready.
                requestGameTest();
            }
        });

        play1v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessGame.setGameName(etName.getText().toString());
                accessGame.setGameType1V1();
                accessGame.setIDRequest();
                //FIXME requestGame(); Just uncomment and remove Test() when server is ready.
                requestGameTest();
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
            tvPlaylistSelected.setText(getString(R.string.playlist_dynamic, playlistName));

            Log.i("DEBUG:" + TAG, "PlaylistID = " + data.getIntExtra("PlaylistID", -1));

            Playlist playlist = new Playlist(playlistName);
            accessGame.setPlaylist(playlist);
        }
    }

    private String getUsernameFromExtra() {
        return getIntent().getStringExtra("username");
    }

    /**
     * This method is to make the Volley Request to the server to start the game then direct to the
     * next screen.
     */
    private void requestGame() {
        JsonObjectRequest gameRequest = new JsonObjectRequest(Request.Method.POST,
                HOSTGAME_URL, accessGame.createJson(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    directToGame(response.getInt("id"), response.getInt("type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(gameRequest);
    }

    //FIXME Temporary for testing
    private void requestGameTest() {
        directToGame(100, 0);
    }

    /**
     * Used to start the next activity, bundles all the necessary data and selects the activity based
     * off of the game type.
     * @param gameID used to connect to the Game in next activity.
     * @param gameType used to determine what activity is next.
     */
    private void directToGame(int gameID, int gameType) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("gameId", gameID);
        intent.putExtra("gameType", gameType);
        intent.putExtra("playlist", playlistName);
        startActivity(intent);
        finish();
    }

}
