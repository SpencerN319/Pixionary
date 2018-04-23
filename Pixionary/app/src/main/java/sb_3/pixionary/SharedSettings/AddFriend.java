package sb_3.pixionary.SharedSettings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import sb_3.pixionary.MainMenuActivity;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.Settings.RequestAddFriend;

public class AddFriend extends Activity {

    private EditText friend_name;
    private Button add;
    private String name;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        friend_name = (EditText) findViewById(R.id.et_FriendName);
        add = (Button) findViewById(R.id.bt_add);
        requestQueue = Volley.newRequestQueue(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateUsername(friend_name.getText().toString())){
                    name = friend_name.getText().toString();
                    RequestAddFriend add_friend = new RequestAddFriend(MainMenuActivity.user.getUsername(), name, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("success")){
                                Intent intent = new Intent();
                                intent.putExtra("friends_name", name);
                                setResult(2,intent);
                                finish();
                            } else if(response.equals("exists")){
                                final ProgressDialog pd = new ProgressDialog(AddFriend.this);
                                pd.setTitle("Error");
                                pd.setMessage("request has already been sent");
                                pd.show();
                                pd.setCancelable(true);
                            }
                        }
                    }); requestQueue.add(add_friend);
                }

            }
        });


    }

    /**
     * Checks to make sure the username is valid
     *
     * @param string
     * @return
     */
    protected boolean validateUsername(String string) {
        if(string == null){
            friend_name.setError("Enter Username");
            return false;
        } else if(string.length() > 12){
            friend_name.setError("Max 12 Characters");
            return false;
        } else if(string.length() < 6){
            friend_name.setError("Minimum 6 Characters");
            return false;
        }
        return true;

    }
}
