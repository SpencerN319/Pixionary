package sb_3.pixionary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;


import sb_3.pixionary.Utilities.RequestLogin;


public class LoginActivity extends AppCompatActivity {
    //Local stuff
    private EditText Username;
    private EditText Password;
    private TextView Attempts;
    private Button Login;
    private Button CreateAccount;
    private int attemptsLeft = 10;
    private boolean success = false;

    EditText et_username, et_password, error_disp;
    private String username, password;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button crt_accnt = (Button) findViewById(R.id.button_createAccount);
        Button login = (Button) findViewById(R.id.bt_login);
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
                            Log.i("user error", password);
                            progressDialog.dismiss();
                            if(response.equals("success")){
                                returnUsernameAndFinish(username);
                            } else if (response.equals("invalid username or password")){
                                error_disp.setText(invalid);
                            } else {
                                error_disp.setText(invalid);
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
    }

    public void returnUsernameAndFinish(String username){
        Intent retInt = new Intent(LoginActivity.this, MainMenuActivity.class);
        retInt.putExtra("username", username);
        setResult(Activity.RESULT_OK, retInt);
        finish();
    }

    /**
     * Checks to make sure the username is valid
     *
     * @param string
     * @return
     */
    protected boolean validateUsername(String string) {
        if(string == ""){
            et_username.setError("Enter Username");
            return false;
        } else if(string.length() > 20){
            et_username.setError("Max 20 Characters");
            return false;
        } else if(string.length() < 4){
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
        if(string.equals("")){
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

    public void moveToCreateAccount() {
        Intent move = new Intent(LoginActivity.this,CreateAccountActivity.class);
        startActivity(move);
    }
}
