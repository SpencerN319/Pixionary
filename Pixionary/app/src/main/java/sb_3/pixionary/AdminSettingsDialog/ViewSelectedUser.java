package sb_3.pixionary.AdminSettingsDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.AdminSettings.RequestSelectedUser;

public class ViewSelectedUser extends Activity {
    private RequestQueue requestQueue;
    TextView usr, pass, id, type, gp, scr, cc, ic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_selected_user);
        Intent intent = getIntent();
        String username = intent.getStringExtra("user");

        usr = (TextView) findViewById(R.id.tv_username);
        pass = (TextView) findViewById(R.id.tv_password);
        id = (TextView) findViewById(R.id.tv_id);
        type = (TextView) findViewById(R.id.tv_userType);
        gp = (TextView) findViewById(R.id.tv_gamesPlayed);
        scr = (TextView) findViewById(R.id.tv_score);
        cc = (TextView) findViewById(R.id.tv_categoryCount);
        ic = (TextView) findViewById(R.id.tv_imageCount);

        requestQueue = Volley.newRequestQueue(this);

        RequestSelectedUser request = new RequestSelectedUser(username, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("success")){
                        usr.setText("Username: "+obj.getString("username"));
                        pass.setText("Password: "+obj.getString("password"));
                        id.setText("User ID: "+obj.getString("username"));
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
