package Client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import ImageBuilder.Pixel;


public class ReceivePixelThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private int x;
    private int y;
    private int RGB;
    Pixel newPix;
    private String pixelMsg;
    private boolean received;

    public ReceivePixelThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        while(true) {
            received = false;
            try {
                //Read Pixel Message and convert.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pixelMsg = in.readLine();
                Scanner scan = new Scanner(pixelMsg);
                String dataId = scan.next();
                if (dataId == "px") {
                    x = scan.nextInt();
                    y = scan.nextInt();
                    RGB = scan.nextInt();
                }
                newPix = new Pixel(x, y, RGB);
                received = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Pixel getNewPixel() { return newPix; }

    public boolean isReceived() { return received; }
}