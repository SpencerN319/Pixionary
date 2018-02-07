package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by fastn on 2/6/2018.
 */

public class GuessThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String guess;
    private boolean correct;

    public GuessThread(Socket socket, String guess) {
        this.socket = socket;
        this.guess = guess;
    }

    public void run() {
        while(true) {
            try {
                String guessString = "Guess: " + guess;

                //Send guess
                out = new PrintWriter(socket.getOutputStream());
                out.println(guessString);
                out.flush();

                //Reading response will need to be going at all times checking
                // to see if someone got the image right,
                // then will code Activity to clear() for now though.
                //Make a different thread for this.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();
                Boolean successObj = Boolean.valueOf(response);
                correct = successObj.booleanValue();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean getCorrect() { return correct; }

}
