package sb_3.pixionary.AdminSettingsDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import sb_3.pixionary.MainMenuActivity;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.RequestPlaylists;

public class Images extends AppCompatActivity {
    private static final int ADD_RESULT_ID = 1;
    private RequestQueue requestQueue;
    private int pageNum = 0;
    private Button next, previous, add;
    TextView categories[] = new TextView[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        add = (Button) findViewById(R.id.bt_Add);
        next = (Button) findViewById(R.id.bt_Next);
        previous = (Button) findViewById(R.id.bt_Previous);
        categories[0] = (TextView) findViewById(R.id.tv_0);
        categories[1] = (TextView) findViewById(R.id.tv_1);
        categories[2] = (TextView) findViewById(R.id.tv_2);
        categories[3] = (TextView) findViewById(R.id.tv_3);
        categories[4] = (TextView) findViewById(R.id.tv_4);
        categories[5] = (TextView) findViewById(R.id.tv_5);
        categories[6] = (TextView) findViewById(R.id.tv_6);
        categories[7] = (TextView) findViewById(R.id.tv_7);
        categories[8] = (TextView) findViewById(R.id.tv_8);
        categories[9] = (TextView) findViewById(R.id.tv_9);

        requestQueue = Volley.newRequestQueue(this);

        request_categories();

        for (TextView user: categories) {
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.tv_0:
                            if(categories[0].getText() != null){

                                request_category(0);
                            }
                            break;
                        case R.id.tv_1:
                            if(categories[1].getText() != null){
                                request_category(1);
                            }
                            break;
                        case R.id.tv_2:
                            if(categories[2].getText() != null){
                                request_category(2);
                            }
                            break;
                        case R.id.tv_3:
                            if(categories[3].getText() != null){
                                request_category(3);
                            }
                            break;
                        case R.id.tv_4:
                            if(categories[4].getText() != null){
                                request_category(4);
                            }
                            break;
                        case R.id.tv_5:
                            if(categories[5].getText() != null){
                                request_category(5);
                            }
                            break;
                        case R.id.tv_6:
                            if(categories[6].getText() != null){
                                request_category(6);
                            }
                            break;
                        case R.id.tv_7:
                            if(categories[7].getText() != null){
                                request_category(7);
                            }
                            break;
                        case R.id.tv_8:
                            if(categories[8].getText() != null){
                                request_category(8);
                            }
                            break;
                        case R.id.tv_9:
                            if(categories[9].getText() != null){
                                request_category(9);
                            }
                            break;
                    }
                }
            });
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Images.this, AddCategory.class);
                startActivityForResult(intent,ADD_RESULT_ID);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;
                request_categories();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                request_categories();
            }
        });
    }


    private void request_category(int category_num){
        Log.i("CATEGORY REQUESTED", (String) categories[category_num].getText());
        Intent show = new Intent(this, ImagesViewCategory.class);
        show.putExtra("category", categories[category_num].getText());
        startActivity(show);
    }

    private void request_categories(){
        RequestPlaylists categoryRequest = new RequestPlaylists(MainMenuActivity.user.getUsername(), pageNum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonPlaylists = new JSONObject(response); //Gets the response
                    if(jsonPlaylists.getBoolean("success")) {
                        pageLogic(jsonPlaylists.getInt("total")); //Enables or disables buttons according to total users.
                        JSONArray jsonPlaylistArr = jsonPlaylists.getJSONArray("data"); //This might be changing.
                        for (int i = 0; i < jsonPlaylistArr.length(); i++) {
                            //This single line creates a Playlist object for every item in Json array.
                            JSONObject jsonObject = jsonPlaylistArr.getJSONObject(i);
                            categories[i].setText(jsonObject.getString("category"));
                            categories[i].setClickable(true);
                            if(jsonPlaylistArr.length() < 10){
                                for(int j = jsonPlaylistArr.length(); j < 10; j++){
                                    categories[j].setText("");
                                    categories[i].setClickable(false);
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error: " , error.toString());
            }
        });
        requestQueue.add(categoryRequest);
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
                case ADD_RESULT_ID:
                    pd.setTitle("Success");
                    pd.setMessage("Category created!");
                    pd.setCancelable(true);
                    pd.show();
                    break;
            }
        }
    }

}

