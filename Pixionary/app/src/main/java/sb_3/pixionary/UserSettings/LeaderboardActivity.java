package sb_3.pixionary.UserSettings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.Adapters.LeaderboardAdapter;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.ShortUser;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.Settings.RequestLeaderboard;

public class LeaderboardActivity extends AppCompatActivity {
    private static final String URL = "http://proj-309-sb-3.cs.iastate.edu:80/leaderboard.php";
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private RequestQueue requestQueue;
    private ListView leaderboard_list;
    private ListView leaderboard_you;
    private Button previous;
    private Button next;
    private LeaderboardAdapter adapter;
    private ArrayList<ShortUser> leaderboardAL;
    private int pageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        context = this;
        leaderboard_list = (ListView) findViewById(R.id.leaderboard_list_view);
        leaderboard_you = (ListView) findViewById(R.id.leaderboard_you);
        previous = (Button) findViewById(R.id.previous);
        next = (Button) findViewById(R.id.next);

        requestQueue = Volley.newRequestQueue(LeaderboardActivity.this);

        requestLeaderboardPage();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;
                requestLeaderboardPage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                requestLeaderboardPage();
            }
        });
    }

    private boolean requestLeaderboardPage() {
        UserDataDBHandler db = new UserDataDBHandler(context);
        User user = db.getUser("0");
        if(user != null) {
            String username = user.getUsername();
            final RequestLeaderboard leaderboardRequest = new RequestLeaderboard(username, pageNum,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                Log.i(TAG, response);
                                JSONObject jsonLeaderboard = new JSONObject(response); //Gets the response
                                pageLogic(jsonLeaderboard.getInt("total")); //Enables or disables buttons according to total users.
                                if(jsonLeaderboard.getBoolean("success")) {
                                    JSONObject youjsonObject = jsonLeaderboard.getJSONObject("thisuser");
                                    setYouData(youjsonObject); //Sets your data.
                                    leaderboardAL = new ArrayList<>();
                                    JSONArray jsonUserArr = jsonLeaderboard.getJSONArray("data");
                                    for (int i = 0; i < jsonUserArr.length(); i++) {
                                        //This single line creates a ShortUser object from a response and adds to the ArrayList
                                        leaderboardAL.add(new ShortUser(jsonUserArr.getJSONObject(i)));
                                    }
                                    adapter = new LeaderboardAdapter(context, R.layout.leaderboard_adapter, leaderboardAL);
                                    leaderboard_list.setAdapter(adapter);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "Error: " + error.toString());
                }
            });
            requestQueue.add(leaderboardRequest);
            return true;
        }
        return false;
    }

    private void setYouData(JSONObject you) {
        ArrayList<ShortUser> sendMe = new ArrayList<>();
        sendMe.add(new ShortUser(you));
        LeaderboardAdapter adaptMe = new LeaderboardAdapter(context, R.layout.leaderboard_adapter, sendMe);
        leaderboard_you.setAdapter(adaptMe);
    }

    private void pageLogic(int totalUsers) {
        if (totalUsers > 10) { //Will be changing 10 to 100.
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

    private void disableButton(Button button) {
        button.setEnabled(false);

    }

    private void enableButton(Button button) {
        button.setEnabled(true);
    }
}
