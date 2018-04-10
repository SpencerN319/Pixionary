package sb_3.pixionary.gameplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;
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
import sb_3.pixionary.Utilities.DownloadImageTask;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.interfaces.DataTransferInterface;

//TODO still need to add a view to see the players in the lobby. --- Not important RN.
public class GameActivity extends AppCompatActivity implements DataTransferInterface {

    private static final String TAG = GameActivity.class.getSimpleName();
    private String url = "ws://proj-309-sb-3.cs.iastate.edu:8080/name";
    private Context context;
    private OkHttpClient okHttpClient;
    private WebSocket webSocket;
    private DataTransferInterface dataTransferInterface;
    private DownloadImageTask downloadImageTask;

    private int gameID = -1;
    private String playlistName;
    private User user;
    private ArrayList<String> listOfOptions;
    private GuessListAdapter adapter;
    private Bitmap bitmapImage;
    private Bitmap bitmapCover;
    private String currentGuess;
    private Handler handler;

    private Button sendGuess;
    private Button fake;
    private ImageView image;
    private ImageView cover;
    private ListView guessList;
    private View view;

    private boolean firstPress = true;
    //Image stuff
    int width;
    int height;
    int[] pixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //TODO Might need to put this elsewhere
        context = this;
        dataTransferInterface = this;

        guessList = (ListView) findViewById(R.id.list_of_guesses);
        sendGuess = (Button) findViewById(R.id.btnSendGuess);
        image = (ImageView) findViewById(R.id.imgGame);
        cover = (ImageView) findViewById(R.id.imgCover);
//        view = (View) findViewById(R.id.)
//        connect();
        String url = "https://i.imgur.com/AEWms1M.jpg";
        setImages(url);

        UserDataDBHandler db = new UserDataDBHandler(this);
        user = db.getUser("0");
        playlistName = getIntent().getStringExtra("playlist");
        listOfOptions = new ArrayList<>();
        adapter = new GuessListAdapter(context, listOfOptions, dataTransferInterface);
        guessList.setAdapter(adapter);

        sendGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGuess(currentGuess);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        sendImageUpdate();
    }


    private void setImages(String url) {
        handler = new Handler();
        bitmapImage = null;
        downloadImageTask = new DownloadImageTask(bitmapImage);
        downloadImageTask.execute(url);

    }

    private void sendImageUpdate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cover.invalidate();
                if (!firstPress) {
                    updateImage();
                    Log.i("Runnable", "Called Again");
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bitmapImage = downloadImageTask.getBitmap();
                    width = bitmapImage.getWidth();
                    height = bitmapImage.getHeight();
                    bitmapCover = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bitmapCover.setPixel(x, y, 0xFFFFFFFF);
                        }
                    }
                    cover.setImageBitmap(bitmapCover);
                    image.setImageBitmap(bitmapImage);
                    firstPress = false;
                }
                handler.postDelayed(this, 10);
            }
        }, 10);
    }

    private void updateImage() {
        for (int i = 0; i < 100; i++) {
            Pixel pixel = getPixel(width, height);
            bitmapCover.setPixel(pixel.getXPosition(), pixel.getYPosition(), pixel.getColor());
        }
    }

    private Pixel getPixel(int width, int height) {
        Random random = new Random();
        Pixel pix;
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        pix = new Pixel(x, y, 0);
        return pix;
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
                createGameReaction(message);
                break;
            case "START":
                break;
            case "WORD":
                addWord(message);
                break;
            case "HEIGHT":
                setHeightAndWidth(message);
                break;
            case "URL":
                receiveImage(message);
                break;
            case "ROUNDEND":
                wipeBitmap();
                break;
            default:
                Log.i("Not tracked", message);
        }
    }

    private void wipeBitmap() {
       // bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    private Pixel getPixel() {
        Random random = new Random();
        Pixel pix;
        int x = random.nextInt(500);
        int y = random.nextInt(600);
        pix = new Pixel(x, y, 0);
        return pix;
    }

    private void receiveImage(String message) {
        Scanner scanner = new Scanner(message);
        String command = scanner.next();
        if (command.equals("URL")) {
            String url = scanner.next();
            //DownloadImageTask imageTask = new DownloadImageTask(image, cover, this);
            //imageTask.execute(url);
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
        //bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //image.setImageBitmap(bitmap);
    }

    @Override
    public void setValuesAndReact(int position) {
        currentGuess = listOfOptions.get(position);
        Log.i("Current Guess", currentGuess);
    }

}
