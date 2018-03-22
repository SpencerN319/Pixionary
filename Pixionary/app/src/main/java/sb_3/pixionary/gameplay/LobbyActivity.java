package sb_3.pixionary.gameplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.RealTimeUpdater;

//TODO still need to add a view to see the players in the lobby. --- Not important RN.
public class LobbyActivity extends AppCompatActivity {

    private static final String TAG = LobbyActivity.class.getSimpleName();
    private Context context;

    //Stuff for lobby
    private Button chatButton;
    private Button startButton;
    private Button leaveButton;

    //Stuff for Play
    private Button sendGuess;
    private ListView guessList;

    private RealTimeUpdater realTimeUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        UserDataDBHandler db = new UserDataDBHandler(this);
        User user = db.getUser("0");
        int gameID = getIntent().getIntExtra("gameId", -1);
        int gameType = getIntent().getIntExtra("gameType", -1);

        realTimeUpdater = new RealTimeUpdater(context, gameID, gameType, user);
        realTimeUpdater.connect();
        if(gameType == 0) {
            setContentView(R.layout.activity_play);
        } else if (user.getUserType().equals("host")) {
            setContentView(R.layout.activity_host_lobby);
            startButton = (Button) findViewById(R.id.start_button);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realTimeUpdater.sendStart();
                    setContentView(R.layout.activity_play);
                }
            });
        } else {
            setContentView(R.layout.activity_player_lobby);
        }

        //For lobby
        chatButton = (Button) findViewById(R.id.open_chat);
        leaveButton = (Button) findViewById(R.id.leave_button);

        //For play
        guessList = (ListView) findViewById(R.id.list_of_guesses);

        if (gameType != 0) {
            chatButton.setEnabled(false);
            leaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realTimeUpdater.close();
                    finish();
                }
            });
        } else {
            guessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    realTimeUpdater.sendGuess(position);
                }
            });
        }
        //May need to create a listener to do what is doing above when we enter a 1v1.
    }

}
