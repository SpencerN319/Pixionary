package sb_3.pixionary.AdminSettingsDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.AdminSettings.RequestViewImages;

public class ImagesViewCategory extends AppCompatActivity {
    private static final int ADDED_IMAGE_ID = 1;
    private static final int DELETED_IMAGE_ID = 2;
    private ImageView img1, img2, img3, img4;
    private Button previous, next, add;
    private int pageNum = 0;
    private RequestQueue requestQueue;
    private String category;
    private ImageView[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_view_category);

        category = getIntent().getStringExtra("category");
        requestQueue = Volley.newRequestQueue(this);
        previous = (Button) findViewById(R.id.bt_PrevImages);
        next = (Button) findViewById(R.id.bt_NextImages);
        add = (Button) findViewById(R.id.bt_AddImage);
        images[0] = (ImageView) findViewById(R.id.iv_one);
        images[1] = (ImageView) findViewById(R.id.iv_two);
        images[2] = (ImageView) findViewById(R.id.iv_three);
        images[3] = (ImageView) findViewById(R.id.iv_four);

        //TODO add on click listener

        pull_images();


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
                //TODO pull 4 images from the server according to their page ordering.
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("success")){
                        pageLogic(data.getInt("total"));
                        JSONArray pulled_images = data.getJSONArray("images");
                        for(int i = 0; i < pulled_images.length(); i++){
                            images[i].setImageBitmap(null);
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
        if(resultCode == 0){
            return;
        } else{
            switch (requestCode){
                case ADDED_IMAGE_ID:
                    pd.setTitle("Success");
                    pd.setMessage("Category created!");
                    pd.setCancelable(true);
                    pd.show();
                    break;
                case DELETED_IMAGE_ID:
                    pd.setTitle("Success");
                    pd.setMessage("Image has been removed");
                    pd.setCancelable(true);
                    pd.show();
                    break;
            }
        }
    }
}
