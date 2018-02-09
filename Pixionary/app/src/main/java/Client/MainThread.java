package Client;



import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by fastn on 2/5/2018.
 */

public class MainThread implements Runnable {

    //Variables to run the thread.
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanInput;

    //Variables to share with the Activities.
    private String msgSend;
    private String msgReceive;
    private String msgError;

    /**
     * Creates the thread for communication with the server.
     * @param socket the socket to communicate on.
     */
    public MainThread(Socket socket) {
        this.socket = socket;
        //Not sure what to do here.
    }

    public void run() {
        while(true) {
            try {
                scanInput = new Scanner(System.in);
            } catch(Exception e) {
                System.out.println();
            }

        }
    }

}
