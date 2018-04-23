package sb_3.pixionary.gameplay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.Adapters.LobbyAdapter;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;


//TODO -- NEED TO GET INFORMATION FROM GameBrowserActivity ABOUT HOW MANY PLAYERS REQUIRED AND HOW MANY CURRENTLY WAITING.
public class LobbyActivity extends AppCompatActivity {

    public static final int START_GAME = 10;
    public static final int LEAVE_GAME = 11;
    private Context context;
    private LobbyReceiver lobbyReceiver = new LobbyReceiver();
    private User user;
    private ArrayList<String> players = new ArrayList<>();
    private int playersRequested;
    private LobbyAdapter adapter;

    private TextView playerUpdate;
    private ListView playerList;
    private Button startButton;
    private Button leaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
        players.add(user.getUsername());
        playersRequested = getIntent().getIntExtra("players", 0);
        playerUpdate = (TextView) findViewById(R.id.tv_player_update);
        playerList = (ListView) findViewById(R.id.player_list);
        leaveButton = (Button) findViewById(R.id.leave_button);
        // Chat button, and other stuff to still be decided if it will be implemented or not.
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyGame(LEAVE_GAME);
            }
        });

        adapter = new LobbyAdapter(context, R.layout.lobby_layout, players);
        playerList.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LOBBY_MESSAGE");
        registerReceiver(lobbyReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(lobbyReceiver);
    }

    private void notifyGame(int command) {
        Intent intent = new Intent();
        intent.putExtra("command", command);
        setResult(RESULT_OK, intent);
        finish();
    }

    public class LobbyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Scanner scanner = new Scanner(intent.getStringExtra("MESSAGE"));
            String command = scanner.next();
            switch (command) {
                case "newmember":
                    playerJoined(scanner.next());
                    break;
                case "Currentplayers":
                    players = new ArrayList<>();
                    break;
                case "Player:":
                    players.add(scanner.next());
                    break;
                case "Endplayers":
                    adapter = new LobbyAdapter(context, R.layout.lobby_layout, players);
                    playerList.setAdapter(adapter);
                    break;
                case "START":
                    closeToStart();
                    break;
                case "NONEWGAME":
                    finish();
                    break;
            }
        }
    }

    private void playerJoined(String username) {
        playerUpdate.setText(username + " joined the Game Lobby");
        players.add(username);
        adapter.notifyDataSetChanged();
    }

    private void closeToStart() {
        finish();
    }
    




}
