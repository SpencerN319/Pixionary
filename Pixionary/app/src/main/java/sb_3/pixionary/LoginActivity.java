package sb_3.pixionary;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private String username = null;
    private String password = null;
    private String[] upPair = new String[2];
    private Button crt_accnt = (Button) findViewById(R.id.button_createAccount);
    private Button login = (Button) findViewById(R.id.button_login);
    EditText usernameTextbox = (EditText) findViewById(R.id.editText_username);
    EditText passwordTextbox = (EditText) findViewById(R.id.editText_password);
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* SAMPLE CODE THAT NEEDS TO BE CHANGED */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameTextbox.getText().toString();
                password = passwordTextbox.getText().toString();
                if (validateUsername(username) && validatePassword(password)) {

                    LoginRequest loginRequest = new LoginRequest(username, password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    Intent loginSuccess = new Intent(LoginActivity.this, MainMenuActivity.class);
                                    //Passing all received data from server to next activity
                                    loginSuccess.putExtra("name", jsonObject.getString("name"));
                                    startActivity(loginSuccess);
                                    finish();
                                } else {
                                    if(jsonObject.getString("status").equals("INVALID")) {
                                        usernameTextbox.setError("Invalid Username");
                                    } else{
                                        passwordTextbox.setError("Invalid Password");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Make into popup widget
                                usernameTextbox.setError("Bad Response From Server");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //MAKE POPUPS
                            if (error instanceof ServerError)

                                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            else if (error instanceof TimeoutError)
                                Toast.makeText(LoginActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                            else if (error instanceof NetworkError)
                                Toast.makeText(LoginActivity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(loginRequest);
                }
            }
        });

















        crt_accnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method to send new User data to the server... eventually
                moveToCreateAccount();
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
        if(string == ""){
            usernameTextbox.setError("Enter Username");
            return false;
        } else if(string.length() > 20){
            usernameTextbox.setError("Max 20 Characters");
            return false;
        } else if(string.length() < 6){
            usernameTextbox.setError("Minimum 6 Characters");
            return false;
        } else {
            return true;
        }
    }

    /**
     *  Checks to make sure the password is valid
     * @param string
     * @return
     */
    protected boolean validatePassword(String string){
        if(string.equals("")){
            passwordTextbox.setError("Enter Password");
            return false;
        } else if(string.length() < 6){
            passwordTextbox.setError("Minimum 6 Characters");
            return false;
        } else if(string.length() > 8){
            passwordTextbox.setError("Max 8 Characters");
            return false;
        } else {
            return true;
        }
    }

    public void returnLoginInfo(View view) {
        upPair[0] = username;
        upPair[1] = password;
        Intent retval = new Intent();
        retval.putExtra("upPair",upPair);
        setResult(RESULT_OK, retval);
        finish();
    }

    public void moveToCreateAccount() {
        Intent move = new Intent(LoginActivity.this,CreateAccountActivity.class);
        startActivity(move);
    }
}
