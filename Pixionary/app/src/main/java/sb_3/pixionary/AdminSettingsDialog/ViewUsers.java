package sb_3.pixionary.AdminSettingsDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import sb_3.pixionary.Utilities.AdminSettings.RequestViewUsers;

public class ViewUsers extends Activity {

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

        for (TextView user: users) {
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.tv_0:
                            if(users[0].getText() != null){
                                request_user(0);
                            }
                            break;
                        case R.id.tv_1:
                            if(users[1].getText() != null){
                                request_user(1);
                            }
                            break;
                        case R.id.tv_2:
                            if(users[2].getText() != null){
                                request_user(2);
                            }
                            break;
                        case R.id.tv_3:
                            if(users[3].getText() != null){
                                request_user(3);
                            }
                            break;
                        case R.id.tv_4:
                            if(users[4].getText() != null){
                                request_user(4);
                            }
                            break;
                        case R.id.tv_5:
                            if(users[5].getText() != null){
                                request_user(5);
                            }
                            break;
                        case R.id.tv_6:
                            if(users[6].getText() != null){
                                request_user(6);
                            }
                            break;
                        case R.id.tv_7:
                            if(users[7].getText() != null){
                                request_user(7);
                            }
                            break;
                        case R.id.tv_8:
                            if(users[8].getText() != null){
                                request_user(8);
                            }
                            break;
                        case R.id.tv_9:
                            if(users[9].getText() != null){
                                request_user(9);
                            }
                            break;
                    }
                }
            });
        }

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


    private void request_user(int user_num){
        Intent show = new Intent(this, ViewSelectedUser.class);
        show.putExtra("user", users[user_num].getText());
        startActivity(show);
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
                        users[i].setText(user_array.getJSONObject(i).getString("username"));
                    }
                    if(length < 10){
                        for(int i = length; i < 10; i++){
                            users[i].setText(null);
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