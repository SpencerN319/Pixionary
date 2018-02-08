package Client;


import android.graphics.Bitmap;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


public class ReceivePixelThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private int x;
    private int y;
    private int RGB;
    private Bitmap bitmap;
    private String pixelMsg;
    private boolean received;

    public ReceivePixelThread(Socket socket, Bitmap bitmap) {
        this.socket = socket;
        this.bitmap = bitmap;
    }

    public void run() {
        //Add a boolean value to stop it.
        while(true) {
            received = false;
            try {
                //Read Pixel Message and convert.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pixelMsg = in.readLine();
                Scanner scan = new Scanner(pixelMsg);
                String dataId = scan.next();
                if (dataId.equals("px")) {
                    x = scan.nextInt();
                    y = scan.nextInt();
                    RGB = scan.nextInt();
                    //May need to read into an array and post on our own.
                    //Doing a while(true) loop within the OnCreate method
                    //could create problems. Then try what is commented out.
                    bitmap.setPixel(x, y, RGB);
                    received = true;
                }
                //Someone guessed right lets move to the next picture.
                if (dataId.equals("clear")) {
                    Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                            bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    bitmap = emptyBitmap;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isReceived() { return received; }
}