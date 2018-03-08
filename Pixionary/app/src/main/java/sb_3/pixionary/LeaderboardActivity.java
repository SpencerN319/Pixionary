package sb_3.pixionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import sb_3.pixionary.Utilities.RequestLeaderboard;

public class LeaderboardActivity extends AppCompatActivity {
    private static final String URL = "http://proj-309-sb-3.cs.iastate.edu:80/leaderboard.php";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        requestQueue = Volley.newRequestQueue(LeaderboardActivity.this);

        RequestLeaderboard jsonObjectRequest = new RequestLeaderboard( null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.i("TAG1", response.toString());
                            //Read all the values into an array of users.

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG2", "Error :" + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
