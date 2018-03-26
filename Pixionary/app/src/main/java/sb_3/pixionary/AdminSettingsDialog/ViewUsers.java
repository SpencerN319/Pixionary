package sb_3.pixionary.AdminSettingsDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.RequestViewUsers;

public class ViewUsers extends AppCompatActivity {

    private RequestQueue requestQueue;
    private int pageNum = 0;
    private Button next, previous;
    TextView users[] = new TextView[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        next = (Button) findViewById(R.id.bt_Next);
        previous = (Button) findViewById(R.id.bt_Previous);
        users[0] = (TextView) findViewById(R.id.tv_0);
        users[1] = (TextView) findViewById(R.id.tv_1);
        users[2] = (TextView) findViewById(R.id.tv_2);
        users[3] = (TextView) findViewById(R.id.tv_3);
        users[4] = (TextView) findViewById(R.id.tv_4);
        users[5] = (TextView) findViewById(R.id.tv_5);
        users[6] = (TextView) findViewById(R.id.tv_6);
        users[7] = (TextView) findViewById(R.id.tv_7);
        users[8] = (TextView) findViewById(R.id.tv_8);
        users[9] = (TextView) findViewById(R.id.tv_9);

        requestQueue = Volley.newRequestQueue(this);

        request_users();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;
                request_users();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                request_users();
            }
        });
    }

    private void request_users(){

        RequestViewUsers view = new RequestViewUsers(pageNum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pageLogic(obj.getInt("total"));
                    JSONArray  user_array = obj.getJSONArray("users");
                    int length = user_array.length();
                    for(int i = 0; i < length; i++) {
                        Log.i("username: ", user_array.getJSONObject(i).toString());
                        users[i].setText(user_array.getJSONObject(i).getString("username"));
                    }
                    if(length < 10){
                        for(int i = length; i < 10; i++){
                            users[i].setText("");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        requestQueue.add(view);
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

    private void disableButton(Button button){button.setEnabled(false);}

    private void enableButton(Button button){button.setEnabled(true);}
}