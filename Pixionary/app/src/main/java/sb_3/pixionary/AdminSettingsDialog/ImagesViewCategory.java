package sb_3.pixionary.AdminSettingsDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.AdminSettings.RequestViewImages;
import sb_3.pixionary.Utilities.PreviewImageTask;

public class ImagesViewCategory extends AppCompatActivity {
    private static final int ADDED_IMAGE_ID = 1;
    private static final int DELETED_IMAGE_ID = 2;
    private Button previous, next, add;
    private int pageNum = 0;
    private RequestQueue requestQueue;
    private String category;
    private ImageView[] images = new ImageView[4];
    private String[] image_urls = new String[4];
    private String[] image_keys = new String[4];
    private PreviewImageTask image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_view_category);

        category = getIntent().getStringExtra("category");
        requestQueue = Volley.newRequestQueue(this);
        previous = (Button) findViewById(R.id.bt_PrevImages);
        next = (Button) findViewById(R.id.bt_NextImages);
        add = (Button) findViewById(R.id.bt_AddImage);
        images[0] = (ImageView) findViewById(R.id.iv_0);
        images[1] = (ImageView) findViewById(R.id.iv_2);
        images[2] = (ImageView) findViewById(R.id.iv_3);
        images[3] = (ImageView) findViewById(R.id.iv_1);


        pull_images();

        for (ImageView imagess: images) {
            imagess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.iv_0:
                            if (images[0] != null) {
                                view_single_image(0);
                                image.cancel(true);
                            }
                            break;
                        case R.id.iv_2:
                            if (images[1] != null) {
                                view_single_image(1);
                                image.cancel(true);
                            }
                            break;
                        case R.id.iv_3:
                            if (images[2] != null) {
                                view_single_image(2);
                                image.cancel(true);
                            }
                            break;
                        case R.id.iv_1:
                            if (images[3] != null) {
                                view_single_image(3);
                                image.cancel(true);
                            }
                            break;
                    }
                }
            });
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add image pop up window Name, Category(current), and URL link
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;
                pull_images();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                pull_images();
            }
        });

    }

    private void pull_images(){
        RequestViewImages view = new RequestViewImages(pageNum, category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("success")){
                        pageLogic(data.getInt("total"));
                        JSONArray pulled_images = data.getJSONArray("urls");
                        JSONArray pulled_words = data.getJSONArray("words");
                        for(int i = 0; i < pulled_images.length(); i++){
                            image_urls[i] = fetch(pulled_images.getString(i), images[i]);
                            image_keys[i] = pulled_words.getString(i);
                            if(pulled_images.length() < 4){
                                for(int j = pulled_images.length(); j < 4; j++){
                                    images[j].setImageBitmap(null);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); requestQueue.add(view);
    }

    private void view_single_image(int img){
        Intent intent = new Intent(this, ViewSelectedImage.class);
        intent.putExtra("image", encode_image(((BitmapDrawable)images[img].getDrawable()).getBitmap()));
        intent.putExtra("word", image_keys[img]);
        intent.putExtra("category", category);
        startActivityForResult(intent, DELETED_IMAGE_ID);
    }

    public static String encode_image(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    private String fetch(String image_url, ImageView iv) {
        String base = "http://proj-309-sb-3.cs.iastate.edu/";
        for(int i = 0; i < image_url.length(); i++){
            if(!(image_url.charAt(i) == '\\')){
                base += image_url.charAt(i);
            }
        }
        image = new PreviewImageTask(iv);
        image.execute(base);
        return base;
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
        Log.i("RESULT CODE ", String.valueOf(resultCode));
        final ProgressDialog pd = new ProgressDialog(this);
        if(resultCode == -1){
            return;
        } else{
            switch (requestCode){
                case ADDED_IMAGE_ID:
                    pd.setTitle("Success");
                    pd.setMessage("Image created!");
                    pd.setCancelable(true);
                    pd.show();
                    break;
                case DELETED_IMAGE_ID:
                    String word = getIntent().getStringExtra("word");
                    pd.setTitle("Success");
                    pd.setMessage(word + " has been removed");
                    pd.setCancelable(true);
                    pd.show();
                    break;
            }
        }
    }
}
