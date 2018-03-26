package sb_3.pixionary.Utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import sb_3.pixionary.Adapters.GuessListAdapter;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.Playlist;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;

/**
 * Created by fastn on 3/25/2018.
 */

public class OkHttpRealTime {

    private static final String TAG = RealTimeUpdater.class.getSimpleName();
    private String url = "ws://proj-309-sb-3.cs.iastate.edu:8080/name"; //TODO this will need to be changed.
    private OkHttpClient okHttpClient;
    private WebSocket webSocket;
    private Context context;

    //Stuff for lobby
    private int gameID = -1;
    private int gameType;
    private User user;
    private String playlist;
    private ArrayList<ShortUser> players;
    private ArrayList<String> chat;

    //Stuff for Play
    private Bitmap bitmap;
    private ArrayList<String> listOfOptions;
    private boolean listChanged = false;
    private int numImages;

    //Stuff for lobby
    private TextView lobbyName;
    private TextView gameName;
    private TextView playerUpdate;
    private ImageView previewImage;

    //Stuff for Play
    private ListView guessList;
    private TextView imagesRemaining;


    public OkHttpRealTime(Context context, String playlist, User user, ListView guessList) {
        this.context = context;
        this.playlist = playlist;
        this.user = user;
        this.guessList = guessList;
        players = new ArrayList<>();
        chat = new ArrayList<>();
        listOfOptions = new ArrayList<>();
        lobbyName = (TextView) ((Activity)context).findViewById(R.id.tv_lobby_label);
        gameName = (TextView) ((Activity)context).findViewById(R.id.tv_game_name);
        playerUpdate = (TextView) ((Activity)context).findViewById(R.id.tv_player_update);
        previewImage = (ImageView) ((Activity)context).findViewById(R.id.lobby_image_preview);
        imagesRemaining = (TextView) ((Activity)context).findViewById(R.id.images_remaining);
    }

    public void connect() {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {

                if (user.getUserType().equals("host")) {
                    String message = "create," + user.getUsername() + "," + playlist;
                    webSocket.send(message);
                } else {
                    String message = "join" + gameID;
                    webSocket.send(message);
                }
//                if (response.isSuccessful()) {
//                    createGameReaction(response.message());
//                }
                Log.i("Response:", response.message());
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                final String message = text;
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageReceived(message);
                    }
                });
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                Log.i("Closing", reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.i("Error", t.getMessage());
            }
        };
        webSocket = okHttpClient.newWebSocket(request, webSocketListener);
    }

    public void sendMessage(String msg) {
        webSocket.send(msg);
        Log.i("Sending Message", msg);
    }


    public void close() {
        webSocket.close(1000, "Method Call.");
    }


//FIXME Need to check if there are any other methods to include.
    public void sendLeft() {
        try {
            JSONObject leftGame = new JSONObject();
            leftGame.put("Command", "Left");
            leftGame.put("username", user.getUsername());
            webSocket.send(leftGame.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendGuess(int position) {
        //Get the position on the listview, need to create the listview still.
//        JSONObject object = new JSONObject();
//        try {
//            object.put("command", "guess");
//            object.put("username", user.getUsername());
//            object.put("value", listOfOptions.get(position));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String message = "guess " + listOfOptions.get(position);
        webSocket.send(message);
    }

    public ArrayList<String> getListOfOptions() {
        return listOfOptions;
    }

    private void messageReceived(String message) {
//        try {
            Scanner scanner = new Scanner(message);
            String type = scanner.next();
            Log.i("Message Received", message);
            switch (type) {
//                case "lobby_init":
//                    initializeDisplay(object);
//                    break;
//                case "joined":
//                    addPlayer(object);
//                    break;
//                case "left":
//                    removePlayer(object);
//                    break;
//                case "play_init":
//                    playInit(object);
//                    break;
//                case "next_image":
//                    nextImage(object);
//                    break;
//                case "pixel":
//                    receivePixel(object);
//                    break;
//                case "guess_result":
//                    guessResult();
//                    break;
//                case "end_game":
//                    endGame(object);
//                    break;
                case "Creating":
                    createGameReaction(message);
                    break;
                case "START":
                    ((Activity)context).setContentView(R.layout.activity_play);
                    break;
                case "WORD":
                    addWord(message);
                    break;
                case "ROUNDBEGIN":
//                    setWords();
                    break;
                case "HEIGHT":
                    setHeightAndWidth(message);
                    break;
                case "px":
                    addPixel(message);
                    break;
                case "ROUNDEND":
                    //wipeBitmap();
                    break;
                default:
                    Log.i("Not tracked", message);
            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void addPixel(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("px")) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int color = scanner.nextInt();
            bitmap.setPixel(x, y, color);
        }
    }

    private void createGameReaction(String message) {
        Scanner scanner = new Scanner(message);
        if(scanner.next().equals("Creating")) {
            gameID = scanner.nextInt();
            Log.i("GameID", String.valueOf(gameID));
        }
    }

    private void addWord(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("WORD") && scanner.hasNext()) {
            String word = scanner.next();
            Log.i("Word Read", word);
            listOfOptions.add(word);
            listChanged = true;
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private void setWords() {
        //TODO DON'T WORRY FOR NOW!
    }

    public boolean getListChanged() {
        return listChanged;
    }

    private void setHeightAndWidth(String message) {
//        GuessListAdapter guessListAdapter = new GuessListAdapter(context, listOfOptions);
//        guessList.setAdapter(guessListAdapter);
        Scanner scanner = new Scanner(message);
        int height = 0;
        int width = 0;
        String command = scanner.next();
        if (command.equals("HEIGHT") && scanner.hasNextInt()) {
            height = scanner.nextInt();
            Log.i("Height:", String.valueOf(height));
            command = scanner.next();
        }
        if (command.equals("WIDTH") && scanner.hasNextInt()) {
            width = scanner.nextInt();
            Log.i("Width:", String.valueOf(width));
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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
               // startGame();
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
            GuessListAdapter guessListAdapter = new GuessListAdapter(context, listOfOptions);
            guessList.setAdapter(guessListAdapter);
            numImages = jsonObject.getInt("numberOfImages");
            imagesRemaining.setText(context.getString(R.string.remain_dynamic, numImages));
            int width = jsonObject.getInt("width");
            int height = jsonObject.getInt("height");
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void nextImage(JSONObject jsonObject) {
        try {
            numImages--;
            imagesRemaining.setText(context.getString(R.string.remain_dynamic, numImages));
            int width = jsonObject.getInt("width");
            int height = jsonObject.getInt("height");
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void endGame(JSONObject jsonObject) {
        //TODO create something to display points here and end the game.
        try {
            JSONArray playersAndScores = jsonObject.getJSONArray("game_result");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO create a view to display the result of the game
    }

    public void finishGame() {
        //TODO for the button to leave after seeing the results.
        ((Activity)context).finish();
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



    private void guessResult() {
        //TODO did you guess right? or WRONG?
    }



}
