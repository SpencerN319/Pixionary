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
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import sb_3.pixionary.MainMenuActivity;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.Settings.RequestSaveImage;

public class AddViewImage extends Activity {

    private RequestQueue requestQueue;
    private TextView image_name;
    private Button add;
    private String word, category, image64, file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_image);
        ImageView image = (ImageView) findViewById(R.id.iv_image);
        image_name = (TextView) findViewById(R.id.tv_ImageName);
        add = (Button) findViewById(R.id.bt_AddImage);
        requestQueue = Volley.newRequestQueue(this);
        word = getIntent().getStringExtra("word");
        category = getIntent().getStringExtra("category");
        image64 = getIntent().getStringExtra("image");
        file_name = getIntent().getStringExtra("file_name");

        image_name.setText(word);
        image.setImageBitmap(decode(getIntent().getStringExtra("image")));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestSaveImage save = new RequestSaveImage(image64, word, category, MainMenuActivity.user.getUsername(), file_name, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject data = new JSONObject(response);
                            if(data.getBoolean("success")){
                                Intent intent = new Intent();
                                intent.putExtra("word", word);
                                setResult(2, intent);
                                finish();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                requestQueue.add(save);
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

}
