package sb_3.pixionary.gameplay;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.net.URL;
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
import sb_3.pixionary.Utilities.DownloadImageTask;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.interfaces.DataTransferInterface;

//TODO still need to add a view to see the players in the lobby. --- Not important RN.
public class GameActivity extends AppCompatActivity implements DataTransferInterface {

    private static final String TAG = GameActivity.class.getSimpleName();
    public static final int START_GAME_REQUEST = 7;
    private String url = "ws://proj-309-sb-3.cs.iastate.edu:8080/name";
    private Context context;
    private OkHttpClient okHttpClient;
    private WebSocket webSocket;
    private DataTransferInterface dataTransferInterface;
    private DownloadImageTask downloadImageTask;

    private int gameID;
    private int gameType;
    private String playlistName;
    private User user;
    private ArrayList<String> listOfOptions;
    private GuessListAdapter adapter;
    private String currentGuess;

    private Button sendGuess;
    private ImageView image;
    private ImageView cover;
    private ListView guessList;

    //Image stuff
    int width;
    int height;
    int[] pixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        context = this;
        dataTransferInterface = this;

        UserDataDBHandler userDataDBHandler = new UserDataDBHandler(context);
        user = userDataDBHandler.getUser("0");

        gameID = getIntent().getIntExtra("gameId", -1);
        gameType = getIntent().getIntExtra("gameType", -1);
        playlistName = getIntent().getStringExtra("playlist");

        guessList = (ListView) findViewById(R.id.list_of_guesses);
        image = (ImageView) findViewById(R.id.imgGame);
        cover = (ImageView) findViewById(R.id.imgCover);
        listOfOptions = new ArrayList<>();
        adapter = new GuessListAdapter(context, listOfOptions, dataTransferInterface);
        guessList.setAdapter(adapter);

        sendGuess = (Button) findViewById(R.id.btnSendGuess);
        sendGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGuess(currentGuess);
            }
        });

        connect();

        //FIXME
        if (gameType >= 0) {
            directToLobby(gameType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (data.getIntExtra("command", -1)) {
            case LobbyActivity.START_GAME:
                sendStart();
                break;
            case LobbyActivity.LEAVE_GAME:
                webSocket.close(0, "Left Lobby");
                finish();
                break;
            case -1:
                Log.i(TAG, "Error from the Lobby");
        }
    }

    @Override
    public void setValuesAndReact(int position) {
        currentGuess = listOfOptions.get(position);
        Log.i("Current Guess", currentGuess);
    }

    private void directToLobby(int gameType) {
        Intent i = new Intent(this, LobbyActivity.class);
        i.putExtra("gameType", gameType);
        startActivityForResult(i, START_GAME_REQUEST);
    }

    public void connect() {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.i(TAG, "Opened Method");
                if (user.getUserType().equals("host")) {
                    createGame();
                } else {
                    joinGame();
                }
                Log.i("Response:", response.message());
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                final String message = text;
                runOnUiThread(new Runnable() {
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

    private void createGame() {
        String message = "create," + user.getUsername() + "," + playlistName;
        webSocket.send(message);
    }

    private void joinGame() {
        //FIXME When join game is called, need to have the name of the host sent over from the GameBrowserActivity.
        String message = "join," + "username of host" + "," + user.getUsername();
        webSocket.send(message);
    }

    private void sendStart() {
        String message = "start," + user.getUsername();
        webSocket.send(message);
    }

    private void sendGuess(String guess) {
        String message = "guess," + guess;
        webSocket.send(message);
    }

    private void messageReceived(String message) {
        Scanner scanner = new Scanner(message);
        String type = scanner.next();
        Log.i("Message Received", message);
        switch (type) {
            case "Creating":
//                createGameReaction(message);
                //TODO Not sure what to here other than a check to make sure that the game is created.
                Log.i(TAG, message);
                break;
            case "START":
                //TODO Need to get something here to close the lobby.
                Log.i(TAG, message);
                break;
            case "ROUNDBEGIN":
                Log.i(TAG, "Do something with this?");
                break;
            case "WORD":
                addWord(message);
                Log.i(TAG, message);
                break;
            case "HEIGHT":
                setHeightAndWidth(message);
                Log.i(TAG, message);
                break;
            case "URL":
                receiveImage(message);
                Log.i(TAG, message);
                break;
            case "CORRECT!":
                startGuessResponse("Correct!");
                Log.i(TAG, message);
                break;
            case "INCORRECT!":
                startGuessResponse("Incorrect!");
                Log.i(TAG, message);
                break;
            case "ROUNDEND":
                wipeBitmap();
                Log.i(TAG, message);
                break;
            case "SCOREUPDATE":
                displayScores(message);
                Log.i(TAG, message);
            default:
                Log.i(TAG, "NOT TRACKED: " +  message);
        }
    }

    private void createGameReaction(String message) {
        Scanner scanner = new Scanner(message);
        if(scanner.next().equals("Creating")) {
            gameID = scanner.nextInt();
            Log.i("GameID", String.valueOf(gameID));
            sendStart();
        }
    }

    private void addWord(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("WORD") && scanner.hasNext()) {
            String word = scanner.next();
            Log.i("Word Read", word);
            listOfOptions.add(word);
            adapter.notifyDataSetChanged();
        }
    }

    private void setHeightAndWidth(String message) {
        Scanner scanner = new Scanner(message);
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
        pixels = new int[width*height];
        Log.i("Pixels Length", String.valueOf(pixels.length));
    }

    private void receiveImage(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("URL")) {
            String url = scanner.next();
            DownloadImageTask imageTask = new DownloadImageTask(image, cover);
            imageTask.execute(url);
        }
    }

    private void startGuessResponse(String response) {
        Intent intent = new Intent(this, GuessResponseActivity.class);
        intent.putExtra("response", response);
        startActivity(intent);
    }

    private void wipeBitmap() {
        downloadImageTask.cancel(true);
    }

    private void displayScores(String message) {
        //TODO Read the usernames and the scores.
    }



}
