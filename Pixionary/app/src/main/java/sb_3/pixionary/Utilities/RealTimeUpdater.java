package sb_3.pixionary.Utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;

/**
 * Created by Steven Rein on 3/12/2018.
 */

//FIXME not sure if this is going to be needed.
public class RealTimeUpdater {

    private static final String TAG = RealTimeUpdater.class.getSimpleName();
    private String url = "ws://echo.websocket.org"; //TODO this will need to be changed.
    private URI uri;
    private WebSocketClient webSocketClient;
    private Context context;

    //Stuff for lobby
    private int gameID;
    private int gameType;
    private User user;
    private ArrayList<ShortUser> players;
    private ArrayList<String> chat;

    //Stuff for Play
    private Bitmap bitmap;
    private ArrayList<String> listOfOptions;
    private int numImages;

    //Stuff for lobby
    private TextView lobbyName;
    private TextView gameName;
    private TextView playerUpdate;
    private ImageView previewImage;

    //Stuff for Play
    private ListView guessList;
    private ImageView image;
    private TextView imagesRemaining;


    public RealTimeUpdater(Context context, int gameID, int gameType,  User user) {
        this.context = context;
        this.gameID = gameID;
        this.gameType = gameType;
        this.user = user;
        players = new ArrayList<>();
        chat = new ArrayList<>();

        lobbyName = (TextView) ((Activity)context).findViewById(R.id.tv_lobby_label);
        gameName = (TextView) ((Activity)context).findViewById(R.id.tv_game_name);
        playerUpdate = (TextView) ((Activity)context).findViewById(R.id.tv_player_update);
        previewImage = (ImageView) ((Activity)context).findViewById(R.id.lobby_image_preview);
        guessList = (ListView) ((Activity)context).findViewById(R.id.list_of_guesses);
        image = (ImageView) ((Activity)context).findViewById(R.id.imgGame);
        imagesRemaining = (TextView) ((Activity)context).findViewById(R.id.images_remaining);
    }

    public void connect() {
        try {
            uri = new URI(url);
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
                    ((Activity)context).runOnUiThread(new Runnable() {
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

                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        webSocketClient.close();
    }

    public void sendStart() {
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

    public void sendLeft() {
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

    //Message Reactions on the Lobby View
    private void initializeDisplay(JSONObject object) {
        try {
            lobbyName.setText(context.getString(R.string.lobby_dynamic, object.getString("host")));
            gameName.setText(context.getString(R.string.lobby_game_name_dynamic, object.getString("name")));
            //previewImage.setImageBitmap(getImage(object.getJSONObject("image"))); //TODO This also might not be needed for the start at least.
            //gameType = object.getInt("gameType"); //TODO Probably dont need this line.
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
            displayPlayerUpdate(0, username);
            players.add(new ShortUser(username, -1));
            if (players.size() == 1 && gameType == 0) {
                if (user.getUserType().equals("host")) {
                    sendStart();
                }
            } else if(players.size() == 2 && gameType == 1) {
                    startGame();
                if (user.getUserType().equals("host")) {
                    sendStart();
                }
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
        ((Activity)context).setContentView(R.layout.activity_play);
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
            playerUpdate.setText(context.getString(R.string.added_player, username));
        } else if (command == 1) {
            playerUpdate.setText(context.getString(R.string.removed_player, username));
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

    //Message Reactions on the Play View -- receiving the list of guesses and the first images dimensions.
    private void playInit(JSONObject jsonObject) {
        try {
            JSONArray jsonGuesses = jsonObject.getJSONArray("guessList");
            listOfOptions = new ArrayList<>();
            for (int i = 0; i < jsonGuesses.length(); i++) {
                listOfOptions.add(jsonGuesses.getString(i));
            }
            numImages = jsonObject.getInt("numberOfImages");
            imagesRemaining.setText(context.getString(R.string.remain_dynamic, numImages));
            int width = jsonObject.getInt("width");
            int height = jsonObject.getInt("height");
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            image.setImageBitmap(bitmap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //React to a new pixel
    private void receivePixel(JSONObject jsonObject) {
        try {
            int x = jsonObject.getInt("x");
            int y = jsonObject.getInt("y");
            int color = jsonObject.getInt("color");
            bitmap.setPixel(x, y, color);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGuess() {
        //Get the position on the listview, need to create the listview still.
        JSONObject object = new JSONObject();
        int position = 0;
        try {
            object.put("command", "guess");
            object.put("username", user.getUsername());
            object.put("value", listOfOptions.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocketClient.send(object.toString());

    }
}
