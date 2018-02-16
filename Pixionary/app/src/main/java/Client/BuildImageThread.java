package Client;

/**
 * Created by fastn on 2/7/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import sb_3.pixionary.ImageBuilder.ImageCreator;

/**
 * Created by fastn on 2/7/2018.
 */

public class BuildImageThread extends Thread{

    private Socket socket;
    private BufferedReader in;

    private Context context;
    private int imgWidth;
    private int imgHeight;
    private String imageData;
    private boolean received;
    private ImageCreator builder;


    public BuildImageThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        received = false;
        while(!received) {
            try {
                //Read ImageData -- Formatting of string "ImageData, (imageWidth), (imageHeight)"
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                imageData = in.readLine();
                Scanner scan = new Scanner(imageData);
                String dataID = scan.next();
                if (dataID == "ImageData") { //This can be changed dependent on the server side.
                    imgWidth = scan.nextInt();
                    imgHeight = scan.nextInt();
                    builder = new ImageCreator(imgWidth, imgHeight);
                    received = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Don't know if we will need this.
    public ImageCreator getbuilder() { return builder; }

    public boolean isCreated() { return received; }

}