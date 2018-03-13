package sb_3.pixionary.hostgame;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import sb_3.pixionary.Adapters.PlaylistsAdapter;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.Playlist;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.RequestPlaylists;

public class CategoriesSelectActivity extends AppCompatActivity {
    private static final String TAG = CategoriesSelectActivity.class.getSimpleName();
    private Context context;
    private RequestQueue requestQueue;

    private PlaylistsAdapter adapter;
    private ListView listView;
    private Button previous;
    private Button next;

    private int pageNum = 0;
    private ArrayList<Playlist> playlistsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_select);
        context = CategoriesSelectActivity.this;
        //Initialize new RequestQueue
        requestQueue = Volley.newRequestQueue(context);

        listView = (ListView) findViewById(R.id.categories_list);
        previous = (Button) findViewById(R.id.previous_btn);
        next = (Button) findViewById(R.id.next_btn);

        requestPlaylistsPage();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;
                requestPlaylistsPage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                requestPlaylistsPage();
            }
        });
    }

    //We could clean both this and leaderboard page up by making a class specifically for the both of them?
    private void requestPlaylistsPage() {
        UserDataDBHandler db = new UserDataDBHandler(context);
        User user = db.getUser("0");
        if(user != null) {
            String username = user.getUsername();
            //Request the different category names. -- Should receive a list of 10 possible choices.
            RequestPlaylists categoryRequest = new RequestPlaylists(username, pageNum, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        Log.i(TAG, response);
                        JSONObject jsonPlaylists = new JSONObject(response); //Gets the response
                        if(jsonPlaylists.getBoolean("success")) {
                            pageLogic(jsonPlaylists.getInt("total")); //Enables or disables buttons according to total users.
                            JSONArray jsonPlaylistArr = jsonPlaylists.getJSONArray("data"); //This might be changing.
                            playlistsList = new ArrayList<>();
                            for (int i = 0; i < jsonPlaylistArr.length(); i++) {
                                //This single line creates a Playlist object for every item in Json array.
                                playlistsList.add(new Playlist(jsonPlaylistArr.getJSONObject(i)));
                            }
                            adapter = new PlaylistsAdapter(context, playlistsList);
                            listView.setAdapter(adapter);
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
            requestQueue.add(categoryRequest);
        }
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
