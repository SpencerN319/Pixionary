package sb_3.pixionary.Utilities;

import android.os.Build;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by fastn on 3/12/2018.
 */

public class RealTimeClient {
    URI uri;
    WebSocketClient webSocketClient;
    private static String TAG = RealTimeClient.class.getSimpleName();

    public RealTimeClient(String location, String uriHost, String uriPort) {
        TAG += ":" + location;
        try {
            uri = new URI("ws://" + uriHost + ":" + uriPort);
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }
    //TODO not sure if this is going to be needed.
}
