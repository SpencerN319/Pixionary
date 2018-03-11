package sb_3.pixionary;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.Adapters.ListAdapter;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.RequestCategories;

public class HostGameActivity extends AppCompatActivity {

    private static final String TAG = HostGameActivity.class.getSimpleName();
    private Context context;
    private Activity activity;
    private RequestQueue requestQueue;

    private String nameGame;
    private String gameID;
    private static final String url = ""; //Declare some url here.
    private int pageNum = 0;

    private EditText nameET;
    private Button categorySelection;
    private Button playAI;
    private Button play1v1;

    private ListAdapter adapter;
    private ListView listView;
    private Button previous;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);
        context = HostGameActivity.this;
        //Initialize new RequestQueue
        requestQueue = Volley.newRequestQueue(context);

        nameET = (EditText) findViewById(R.id.et_game_name);
        categorySelection = (Button) findViewById(R.id.button_category);
        listView = (ListView) findViewById(R.id.categories_list);
        previous = (Button) findViewById(R.id.previous_btn);
        next = (Button) findViewById(R.id.next_btn);
        playAI = (Button) findViewById(R.id.button_play_ai);
        play1v1 = (Button) findViewById(R.id.button_play_1v1);

        categorySelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_host_game_categories);
                requestCategoriesPage();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;
                requestCategoriesPage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                requestCategoriesPage();
            }
        });
    }
    //We could clean both this and leaderboard page up by making a class specifically for the both of them?
    private void requestCategoriesPage() {
        UserDataDBHandler db = new UserDataDBHandler(context);
        User user = db.getUser("0");
        if(user != null) {
            String username = user.getUsername();
            //Request the different category names. -- Should receive a list of 10 possible choices.
            RequestCategories categoryRequest = new RequestCategories(username, pageNum, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        Log.i(TAG, response);
                        JSONObject jsonLeaderboard = new JSONObject(response); //Gets the response
                        if(jsonLeaderboard.getBoolean("success")) {
                            pageLogic(jsonLeaderboard.getInt("total")); //Enables or disables buttons according to total users.
                            JSONArray jsonCategoriesArr = jsonLeaderboard.getJSONArray("data"); //This might be changing.
                            ArrayList<String> categoryList = new ArrayList<>();
                            for (int i = 0; i < jsonCategoriesArr.length(); i++) {
                                //This single line creates a ShortUser object from a response and adds to the ArrayList
                                categoryList.add(jsonCategoriesArr.getString(i));
                            }
                            adapter = new ListAdapter(context, R.layout.child_listview, R.id.textForBox, categoryList, "Preview");
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
