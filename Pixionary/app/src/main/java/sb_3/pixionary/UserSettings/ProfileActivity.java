package sb_3.pixionary.UserSettings;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.Adapters.ProfileAdapter;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.RequestProfile;

public class ProfileActivity extends AppCompatActivity {

    private Context context;
    private RequestQueue requestQueue;
    private ListView profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        requestQueue = Volley.newRequestQueue(this);
        profileList = (ListView) findViewById(R.id.list_profile);
        getProfileData();
    }

    private void getProfileData() {
        UserDataDBHandler db = new UserDataDBHandler(this);
        User user = db.getUser("0");
        if(user != null) {
            String username = user.getUsername();
            Log.i("ProfileActivity", username);
            final RequestProfile profileRequest = new RequestProfile(username, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i("ProfileActivity", response);
                        JSONObject jsonProfile = new JSONObject(response);
                        if(jsonProfile.getBoolean("success")) {
                            ArrayList<String> profileData = new ArrayList<>();
                            profileData.add(jsonProfile.getString("username"));
                            profileData.add(jsonProfile.getString("password"));
                            profileData.add(jsonProfile.getString("user_id"));
                            profileData.add(jsonProfile.getString("user_type"));
                            profileData.add(jsonProfile.getString("score"));
                            profileData.add(jsonProfile.getString("games_played"));

                            ProfileAdapter adapter = new ProfileAdapter(context, R.layout.profile_layout, profileData);
                            profileList.setAdapter(adapter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            requestQueue.add(profileRequest);
        }
    }
}
