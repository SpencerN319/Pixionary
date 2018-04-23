package sb_3.pixionary.SharedSettings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.Settings.DownloadSearchedImages;

public class AddImage extends AppCompatActivity {
    private static final int ADDED_IMAGE_ID = 2;
    private Button previous, next;
    private int pageNum = 0;
    private String category;
    private ImageView[] images = new ImageView[4];
    private String key_word;
    private RequestQueue requestQueue;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        dialog = new ProgressDialog(this);
        category = getIntent().getStringExtra("category");
        key_word = getIntent().getStringExtra("word");
        previous = (Button) findViewById(R.id.bt_PrevImages);
        requestQueue = Volley.newRequestQueue(this);
        next = (Button) findViewById(R.id.bt_NextImages);
        images[0] = (ImageView) findViewById(R.id.iv_0);
        images[1] = (ImageView) findViewById(R.id.iv_2);
        images[2] = (ImageView) findViewById(R.id.iv_3);
        images[3] = (ImageView) findViewById(R.id.iv_1);

        pull_images(key_word);


        for (ImageView imagess: images) {
            imagess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.iv_0:
                            view_single_image(0);
                            break;
                        case R.id.iv_2:
                            view_single_image(1);
                            break;
                        case R.id.iv_3:
                            view_single_image(2);
                            break;
                        case R.id.iv_1:
                            view_single_image(3);
                            break;
                    }
                }
            });
        }

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
            }
        });

    }

    private void pull_images(String word){
        dialog.setTitle("Loading");
        dialog.setMessage("searching for "+key_word);
        dialog.show();
        dialog.setCancelable(false);
        DownloadSearchedImages down = new DownloadSearchedImages(word, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray data = new JSONArray(response);
                    int length = data.length();
                    Log.i("DATA LENGTH", ""+length);
                    if(length > 4){
                        for(int i = 0; i < 4; i++){
                            images[i].setImageBitmap(decode(data.getString(i)));
                            images[i].setClickable(true);
                        }
                    } else {
                        for(int i = 0; i < length; i++){
                            images[i].setImageBitmap(decode(data.getString(i)));
                            images[i].setClickable(true);
                        }
                        for(int j = length; j < 4; j++){
                            images[j].setClickable(false);
                        }
                    }
                    dialog.cancel();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        requestQueue.add(down);
    }

    private static Bitmap decode(String image)
    {
        byte[] decodedBytes = Base64.decode(image, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void view_single_image(int img){
        Intent intent = new Intent(this, AddViewImage.class);
        intent.putExtra("image", encode_image(((BitmapDrawable)images[img].getDrawable()).getBitmap()));
        intent.putExtra("word", key_word);
        intent.putExtra("category", category);
        startActivityForResult(intent, ADDED_IMAGE_ID);
    }


    public static String encode_image(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    private void pageLogic(int totalUsers) {
        if (totalUsers > 10) {
            if (pageNum == 0) {
                disableButton(previous);
            } else {
                enableButton(previous);
            }
            if (totalUsers < (pageNum+1)*10) {
                disableButton(next);
            } else {
                enableButton(next);
            }
        } else {
            disableButton(previous);
            disableButton(next);
        }
    }

    private void disableButton(Button button){button.setEnabled(false);}

    private void enableButton(Button button){button.setEnabled(true);}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1 || resultCode == 0){
            return;
        } else{
            switch (requestCode){
                case ADDED_IMAGE_ID:
                    String word = data.getStringExtra("word");
                    Intent intent = new Intent();
                    intent.putExtra("word", word);
                    finish();
                    break;
            }
        }
    }
}
