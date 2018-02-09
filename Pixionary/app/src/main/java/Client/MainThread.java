package Client;




import android.app.Service;
import android.content.Context;
import android.content.Intent;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Scanner;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.View;


/**
 * Created by fastn on 2/5/2018.
 */

public class MainThread extends Service implements Runnable {

    //Variables to run the thread.
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanInput;
    private ServiceToActivity activityReceiver;

    //Variables to share with the Activities.
    private String msgSend;
    private String msgReceive;
    private String msgError;

    /**
     * Creates the thread for communication with the server.
     *
     */
    public MainThread(Socket socket) {

        this.socket = socket;
    }

    public void run() {
        enableReceiver();

        while(true) {
            try {
                //Initiate both input and output to server.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

                //Check if message should be sent, if so send.
                getAndSendData();

                //Reads message from server, deals with it.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String received = in.readLine();
                if (isStringNew(received)) {
                    scanInput = new Scanner(msgReceive);
                    String id  = scanInput.next();
                    receiveMessageHandling(id);
                }


            } catch(Exception e) {
                System.out.println();
            }

        }
    }


    /**
     * Finds whether the current string is new or not.
     * @param string The string we are testing.
     * @return tells whether or not the string is new.
     */
    private boolean isStringNew(String string) {


        if (string.equals(msgSend)) {
            msgSend = string;
            return true;
        }

        return false;
    }

    private void enableReceiver() {
        activityReceiver = new ServiceToActivity("Server");
        IntentFilter filter = new IntentFilter("Server");
        registerReceiver(activityReceiver, filter);
    }

    private void getAndSendData() {
        msgSend = activityReceiver.getData();
        if (isStringNew(msgSend) && msgSend != null) {
            out.write(msgSend);
            out.flush();
            msgSend = null;
        }
    }

    /**
     * Identifies where the message should go.
     */
    private void receiveMessageHandling(String id) {
        //Decide where to send message data.
        switch (id) {
            case "LoginResult":
                //Send to the LoginActivity
                sendDataToActivity("LoginActivity", msgReceive);
            case "ImageData":
                //Send to the PlayActivity.
                sendDataToActivity("PlayActivity", msgReceive);
            case "px":
                //Send to the PlayActivity
                sendDataToActivity("PlayActivity", msgReceive);
            case "GuessResponse":
                //Send to the PlayActivity
                sendDataToActivity("PlayActivity", msgReceive);
        }
    }

    private void sendDataToActivity(String whichActivity, String receivedMessage) {
        Intent broadcaster = new Intent();
        broadcaster.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES); //Don't know what this is for.
        broadcaster.setAction(whichActivity);
        broadcaster.putExtra("data", receivedMessage);
        sendBroadcast(broadcaster);
    }

    //Need to extend Service.
    @Override
    public IBinder onBind(Intent intent) {
        //Won't be used.
        return null;
    }



}
