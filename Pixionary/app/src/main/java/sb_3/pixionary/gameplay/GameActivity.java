package sb_3.pixionary.gameplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

import SaveData.UserDataDBHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import sb_3.pixionary.Adapters.GuessListAdapter;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.RealTimeUpdater;

//TODO still need to add a view to see the players in the lobby. --- Not important RN.
public class GameActivity extends AppCompatActivity {

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
    private TextView lobbyName;
    private TextView gameName;
    private TextView playerUpdate;
    private ImageView previewImage;

    //Stuff for Play
    private Bitmap bitmap;
    private ArrayList<String> listOfOptions;
    private boolean listChanged = false;
    private int numImages;
    private Button sendGuess;
    private ImageView image;
    private ListView guessList;

    //Stuff for lobby
    private Button chatButton;
    private Button startButton;
    private Button leaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        UserDataDBHandler db = new UserDataDBHandler(this);
        User user = db.getUser("0");
        String playlistName = getIntent().getStringExtra("playlist");
        listOfOptions = new ArrayList<>();
        guessList = (ListView) findViewById(R.id.list_of_guesses);
        setContentView(R.layout.activity_play);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Sending Start", "");
                    sendStart();
                }
            });

        sendGuess = (Button) findViewById(R.id.btnSendGuess);
        sendGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //For lobby
        chatButton = (Button) findViewById(R.id.open_chat);
        leaveButton = (Button) findViewById(R.id.leave_button);
        image = (ImageView) findViewById(R.id.imgGame);
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

    private void messageReceived(String message) {
        Scanner scanner = new Scanner(message);
        String type = scanner.next();
        Log.i("Message Received", message);
        switch (type) {
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
                setWords();
                break;
            case "HEIGHT":
                setHeightAndWidth(message);
                break;
            case "px":
                addPixel(message);
                break;
            case "ROUNDEND": //FIXME Create something for wiping the Bitmap.
                wipeBitmap();
                break;
            default:
                Log.i("Not tracked", message);
        }
    }

    private void setWords() {
        GuessListAdapter guessListAdapter = new GuessListAdapter(context, listOfOptions);
        guessList.setAdapter(guessListAdapter);
    }

    private void wipeBitmap() {
        bitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
    }

    private void sendStart() {
        String message = "start," + gameID;
        webSocket.send(message);
    }

    private void addPixel(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("px")) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int color = scanner.nextInt();
            bitmap.setPixel(x, y, color);
            Log.i("Pixel Set", String.valueOf(color));
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

    private void setHeightAndWidth(String message) {
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

}
