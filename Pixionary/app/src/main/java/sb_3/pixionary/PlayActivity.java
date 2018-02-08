package sb_3.pixionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import sb_3.pixionary.ImageBuilder.ImageCreator;

import java.io.IOException;
import java.net.Socket;

import Client.BuildImageThread;
import Client.GuessThread;
import Client.ReceivePixelThread;

public class PlayActivity extends AppCompatActivity {

    private Button btnGuess;
    private EditText etGuess;
    public ImageView picGuess;
    private Bitmap bitmap;
    public TextView imagesRemaining;
    private ImageCreator editImage;

    private String[] images;
    private String guess;
    private int imagenum = 0;

    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        btnGuess = (Button) findViewById(R.id.btnSendGuess);
        etGuess = (EditText) findViewById(R.id.etGuess);
        picGuess = (ImageView) findViewById(R.id.imgGame);
        imagesRemaining = (TextView) findViewById(R.id.images_remaining);

        //For local
        images = new String[]{"cat.jpg", "cow.jpg", "dog.jpg", "horse.jpg"};
        nextImage();

        //Server connecting, creating and updating image.
//        try {
//            Socket socket = new Socket("localhost", 9092);
//            presentImage(socket);
//
//        } catch(IOException e) {
//            e.printStackTrace();
//        }


        images = new String[]{"cat.jpg", "cow.jpg", "dog.jpg", "horse.jpg"};

        nextImage();

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guess = etGuess.getText().toString();

                //This is locally test stuff.
                if (guess.equals(cutExtension(images[imagenum]))) {
                    imagenum++;
                    etGuess.setText("");
                    if (imagenum >= images.length) {
                        endGame();
                    } else {
                        nextImage();
                    }
                }
                else {
                    wrongGuessDialog();
                }
            }
        });

    }

    private String cutExtension(String fileName) {
        int dot = fileName.lastIndexOf(".");

        return fileName.substring(0, dot);
    }


    private void wrongGuessDialog() {
        AlertDialog.Builder wrongBuilder = new AlertDialog.Builder(PlayActivity.this);
        wrongBuilder.setTitle("Wrong!");
        wrongBuilder.setMessage("Hurry up and try again!");
        wrongBuilder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        wrongBuilder.create();
        wrongBuilder.show();
    }

    private void endGame() {
        //Add the collection of data from the game.

        //End Game Dialog
        AlertDialog.Builder endBuilder = new AlertDialog.Builder(PlayActivity.this);
        endBuilder.setTitle("Game Over!");
        endBuilder.setMessage("You Won!");
        //Figure out how to make changes to
        //the look of the alert dialog.
        //endBuilder.setView();
        endBuilder.setPositiveButton("Exit to Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(PlayActivity.this, MainMenuActivity.class);
//                startActivity(i);
                finish();

            }
        });
        endBuilder.create();
        endBuilder.show();
    }

    private void nextImage(){
        //Display Remaining images.
        imagesRemaining.setText("Images Remaining: " + (images.length - imagenum));

        //Initialize editImage and starts the image at the View.
        editImage = new ImageCreator(getApplicationContext(), images, imagenum);
        bitmap = editImage.getImage();
        picGuess.setImageBitmap(bitmap);

        editImage.updateImageLocal();

    }

    private void presentImage(Socket socket) {
        BuildImageThread builder = new BuildImageThread(socket);
        builder.run();

        editImage = new ImageCreator(getApplicationContext(), builder);

        ReceivePixelThread updater = new ReceivePixelThread(socket);
        updater.run();

        editImage.updateImage(updater);


    }

    private void  sendGuess(String guess) {
        try {
            socket = new Socket("localhost", 9091);
            GuessThread guessSend = new GuessThread(socket, guess);
            guessSend.run();
            boolean correct = guessSend.getCorrect();
            if (correct) {
                //clear() and start new from server.
            }
            else {
                wrongGuessDialog();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


