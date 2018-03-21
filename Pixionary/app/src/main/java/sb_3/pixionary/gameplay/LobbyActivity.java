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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;
//TODO still need to add a view to see the players in the lobby.
public class LobbyActivity extends AppCompatActivity {

    private static final String TAG = LobbyActivity.class.getSimpleName();

    private Context context;
    private TextView lobbyName;
    private TextView gameName;
    private TextView playerUpdate;
    private ImageView previewImage;
    private Button chatButton;
    private Button startButton;
    private Button leaveButton;

    private int gameID;
    private int gameType;
    private User user;
    private ArrayList<ShortUser> players;
    private ArrayList<String> chat;
    WebSocketClient webSocketClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDataDBHandler db = new UserDataDBHandler(this);
        user = db.getUser("0");
        if(user.getUserType().equals("player")) {
            setContentView(R.layout.activity_player_lobby);
        } else {
            setContentView(R.layout.activity_host_lobby);
            startButton = (Button) findViewById(R.id.start_button);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendStart();
                    startGame();
                }
            });
        }

        context = this;
        gameID = getIntent().getIntExtra("gameId", -1);
        gameType = getIntent().getIntExtra("gameType", -1);
        players = new ArrayList<>();
        connectWebSocket();


        lobbyName = (TextView) findViewById(R.id.tv_lobby_label);
        gameName = (TextView) findViewById(R.id.tv_game_name);
        playerUpdate = (TextView) findViewById(R.id.tv_player_update);
        previewImage = (ImageView) findViewById(R.id.lobby_image_preview);
        chatButton = (Button) findViewById(R.id.open_chat);
        leaveButton = (Button) findViewById(R.id.leave_button);

        chatButton.setEnabled(false);

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLeft();
                webSocketClient.close();
                finish();
            }
        });
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://echo.websocket.org"); //TODO this will need to be changed.
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.i(TAG, "opened");
                    JSONObject object = new JSONObject();
                    try {
                        object.put("access", gameID);
                        object.put("username", user.getUsername());
                        webSocketClient.send(object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onMessage(String s) {
                    final String message = s;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageReceived(message);
                        }
                    });
                }
                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.i(TAG, "Closed" + s);
                    sendLeft();
                }
                @Override
                public void onError(Exception e) {
                    Log.i(TAG, "Error " + e.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void sendStart() {
        try {
            JSONObject startGame = new JSONObject();
            startGame.put("command", "start");
            JSONArray thesePlayers = new JSONArray(players);
            startGame.put("players", thesePlayers);
            webSocketClient.send(startGame.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendLeft() {
        try {
            JSONObject leftGame = new JSONObject();
            leftGame.put("Command", "Left");
            leftGame.put("username", user.getUsername());
            webSocketClient.send(leftGame.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void messageReceived(String message) {
        try {
            JSONObject object = new JSONObject(message);
            String objectType = object.getString("type");
            switch (objectType) {
                case "init":
                    initializeDisplay(object);
                case "joined":
                    addPlayer(object);
                case "left":
                    removePlayer(object);
                default:
                    Log.i("Not tracked", message);
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeDisplay(JSONObject object) {
        try {
            lobbyName.setText(getString(R.string.lobby_dynamic, object.getString("host")));
            gameName.setText(getString(R.string.lobby_game_name_dynamic, object.getString("name")));
            previewImage.setImageBitmap(getImage(object.getJSONObject("image")));
            gameType = object.getInt("gameType");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImage(JSONObject object) {
        try {
            int width = object.getInt("width");
            int height = object.getInt("height");
            JSONArray array = object.getJSONArray("pixels");
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    bitmap.setPixel(i, j, array.getInt(j*width + i));
                }
            }
            return bitmap;
        } catch (JSONException e) {
            return null;
        }
    }

    private void addPlayer(JSONObject object) {
        try {
            String username = object.getString("username");
            int score = object.getInt("score");
            displayPlayerUpdate(0, username);
            players.add(new ShortUser(username, score));
            if (players.size() == 1 && gameType == 0) {
                sendStart();
                startGame();
            } else if(players.size() == 2 && gameType == 1) {
                sendStart();
                startGame();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void removePlayer(JSONObject object) {
        try {
            String username = object.getString("username");
            displayPlayerUpdate(1, username);
            int i = 0;
            while(true) {
                if (username.equals(players.get(i).getUsername())) {
                    players.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra("gameId", gameID);
        intent.putExtra("gameType", gameType);
        startActivity(intent);
        finish();
    }

    /**
     * This method is used to show when a person joins or leaves the lobby.
     * @param command adding player=0 removed player=1
     * @param username username to display
     */
    private void displayPlayerUpdate(int command, String username) {
        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(2000);
        if (command == 0) {
            playerUpdate.setText(getString(R.string.added_player, username));
        } else if (command == 1) {
            playerUpdate.setText(getString(R.string.removed_player, username));
        }
        playerUpdate.startAnimation(out);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playerUpdate.setText("");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Nothing
            }
        });
    }

}
