package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Steven Rein on 2/5/2018.
 */

public class LoginThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String username;
    private String password;
    private boolean success;

    public LoginThread(Socket socket, String user, String pass) {
        this.socket = socket;
        this.username = user;
        this.password = pass;
        success = false;

    }

    public void run() {
        while(true) {
            try {
                String loginInfo = "LoginRequest " + username + " " + password;

                //Send the login request
                out = new PrintWriter(socket.getOutputStream());
                out.println(loginInfo);
                out.flush();

                //Read Response
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();
                Boolean successObj = Boolean.valueOf(response);
                success = successObj.booleanValue();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }




}
