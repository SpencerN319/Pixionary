package sb_3.pixionary.gameplay;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class GameActivity extends AppCompatActivity implements DataTransferInterface {

    private static final String TAG = GameActivity.class.getSimpleName();
    public static final int START_GAME_REQUEST = 7;
    private String url = "ws://proj-309-sb-3.cs.iastate.edu:8080/name";
    private Context context;
    private OkHttpClient okHttpClient;
    private WebSocket webSocket;
    private DataTransferInterface dataTransferInterface;
    private DownloadImageTask downloadImageTask;

    private String gameID;
    private int playersRequested;
    private String playlistName;
    private User user;
    private ArrayList<String> listOfOptions;
    private ArrayList<String> currentScores;
    private GuessListAdapter adapter;
    private String currentGuess;
    private int difficulty;
    private int rounds;
    private Handler botHandler;
    private Runnable botRunnable;
    public Bot bot;
    private boolean reconnect = false;

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
        String isReconnect = getIntent().getStringExtra("reconnect");
        if (isReconnect != null && isReconnect.equals("true")) {
            reconnect = true;
        }

        guessList = (ListView) findViewById(R.id.list_of_guesses);
        image = (ImageView) findViewById(R.id.imgGame);
        cover = (ImageView) findViewById(R.id.imgCover);

        listOfOptions = new ArrayList<>();
        adapter = new GuessListAdapter(context, listOfOptions, dataTransferInterface);
        guessList.setAdapter(adapter);

        connect();
        if (!reconnect) {
            directToLobby(playersRequested);
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
                webSocket.close(1000, "Left Lobby");
                finish();
                break;
            case PlayAgainActivity.PLAYAGAIN:
                playAgain(PlayAgainActivity.PLAYAGAIN);
                break;
            case PlayAgainActivity.NOTPLAYAGAIN:
                playAgain(PlayAgainActivity.NOTPLAYAGAIN);
                break;
            default:
                Log.i(TAG, "Error from the Lobby");
                break;

        }
    }

    @Override
    public void setValuesAndReact(int position) {
        currentGuess = listOfOptions.get(position);
        Log.i("Current Guess", currentGuess);
        sendGuessMessage(currentGuess);
    }

    private void playAgain(int command) {
        if (command == PlayAgainActivity.PLAYAGAIN) {
            Log.i(TAG, "playagain");
            webSocket.send("playagain," + gameID + "," + user.getUsername());
            directToLobby(playersRequested);
        } else if (command == PlayAgainActivity.NOTPLAYAGAIN) {
            finish();
        }
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
                if (reconnect) {
                    webSocket.send("reconnect," + user.getUsername());
                } else {
                    if (user.getUserType().equals("host")) {
                        createGame();
                    } else {
                        joinGame();
                    }
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
        String message = "create," + user.getUsername() + "," + playlistName + "," + String.valueOf(playersRequested) + "," + String.valueOf(rounds);
        webSocket.send(message);
    }

    private void joinGame() {
        String message = "join," + user.getUsername() + "," + gameID;
        webSocket.send(message);
    }

    private void sendStart() {
        String message = "start," + user.getUsername();
        webSocket.send(message);
    }

    private void sendGuessMessage(String guess) {
        String message = "guess," + guess + "," + gameID + "," + user.getUsername();
        webSocket.send(message);
    }

    private void messageReceived(String message) {
        Scanner scanner = new Scanner(message);
        String type = scanner.next();
        switch (type) {
            case "START":
                broadcastToLobby(message);
                break;
            case "ROUNDBEGIN":
                if(playersRequested == 1) {
                    runBot();
                }
                if(reconnect) {
                    reconnect = false;
                }
                break;
            case "WORD":
                addWord(message);
                break;
            case "URL":
                receiveImage(message);
                break;
            case "CORRECT!":
                if (playersRequested == 1) {
                    botHandler.removeCallbacks(botRunnable);
                } else {
                    startGuessResponse("Correct!");
                }
                break;
            case "INCORRECT!":
                startGuessResponse("Incorrect!");
                break;
            case "ROUNDEND":
                wipeBitmap();
                break;
            case "CURRENTSCORES":
                wipeScores();
                break;
            case "USERSCORE":
                addScores(message);
                break;
            case "BOTSCORE":
                addScores(message);
                break;
            case "ENDSCORES":
                displayScores();
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
            case "BOTCORRECT!":
                bot.setCorrect(true);
                break;
            case "GG":
                //This means game is over.
                break;
            case "Winner:":
                displayPlayAgain(message);
                break;
            case "NEWGAME":
                if (playersRequested == 1) {
                    bot.setCorrect(true);
                }
                break;
            case "NONEWGAME":
                broadcastToLobby(message);
                finish();
                break;
            case "fail":
                finish();
                break;
            default:
                Log.i(TAG, "NOT TRACKED: " +  message);
                break;
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
        if (!reconnect) {
            downloadImageTask.cancel(true);
        }

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
        } else if (command.equals("BOTSCORE")) {
            currentScores.add("Bot");
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

    private void displayPlayAgain(String message) {
        Intent intent = new Intent(context, PlayAgainActivity.class);
        intent.putExtra("winner", message);
        startActivityForResult(intent, 20);
    }

    private void runBot() {
        bot = new Bot(user.getUsername(), difficulty);
        bot.setCorrect(false);
        ArrayList<String> botList = new ArrayList<>(listOfOptions);
        bot.set_word_list(botList);
        final int size = bot.getWord_list().size();
        botHandler = new Handler();
        botRunnable = new Runnable() {
            @Override
            public void run() {
                if (bot.isCorrect()) {
                    botHandler.removeCallbacks(botRunnable);
                } else {
                    String guess = "guess,Bot:" + bot.guess() + "," + gameID + "," + user.getUsername();
                    webSocket.send(guess);
                    Log.i(TAG, guess);
                    botHandler.postDelayed(this, (120 - 4*bot.getDifficulty()) / size * 1000);
                }
            }
        };
        if (bot.isCorrect()) {
            botHandler.removeCallbacks(botRunnable);
        } else {
            botHandler.postDelayed(botRunnable, (120 - 4*bot.getDifficulty()) / size * 1000);
        }
    }

}
