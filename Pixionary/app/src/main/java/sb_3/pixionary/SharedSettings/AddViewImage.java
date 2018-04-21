package sb_3.pixionary.SharedSettings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import sb_3.pixionary.R;

public class AddViewImage extends Activity {

    private RequestQueue requestQueue;
    private TextView image_name;
    private Button add, close;
    private String word, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_image);
        ImageView image = (ImageView) findViewById(R.id.iv_image);
        image_name = (TextView) findViewById(R.id.tv_ImageName);
        add = (Button) findViewById(R.id.bt_AddImage);
        close = (Button) findViewById(R.id.bt_close);
        requestQueue = Volley.newRequestQueue(this);
        word = getIntent().getStringExtra("word");
        category = getIntent().getStringExtra("category");

        image_name.setText(word);
        image.setImageBitmap(decode(getIntent().getStringExtra("image")));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO create RequestAddImage(String creator, String location, String word, String Category, Image);
                /*
                RequestDeleteImage delete_image = new RequestDeleteImage(category, word, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject data = new JSONObject(response);
                            if(data.getBoolean("success")){
                                returnToImages(2,word);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }); requestQueue.add(delete_image);*/
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToImages(RESULT_OK, null);
            }
        });
    }

    /**
     * Decodes the bitmap string passed by putExtra
     * @param image
     * @return
     */
    private static Bitmap decode(String image)
    {
        byte[] decodedBytes = Base64.decode(image, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }



    private void returnToImages(int result, String word){
        Intent intent = new Intent();
        intent.putExtra("word", word);
        setResult(result);
        finish();
    }
}
