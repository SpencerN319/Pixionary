package sb_3.pixionary.gameplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import sb_3.pixionary.R;

public class LobbyActivity extends AppCompatActivity {

    private static final String TAG = LobbyActivity.class.getSimpleName();

    TextView id;
    EditText send;
    Button btn;

    WebSocketClient webSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        id = (TextView) findViewById(R.id.displayTEMP);
        send = (EditText) findViewById(R.id.sendTEMP);
        btn = (Button) findViewById(R.id.buttonTEMP);

        connectWebSocket();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });

//        Bundle bundle = getIntent().getExtras();
//        int value = -1;
//        if (bundle != null) {
//            value = bundle.getInt("id");
//        }
//        id.setText(String.valueOf(value));

    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://echo.websocket.org");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i(TAG, "opened");
                webSocketClient.send("Hello from Steven!");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.displayTEMP);
                        textView.setText(message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i(TAG, "Closed" + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i(TAG, "Error " + e.getMessage());
            }
        };
        webSocketClient.connect();
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.sendTEMP);
        webSocketClient.send(editText.getText().toString());
        editText.setText("");
    }
}
