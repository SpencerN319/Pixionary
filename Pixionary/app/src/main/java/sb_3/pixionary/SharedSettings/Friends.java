package sb_3.pixionary.SharedSettings;

import android.app.ProgressDialog;
import android.content.Intent;
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

import sb_3.pixionary.MainMenuActivity;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.Settings.RequestViewFriends;

public class Friends extends AppCompatActivity {

    private static final int ADD_FRIEND_ID = 2;
    private RequestQueue requestQueue;
    private int pageNum = 0;
    private Button next, previous, add, requests;
    TextView users[] = new TextView[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        next = (Button) findViewById(R.id.bt_Next);
        previous = (Button) findViewById(R.id.bt_Previous);
        add = (Button) findViewById(R.id.bt_add);
        requests = (Button) findViewById(R.id.bt_requests);
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

        request_friends();

        for (TextView user: users) {
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.tv_0:
                            if(users[0].getText() != null){
                                request_friend(0);
                            }
                            break;
                        case R.id.tv_1:
                            if(users[1].getText() != null){
                                request_friend(1);
                            }
                            break;
                        case R.id.tv_2:
                            if(users[2].getText() != null){
                                request_friend(2);
                            }
                            break;
                        case R.id.tv_3:
                            if(users[3].getText() != null){
                                request_friend(3);
                            }
                            break;
                        case R.id.tv_4:
                            if(users[4].getText() != null){
                                request_friend(4);
                            }
                            break;
                        case R.id.tv_5:
                            if(users[5].getText() != null){
                                request_friend(5);
                            }
                            break;
                        case R.id.tv_6:
                            if(users[6].getText() != null){
                                request_friend(6);
                            }
                            break;
                        case R.id.tv_7:
                            if(users[7].getText() != null){
                                request_friend(7);
                            }
                            break;
                        case R.id.tv_8:
                            if(users[8].getText() != null){
                                request_friend(8);
                            }
                            break;
                        case R.id.tv_9:
                            if(users[9].getText() != null){
                                request_friend(9);
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
                request_friends();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                request_friends();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Friends.this, AddFriend.class);
                startActivityForResult(intent, ADD_FRIEND_ID);
            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Friends.this, FriendRequests.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }


    private void request_friend(int user_num){
        Intent show = new Intent(this, ViewSelectedFriend.class);
        show.putExtra("friend", users[user_num].getText());
        startActivity(show);
    }

    private void request_friends(){
        RequestViewFriends view = new RequestViewFriends(MainMenuActivity.user.getUsername(), pageNum, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("success")){
                        pageLogic(obj.getInt("total"));
                        JSONArray user_array = obj.getJSONArray("friends");
                        int length = user_array.length();
                        for(int i = 0; i < length; i++) {
                            users[i].setText(user_array.getString(i));
                            users[i].setClickable(true);
                        }
                        if(length < 10){
                            for(int i = length; i < 10; i++){
                                users[i].setText(null);
                                users[i].setClickable(false);
                            }
                        }
                    } else {
                        for(int i = 0; i < 10; i++){
                            users[i].setText("");
                            users[i].setClickable(false);
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
        if (totalUsers > 10) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedData){
        Log.i("ACTIVITY RESULT", ""+resultCode);
        final ProgressDialog pd = new ProgressDialog(this);
        if(resultCode == -1 || returnedData == null){
            request_friends();
            return;
        }
        super.onActivityResult(requestCode, resultCode, returnedData);
        switch (requestCode){

            case ADD_FRIEND_ID:
                String friends_name = returnedData.getStringExtra("friends_name");
                if (friends_name != null) {
                    pd.setTitle("Success");
                    pd.setMessage("Friend request sent to: " + friends_name);
                    pd.show();
                    pd.setCancelable(true);
                }
                break;
        }
    }
}
