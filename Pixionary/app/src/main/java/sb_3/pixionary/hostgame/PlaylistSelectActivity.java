package sb_3.pixionary.hostgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import sb_3.pixionary.interfaces.DataTransferInterface;

public class PlaylistSelectActivity extends AppCompatActivity implements DataTransferInterface {
    private static final String TAG = PlaylistSelectActivity.class.getSimpleName();
    private DataTransferInterface dataTransferInterface;
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
        context = PlaylistSelectActivity.this;
        dataTransferInterface = this;
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
                                JSONObject jsonObject = jsonPlaylistArr.getJSONObject(i);
                                playlistsList.add(new Playlist(jsonObject.getString("category"), jsonObject.getString("image")));

                                Log.i(TAG, jsonPlaylistArr.getString(i));
                            }
                            adapter = new PlaylistsAdapter(context, playlistsList, dataTransferInterface);
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

    //TODO possibly add some sort of alpha to the color of these when they are disabled.
    private void disableButton(Button button) { button.setEnabled(false); }

    private void enableButton(Button button) { button.setEnabled(true); }

    @Override
    public void setValuesAndReact(int position) {
        sendResultingPlaylist(playlistsList.get(position));
    }

    private void sendResultingPlaylist(Playlist playlist) {
        Intent intent = new Intent(context, HostGameActivity.class);
        intent.putExtra("PlaylistName", playlist.getName());
        intent.putExtra("ImagePreview", playlist.getUrl());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }



}
