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
import sb_3.pixionary.Utilities.POJO.GameClasses.Bot;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.interfaces.DataTransferInterface;
import sb_3.pixionary.joingame.GameBrowserActivity;

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

    private String gameID; //This is just the host name
    private int playersRequested;
    private String playlistName;
    private User user;
    private ArrayList<String> listOfOptions;
    private ArrayList<String> currentScores;
    private GuessListAdapter adapter;
    private String currentGuess;
    private int difficulty;
    private int rounds;
    public Bot bot;

    private ImageView image;
    private ImageView cover;
    private ListView guessList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        context = this;
        dataTransferInterface = this;

        UserDataDBHandler userDataDBHandler = new UserDataDBHandler(context);
        user = userDataDBHandler.getUser("0");

        gameID = getIntent().getStringExtra("id");
        playersRequested = getIntent().getIntExtra("players", 0);
        rounds = getIntent().getIntExtra("rounds", 0);
        difficulty = getIntent().getIntExtra("bot_difficulty", 0);
        playlistName = getIntent().getStringExtra("playlist");

        guessList = (ListView) findViewById(R.id.list_of_guesses);
        image = (ImageView) findViewById(R.id.imgGame);
        cover = (ImageView) findViewById(R.id.imgCover);

        listOfOptions = new ArrayList<>();
        adapter = new GuessListAdapter(context, listOfOptions, dataTransferInterface);
        guessList.setAdapter(adapter);

        connect();

        directToLobby(playersRequested);

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
        sendGuessMessage(currentGuess);
    }

    private void directToLobby(int playersNeeded) {
        Intent i = new Intent(this, LobbyActivity.class);
        i.putExtra("players", playersNeeded);
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
                        Log.i(TAG, message);
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
        String message = "create," + user.getUsername() + "," + playlistName + "," + String.valueOf(playersRequested);
        webSocket.send(message);
    }

    private void joinGame() {
        //FIXME When join game is called, need to have the name of the host sent over from the GameBrowserActivity.
        String message = "join," + gameID + "," + user.getUsername();
        webSocket.send(message);
    }

    private void sendStart() {
        String message = "start," + user.getUsername();
        webSocket.send(message);
    }

    private void sendGuessMessage(String guess) {
        String message = "guess," + guess + "," + gameID + "," + user.getUsername();
        Log.i(TAG, message);
        webSocket.send(message);
    }

    private void messageReceived(String message) {
        Scanner scanner = new Scanner(message);
        String type = scanner.next();
        switch (type) {
            case "Created":
                createGameReaction(message);
                Log.i(TAG, message);
                break;
            //TODO THIS IS COMMENTED OUT UNTIL THE SERVER ISN'T DOUBLING THE CONNECTION.
//            case "FULL":
//                backToGameBrowser();
//                break;
            case "START":
                if(!user.getUserType().equals("host")) {
                    broadcastToLobby(message);
                }

                Log.i(TAG, message);
                break;
            case "ROUNDBEGIN":
                if(playersRequested == 1) {
                    bot = new Bot("bot", difficulty);
                }
                Log.i(TAG, "Do something with this?");
                break;
            case "WORD":
                addWord(message);
                Log.i(TAG, message);
                break;
//            case "HEIGHT":
//                setHeightAndWidth(message);
//                Log.i(TAG, message);
//                break;
            case "URL":
                receiveImage(message);
                if(playersRequested == 1) {
                    bot.set_word_list(listOfOptions);
                    runBot(); //TODO This still needs to be tested with the server.
                }
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
            case "CURRENTSCORES":
                wipeScores();
                Log.i(TAG, message);
                break;
            case "USERSCORE":
                addScores(message);
                Log.i(TAG, message);
                break;
            case "ENDSCORES":
                displayScores();
                break;
            case "PING":
//                Log.i(TAG, "Still Connected");
                break;
            case "newmember":
                broadcastToLobby(message);
                break;
            case "Currentplayers":
                broadcastToLobby(message);
                break;
            case "Player:":
                broadcastToLobby(message);
                break;
            case "Endplayers":
                broadcastToLobby(message);
                break;
            case "Bot:Correct": //FIXME Not sure that this is how it will respond.
                bot.setCorrect(true);
            default:
                Log.i(TAG, "NOT TRACKED: " +  message);
                break;
        }
    }

    private void createGameReaction(String message) {
        Scanner scanner = new Scanner(message);
//        if(scanner.next().equals("Created")) {
//            if (playersRequested < 2 && user.getUserType().equals("host")) {
//                sendStart();
//            }
//        }

    }

//    private void backToGameBrowser() {
//        webSocket.close(1000, "Game is Full.");
//        Intent backToBrowser = new Intent(context, GameBrowserActivity.class);
//        startActivity(backToBrowser);
//        finish();
//    }

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

//    private void setHeightAndWidth(String message) {
//        Scanner scanner = new Scanner(message);
//        String command = scanner.next();
//        if (command.equals("HEIGHT") && scanner.hasNextInt()) {
//            height = scanner.nextInt();
//            Log.i("Height:", String.valueOf(height));
//            command = scanner.next();
//        }
//        if (command.equals("WIDTH") && scanner.hasNextInt()) {
//            width = scanner.nextInt();
//            Log.i("Width:", String.valueOf(width));
//        }
//        pixels = new int[width*height];
//        Log.i("Pixels Length", String.valueOf(pixels.length));
//    }

    private void receiveImage(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("URL")) {
            String url = scanner.next();
            downloadImageTask = new DownloadImageTask(image, cover);
            downloadImageTask.execute(url);
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

    private void wipeScores() {
        currentScores = new ArrayList<>();
    }

    private void addScores(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("USERSCORE")) {
            currentScores.add(scanner.next());
            currentScores.add(String.valueOf(scanner.nextInt()));
        }
    }
    private void displayScores() {
        Intent intent = new Intent(this, PointUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("USERSANDSCORES", currentScores);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void broadcastToLobby(String message) {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", message);
        intent.setAction("LOBBY_MESSAGE");
        sendBroadcast(intent);
    }

    private void runBot() {
        final Handler botHandler = new Handler();
        Runnable botRunnable = new Runnable() {
            @Override
            public void run() {
                if (!bot.isCorrect()) {
                    String guess = "Bot:" + bot.guess();
                    webSocket.send(guess);
                }

                botHandler.postDelayed(this, (120 - 8*bot.getDifficulty()) / bot.getWord_list().size());
            }
        };
        botHandler.postDelayed(botRunnable, (120 - 8*bot.getDifficulty()) / bot.getWord_list().size());
    }

}
