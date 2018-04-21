package sb_3.pixionary.SharedSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.Settings.RequestViewSelectedFriend;

public class ViewSelectedFriend extends Activity {

    private RequestQueue requestQueue;
    TextView usr, id, type, gp, scr, cc, ic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selected_friend);
        Intent intent = getIntent();
        String friend = intent.getStringExtra("friend");

        usr = (TextView) findViewById(R.id.tv_username);
        type = (TextView) findViewById(R.id.tv_userType);
        gp = (TextView) findViewById(R.id.tv_gamesPlayed);
        scr = (TextView) findViewById(R.id.tv_score);
        cc = (TextView) findViewById(R.id.tv_categoryCount);
        ic = (TextView) findViewById(R.id.tv_imageCount);

        requestQueue = Volley.newRequestQueue(this);

        RequestViewSelectedFriend request = new RequestViewSelectedFriend(friend, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("success")){
                        usr.setText("Username: "+obj.getString("username"));
                        type.setText("User Type: "+obj.getString("user_type"));
                        gp.setText("Games Played: "+obj.getInt("games_played"));
                        scr.setText("Score: "+obj.getInt("score"));
                        cc.setText("Category Count: "+obj.getInt("category_count"));
                        ic.setText("Image Count: "+obj.getInt("image_count"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        requestQueue.add(request);
    }
}

