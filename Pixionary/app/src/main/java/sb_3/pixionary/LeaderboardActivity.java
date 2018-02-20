package sb_3.pixionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LeaderboardActivity extends AppCompatActivity {
    private static final String URL = "http://proj-309-sb-3.cs.iastate.edu:80/leaderboard.php";
    private TextView user_data;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user_data = (TextView) findViewById(R.id.tv_leaderData);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        requestQueue = Volley.newRequestQueue(LeaderboardActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.i("TAG1", response.toString());
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
