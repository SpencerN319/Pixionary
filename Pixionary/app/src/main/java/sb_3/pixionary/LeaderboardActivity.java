package sb_3.pixionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.Locale;

public class LeaderboardActivity extends AppCompatActivity {
    private static final String URL = "http://proj-309-sb-3.cs.iastate.edu:80/leaderboard.php";
    private TextView data  = (TextView) findViewById(R.id.tvData);
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        String username; int score; int games_played;
        data.setMovementMethod(new ScrollingMovementMethod());
        String all_data = "";
        for(int i = 0; i < 100; i++){
            username = "john";
            score = i;
            games_played = i;
            String data_string = String.format(Locale.US,"%-22s%%-22d%-22d", username, score, games_played);
            all_data += data_string + "\n";
        }
        data.setText(all_data);
        /*
        requestQueue = Volley.newRequestQueue(LeaderboardActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.i("TAG1", response.getString("data"));
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
        requestQueue.add(jsonObjectRequest);*/
    }
}
