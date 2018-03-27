package sb_3.pixionary.gameplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import SaveData.UserDataDBHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import sb_3.pixionary.Adapters.GuessListAdapter;
import sb_3.pixionary.ImageBuilder.Pixel;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.RealTimeUpdater;

import static sb_3.pixionary.MainMenuActivity.user;

//TODO still need to add a view to see the players in the lobby. --- Not important RN.
public class GameActivity extends AppCompatActivity {

    private static final String TAG = RealTimeUpdater.class.getSimpleName();
    private String url = "ws://proj-309-sb-3.cs.iastate.edu:8080/name";
    private Context context;
    private OkHttpClient okHttpClient;
    private WebSocket webSocket;

    private int gameID = -1;
    private String playlistName;
    private User user;
    private ArrayList<String> listOfOptions;
    private ArrayAdapter<String> adapter;
    private Bitmap bitmap;

    private Button sendGuess;
    private ImageView image;
    private ListView guessList;

    //Image stuff
    int width;
    int height;
    int x = 0;
    int y = 0;
    int[] pixels;
    boolean[] pixelUsed;
    Handler handler = null;
    Runnable runnable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        context = this;

        guessList = (ListView) findViewById(R.id.list_of_guesses);
        sendGuess = (Button) findViewById(R.id.btnSendGuess);
        image = (ImageView) findViewById(R.id.imgGame);

        connect();

        UserDataDBHandler db = new UserDataDBHandler(this);
        user = db.getUser("0");
        playlistName = getIntent().getStringExtra("playlist");
        listOfOptions = new ArrayList<>();
        listOfOptions.add("Fake 1");
        listOfOptions.add("Fake 2");
//        GuessListAdapter adapter = new GuessListAdapter(context, listOfOptions);
        adapter = new ArrayAdapter<>(context, R.layout.guess_list, R.id.guess1, listOfOptions);
        guessList.setAdapter(adapter);

        sendGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Create a method to send the Guess
            }
        });

    }

    public void connect() {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {

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
        String message = "join" + gameID;
        webSocket.send(message);
    }

    private void sendStart() {
        String message = "start," + gameID;
        webSocket.send(message);
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
                //addPixel(message);
                receiveAllPixels(message);
                break;
            case "ROUNDEND":
                wipeBitmap();
                break;
            default:
                Log.i("Not tracked", message);
        }
    }

    private void setWords() {
        //TODO not sure what to do here.
    }

    private void wipeBitmap() {
        bitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
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

    private void receiveAllPixels(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("px")) {
            for (int i = 0; i < pixels.length; i++) {
                x++;
                if (x >= width) {
                    y++;
                    x = 0;
                }
                pixels[y*width + x] = scanner.nextInt();
            }
        }
        //FIXME Might need a separate method to check for when the runnable should start then we can put the method in the onCreate()
        pixelUsed = new boolean[pixels.length];
        Arrays.fill(pixelUsed, false);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Pixel pix = getUnusedPixel();
                postPixel(bitmap, pix);
                handler.postDelayed(runnable, 3);
            }
        };
        handler.postDelayed(runnable, 3);
    }

    private Pixel getUnusedPixel() {
        Random random = new Random();
        boolean used;
        Pixel pix;
        do {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            used = pixelUsed[x*y];
            pix = new Pixel(x, y, pixels[x*y]);
        } while (used);

        return pix;
    }

    private void postPixel(Bitmap bitmap, Pixel pixel) {
        int x = pixel.getXPosition();
        int y = pixel.getYPosition();
        bitmap.setPixel(x, y, pixels[y*width + x]);
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
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image.setImageBitmap(bitmap);
    }

    class UpdateImage extends AsyncTask<Void, Void, Void> {

        String message;

        public UpdateImage(String message) {
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            Scanner scanner = new Scanner(message);
            String command = scanner.next();
            if (command.equals("px")) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int color = scanner.nextInt();
                bitmap.setPixel(x, y, color);
                Log.i("Pixel Set", String.valueOf(color));
            }
            return null;
        }

    }

}
