package Client;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Steven on 2/2/2018.
 */

public class ClientGuessThread implements Runnable {


    private Socket socket;
    private String guessStr;

    public ClientGuessThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while(true) {
        }
    }

//    class myTask extends AsyncTask<Void, Void, Void>
//    {
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            try {
//                printWriter = new PrintWriter(socket.getOutputStream()); //set the output stream
//                printWriter.write(guessStr); //send the String
//                printWriter.flush();
//                printWriter.close();
//                socket.close();
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//
//    }
}
