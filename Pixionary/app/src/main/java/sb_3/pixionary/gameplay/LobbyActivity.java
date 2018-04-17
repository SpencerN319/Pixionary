package sb_3.pixionary.gameplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;

public class LobbyActivity extends AppCompatActivity {

    public static final int START_GAME = 10;
    public static final int LEAVE_GAME = 11;
    private int gameType;
    private User user;
//    private String playlist;
    private ArrayList<String> players;
//    private ArrayList<String> chat;
//    private TextView lobbyName;
//    private TextView gameName;
    private TextView playerUpdate;
//    private ImageView previewImage;
//    private Button chatButton;
    private int numOfPlayers;
    private Button startButton;
    private Button leaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDataDBHandler db = new UserDataDBHandler(this);
        user = db.getUser("0");
        if (user.getUserType().equals("host")) {
            setContentView(R.layout.activity_host_lobby);
            startButton = (Button) findViewById(R.id.start_button);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyGame(START_GAME);
                }
            });
        } else {
            setContentView(R.layout.activity_player_lobby);
        }
        numOfPlayers = getIntent().getIntExtra("players", 0);
        leaveButton = (Button) findViewById(R.id.leave_button);
        // Chat button, and other stuff to still be decided if it will be implemented or not.
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyGame(LEAVE_GAME);
            }
        });

    }

    private void notifyGame(int command) {
        Intent intent = new Intent();
        intent.putExtra("command", command);
        setResult(RESULT_OK, intent);
        finish();
    }
    




}
