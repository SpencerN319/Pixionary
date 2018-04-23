package sb_3.pixionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.MainMenu.RequestLogin;

public class LoginActivity extends AppCompatActivity {

    EditText et_username, et_password, error_disp;
    private String username, password;
    private UserDataDBHandler db;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button crt_accnt = (Button) findViewById(R.id.button_create_account);
        Button login = (Button) findViewById(R.id.bt_login);
        Button guest = (Button) findViewById(R.id.bt_guest);
        et_username = (EditText) findViewById(R.id.editText_username);
        et_password = (EditText) findViewById(R.id.editText_password);
        error_disp = (EditText) findViewById(R.id.error_window);
        final String invalid = String.format("%s", "invalid username or password");
        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                if(validateUsername(username) && validatePassword(password)) {

                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Logging In");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    RequestLogin loginRequest = new RequestLogin(username, password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                progressDialog.dismiss();
                                if(jsonObject.getBoolean("success")){
                                    saveLoginData(jsonObject);
                                    returnUsernameAndFinish(username);
                                } else {
                                    error_disp.setText(invalid);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error_disp.setVisibility(View.VISIBLE);
                            if (error instanceof ServerError)
                                error_disp.setText("Server Error");
                            else if (error instanceof TimeoutError)
                                error_disp.setText("Connection Timed Out");
                            else if (error instanceof NetworkError)
                                error_disp.setText("Bad Network Connection");
                        }
                    });
                    requestQueue.add(loginRequest);
                }
            }
        });

        crt_accnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCreateAccount();
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_username.getText().toString();
                final String username_error = String.format("%s", "Please type a guest username");
                if(validateUsername(username)){
                    create_guest(username);
                } else {
                    error_disp.setText(username_error);
                }
            }
        });
    }

    public void returnUsernameAndFinish(String user_name){
        Intent retInt = new Intent(LoginActivity.this, MainMenuActivity.class);
        retInt.putExtra("username", user_name);
        setResult(MainMenuActivity.LOGIN_REQUEST_ID, retInt);
        finish();
    }

    /**
     * Checks to make sure the username is valid
     *
     * @param string
     * @return
     */
    protected boolean validateUsername(String string) {
        if(string == null){
            et_username.setError("Enter Username");
            return false;
        } else if(string.length() > 12){
            et_username.setError("Max 12 Characters");
            return false;
        } else if(string.length() < 6){
            et_username.setError("Minimum 6 Characters");
            return false;
        }
        return true;

    }

    /**
     *  Checks to make sure the password is valid
     * @param string
     * @return
     */
    protected boolean validatePassword(String string){
        if(string == null){
            et_password.setError("Enter Password");
            return false;
        } else if(string.length() < 6){
            et_password.setError("Minimum 6 Characters");
            return false;
        } else if(string.length() > 24){
            et_password.setError("Max 24 Characters");
            return false;
        }
        return true;
    }

    private void saveLoginData(JSONObject jsonObject) throws JSONException {
        db = new UserDataDBHandler(this);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String user_id = jsonObject.getString("user_id");
        String user_type = jsonObject.getString("user_type");
        int games_played = jsonObject.getInt("games_played");
        int score = jsonObject.getInt("score");
        int category_count = jsonObject.getInt("category_count");
        int image_count = jsonObject.getInt("image_count");
        User user = new User(username, password, user_id, user_type, games_played, score, category_count, image_count);
        MainMenuActivity.set_user(user);
        db.addUser(user);
    }

    private void moveToCreateAccount() {
        Intent move = new Intent(LoginActivity.this,CreateAccountActivity.class);
        startActivityForResult(move, MainMenuActivity.CREATEACCOUNT_REQUEST_ID);
    }

    private void create_guest(String user_name){
        User user = new User(user_name, null, null, "guest", 0, 0, 0, 0);
        MainMenuActivity.set_user(user);
        Intent retInt = new Intent(LoginActivity.this, MainMenuActivity.class);
        retInt.putExtra("username", "Guest_"+user_name);
        setResult(MainMenuActivity.GUEST_REQUEST_ID, retInt);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedData){
        if(returnedData == null){
            return;
        }
        super.onActivityResult(requestCode, resultCode, returnedData);
        switch (requestCode){
            case MainMenuActivity.CREATEACCOUNT_REQUEST_ID:
                username = returnedData.getStringExtra("username");
                Intent retInt = new Intent(LoginActivity.this, MainMenuActivity.class);
                retInt.putExtra("username", username);
                setResult(MainMenuActivity.LOGIN_REQUEST_ID, retInt);
                finish();
        }
    }

}
