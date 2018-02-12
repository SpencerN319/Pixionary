package sb_3.pixionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import sb_3.pixionary.Utilities.RequestRegister;


public class CreateAccountActivity extends AppCompatActivity {

    private Button Create;
    private EditText Username, Password, Conf_Password;
    private TextView error_disp;

    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Create = (Button) findViewById(R.id.btnCreate);
        error_disp = (TextView) findViewById(R.id.tvExcited);
        requestQueue = Volley.newRequestQueue(CreateAccountActivity.this);
        final String success = String.format("%s", "Account Created!");
        final String fail = String.format("%s", "Error, Try Again");

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = (EditText) findViewById(R.id.etCreateUser);
                Password = (EditText) findViewById(R.id.etCreatePass);
                Conf_Password = (EditText) findViewById(R.id.etConfirmPass);
                //perform validation by calling all the validate functions inside the IF condition
                if ( validateUsername(Username.getText().toString()) && validatePassword(Password.getText().toString(),Conf_Password.getText().toString())) {
                    //Validation Success
                    RequestRegister requestRegister = new RequestRegister(Username.getText().toString(), Password.getText().toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (new JSONObject(response).getBoolean("success")) {
                                    error_disp.setText(success);
                                    finish();
                                } else {
                                    error_disp.setText(fail);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    requestQueue.add(requestRegister);
                }
            }
        });

    }


    /*
    private void createUser(String user, String pass) {
        String newUser = "NewUser: " + user + " " + pass;
        //TODO Pass string to the server, if successful server will return value.
        boolean success = true;
        Log.i("CreateAccountActivity", "Username: " + Username.getText().toString() + "    Password: " + Password.getText().toString());
        if (success) {
            returnUsernameAndFinish(user);
        }
    }

    public void returnUsernameAndFinish(String username){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("username", username);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
    */

    /**
     * Checks to make sure the username is valid
     *
     * @param name
     * @return
     */
    protected boolean validateUsername(String name) {
        if(name == ""){
            Username.setError("Enter Username");
            return false;
        } else if(name.length() > 20){
            Username.setError("Max 20 Characters");
            return false;
        } else if(name.length() < 6){
            Username.setError("Minimum 6 Characters");
            return false;
        }

        //Check if username already exists
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
            Password.setError("Enter Password");
            return false;
        } else if(pass1.length() < 6){
            Password.setError("Minimum 6 Characters");
            return false;
        } else if(pass1.length() > 8){
            Password.setError("Max 8 Characters");
            return false;
        } else if (!(pass1.equals(pass2))){
            Password.setError("Passwords Don't Match");
        }
        return true;
    }

}
