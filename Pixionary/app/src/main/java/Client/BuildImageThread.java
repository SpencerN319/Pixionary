package Client;

/**
 * Created by fastn on 2/7/2018.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by fastn on 2/7/2018.
 */

public class BuildImageThread extends Thread{

    private Socket socket;
    private BufferedReader in;

    private int imgWidth;
    private int imgHeight;
    private String imageData;

    public BuildImageThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        while(true) {
            try {
                //Read ImageData -- Formatting of string "ImageData, (imageWidth), (imageHeight)"
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                imageData = in.readLine();
                Scanner scan = new Scanner(imageData);
                String dataID = scan.next();
                if (dataID == "ImageData") { //This can be changed dependent on the server side.
                    imgWidth = scan.nextInt();
                    imgHeight = scan.nextInt();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getImgWidth() { return imgWidth; }

    public int getImgHeight() { return imgHeight; }

}