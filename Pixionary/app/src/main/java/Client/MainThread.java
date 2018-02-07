package Client;

import java.net.Socket;

/**
 * Created by fastn on 2/5/2018.
 */

public class MainThread implements Runnable {

    private Socket socket;

    public MainThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        while(true) {
            try {
                //Add some subThreads here I think?
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
