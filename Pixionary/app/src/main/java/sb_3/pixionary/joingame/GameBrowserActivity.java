package sb_3.pixionary.joingame;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import sb_3.pixionary.Adapters.GameListAdapter;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.POJO.GameClasses.ShortGame;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.RequestGamesAvailable;
import sb_3.pixionary.gameplay.GameActivity;


public class GameBrowserActivity extends AppCompatActivity {

    private static final String TAG = GameBrowserActivity.class.getSimpleName();
    Context context;
    private RequestQueue requestQueue;

    private GameListAdapter adapter;
    private ListView listView;
    private Button next;
    private Button previous;
    private Button reconnect;

    private int pageNum;
    private ArrayList<ShortGame> gamesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_browser);
        context = this;
        requestQueue = Volley.newRequestQueue(context);

        listView = (ListView) findViewById(R.id.gameList);
        previous = (Button) findViewById(R.id.previous_gameS);
        next = (Button) findViewById(R.id.next_gameS);
        reconnect = (Button) findViewById(R.id.btn_reconnect);

        gamesAvailablePage();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;
                gamesAvailablePage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                gamesAvailablePage();
            }
        });

        reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("reconnect", "true");
                startActivity(intent);
                finish();
            }
        });

    }

    private void gamesAvailablePage() {
        UserDataDBHandler db = new UserDataDBHandler(context);
        User user = db.getUser("0");
        if (user != null) {
            final String username = user.getUsername();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //Request the different category names. -- Should receive a list of 10 possible choices
                    RequestGamesAvailable categoryRequest = new RequestGamesAvailable(username, pageNum, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i(TAG, response);
                                JSONObject jsonGameList = new JSONObject(response); //Gets the response
                                if (jsonGameList.getBoolean("success")) {
                                    pageLogic(jsonGameList.getInt("total")); //Enables or disables buttons according to total users.
                                    JSONArray jsonGameArr = jsonGameList.getJSONArray("data"); //This might be changing.
                                    gamesList = new ArrayList<>();
                                    for (int i = 0; i < jsonGameArr.length(); i++) {
                                        ShortGame shortGame = new ShortGame();
                                        shortGame.setShortGameFromJSON(jsonGameArr.getJSONObject(i));
                                        gamesList.add(shortGame);
                                    }
                                    adapter = new GameListAdapter(context, gamesList);
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
            };
            handler.post(runnable);

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


