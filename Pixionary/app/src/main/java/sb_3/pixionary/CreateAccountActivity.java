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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import sb_3.pixionary.Utilities.RequestRegister;


public class CreateAccountActivity extends AppCompatActivity {

    private EditText et_username, et_password, et_conf_password;
    private TextView error_disp;
    private String user_type = "general";
    private String username, password, conf_password;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button Create = (Button) findViewById(R.id.btnCreate);
        error_disp = (TextView) findViewById(R.id.tvExcited);
        et_username = (EditText) findViewById(R.id.etCreateUser);
        et_password = (EditText) findViewById(R.id.etCreatePass);
        et_conf_password = (EditText) findViewById(R.id.etConfirmPass);
        requestQueue = Volley.newRequestQueue(CreateAccountActivity.this);
        final String fail = String.format("%s", "Username Already Exists");

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                conf_password = et_conf_password.getText().toString();

                //Validate username and passwords
                if ( validateUsername(username) && validatePassword(password,conf_password)) {

                    final ProgressDialog progressDialog = new ProgressDialog(CreateAccountActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Registering New Account");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    RequestRegister requestRegister = new RequestRegister(username, password, user_type, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Create Account Response", response);
                            progressDialog.dismiss();
                            if(response.equals("success")){
                                returnUsernameAndFinish(username);
                            } else if(response.equals("username already exists")){
                                error_disp.setText(fail);
                            }

                            else {
                                progressDialog.show();
                                progressDialog.setMessage(response);
                            }
                        }
                    });
                    requestQueue.add(requestRegister);
                }
            }
        });

    }


    public void returnUsernameAndFinish(String username){
        Intent retInt = new Intent(CreateAccountActivity.this, MainMenuActivity.class);
        retInt.putExtra("username", username);
        setResult(Activity.RESULT_OK, retInt);
        finish();
    }



    /**
     * Checks to make sure the username is valid
     *
     * @param name
     * @return
     */
    protected boolean validateUsername(String name) {
        if(name == ""){
            et_username.setError("Enter Username");
            return false;
        } else if(name.length() > 20){
            et_username.setError("Max 20 Characters");
            return false;
        } else if(name.length() < 6){
            et_username.setError("Minimum 6 Characters");
            return false;
        }
        return true;
    }

    /**
     *  Checks to make sure the password is valid
     * @param pass1
     * @param pass2
     * @return
     */
    protected boolean validatePassword(String pass1, String pass2){
        if(pass1.equals("")){
            et_password.setError("Enter Password");
            return false;
        } else if(pass1.length() < 6){
            et_password.setError("Minimum 6 Characters");
            return false;
        } else if(pass1.length() > 24){
            et_password.setError("Max 8 Characters");
            return false;
        } else if (!(pass1.equals(pass2))){
            et_password.setError("Passwords Don't Match");
        }
        return true;
    }

}
