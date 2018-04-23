package sb_3.pixionary.SharedSettings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import sb_3.pixionary.R;

public class AddImage extends AppCompatActivity {
    private static final int ADDED_IMAGE_ID = 2;
    private Button previous, next;
    private int pageNum = 0;
    private String category;
    private ImageView[] images = new ImageView[4];
    private String key_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        category = getIntent().getStringExtra("category");
        key_word = getIntent().getStringExtra("word");
        previous = (Button) findViewById(R.id.bt_PrevImages);
        next = (Button) findViewById(R.id.bt_NextImages);
        images[0] = (ImageView) findViewById(R.id.iv_0);
        images[1] = (ImageView) findViewById(R.id.iv_2);
        images[2] = (ImageView) findViewById(R.id.iv_3);
        images[3] = (ImageView) findViewById(R.id.iv_1);

        //pull_images();

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
                //pull_images();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                //pull_images();
            }
        });

    }

    /* //TODO insert bash command to pull all 12 images
    private void pull_images(){
        RequestViewImages view = new RequestViewImages(pageNum, category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("success")){
                        pageLogic(data.getInt("total"));
                        Log.i("PULLED IMAGES LENGTH", ""+data.getInt("total"));
                        JSONArray pulled_images = data.getJSONArray("urls");
                        JSONArray pulled_words = data.getJSONArray("words");
                        for(int i = 0; i < pulled_images.length(); i++){
                            if(!(pulled_words.getString(i).equals("blank"))){
                                image_urls[i] = fetch(pulled_images.getString(i), images[i]); //SETS ImageView image in background
                                image_keys[i] = pulled_words.getString(i);
                                images[i].setClickable(true);
                            } else {
                                image_urls[i] = "";
                                image_keys[i] = "";
                                images[i].setImageBitmap(null);
                                images[i].setClickable(false);
                            }
                            if(pulled_images.length() < 4){
                                for(int j = pulled_images.length(); j < 4; j++){
                                    images[j].setImageBitmap(null);
                                    images[j].setClickable(false);
                                }
                            }
                        }
                    } else {
                        for(int i = 0; i < 4; i++){
                            image_urls[i] = "";
                            image_keys[i] = "";
                            images[i].setImageBitmap(null);
                            images[i].setClickable(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); requestQueue.add(view);
    }*/

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
        final ProgressDialog pd = new ProgressDialog(this);
        if(resultCode == -1){
            return;
        } else{
            switch (requestCode){
                case ADDED_IMAGE_ID:
                    //pull_images();
                    String word = data.getStringExtra("word");
                    Intent intent = new Intent();
                    intent.putExtra("word", word);
                    finish();
                    break;
            }
        }
    }
}
