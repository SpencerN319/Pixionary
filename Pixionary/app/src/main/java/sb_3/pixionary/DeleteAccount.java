package sb_3.pixionary;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;


import sb_3.pixionary.Utilities.RequestDeleteAccount;

public class DeleteAccount extends AppCompatActivity {
    private String username, password, conf_password;
    Button delete;
    EditText user, pass1, pass2;
    CheckBox confirm;
    TextView warning;
    Boolean confirmed_delete = false;
    final String fail = String.format("%s", "Box not checked");
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        delete = (Button) findViewById(R.id.bt_DeleteAccount);
        user = (EditText) findViewById(R.id.et_Username);
        pass1 = (EditText) findViewById(R.id.et_Password);
        pass2 = (EditText) findViewById(R.id.et_ConfirmPassword);
        confirm = (CheckBox) findViewById(R.id.bx_ConfirmDelete);
        warning = (TextView) findViewById(R.id.tv_warning);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO if checked activate warning and set delete Button to clickable.
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                password = pass1.getText().toString();
                conf_password = pass2.getText().toString();

                //Validate username and passwords
                if ( validateUsername(username) && validatePassword(password,conf_password) && confirm.isChecked()) {

                    final ProgressDialog progressDialog = new ProgressDialog(DeleteAccount.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Deleting Account");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    RequestDeleteAccount requestDeleteAccount = new RequestDeleteAccount(username, password, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.equals("success")) {
                                //TODO logout and return to main menu
                            } else if (response.equals("invalid")) {
                                //TODO invalid username or password
                            } else {
                                progressDialog.show();
                                progressDialog.setMessage(response);
                            }
                        }
                    });


                    requestQueue.add(requestDeleteAccount);
                }
            }
        });




    }

    /**
     * Checks to make sure the username is valid
     *
     * @param name
     * @return
     */
    protected boolean validateUsername(String name) {
        if(name == null){
            user.setError("Enter Username");
            return false;
        } else if(name.length() > 24){
            user.setError("Max 12 Characters");
            return false;
        } else if(name.length() < 6){
            user.setError("Minimum 6 Characters");
            return false;
        }
        return true;
    }

    /**
     *  Checks to make sure the password is valid
     * @param one
     * @param two
     * @return
     */
    protected boolean validatePassword(String one, String two){
        if(pass1.equals("")){
            pass1.setError("Enter Password");
            return false;
        } else if(pass1.length() < 6){
            pass1.setError("Minimum 6 Characters");
            return false;
        } else if(pass1.length() > 24){
            pass1.setError("Max 8 Characters");
            return false;
        } else if (!(pass1.equals(pass2))){
            pass1.setError("Passwords Don't Match");
            return false;
        }
        return true;
    }
}
