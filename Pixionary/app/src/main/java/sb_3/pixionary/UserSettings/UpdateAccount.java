package sb_3.pixionary.UserSettings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.MainMenuActivity;
import sb_3.pixionary.R;
import sb_3.pixionary.Utilities.Settings.RequestUpdateAccount;

public class UpdateAccount extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Button confirm;
    private EditText user, pass1, pass2;
    private CheckBox cb_confirm, cb_user, cb_pass, cb_delete, cb_host;
    private TextView warning;
    private Boolean confirmed_delete = false;
    private Boolean update_pass = false;
    private Boolean update_user = false;
    private Boolean delete_acc = false;
    private Boolean host_request = false;
    private RequestQueue requestQueue;
    private String  username, password, conf_password, action, old_username, old_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        progressDialog = new ProgressDialog(this);
        confirm = (Button) findViewById(R.id.bt_confirm);
        user = (EditText) findViewById(R.id.et_Username);
        pass1 = (EditText) findViewById(R.id.et_Password);
        pass2 = (EditText) findViewById(R.id.et_ConfirmPassword);
        cb_confirm = (CheckBox) findViewById(R.id.cb_ConfirmDelete);
        cb_confirm.setClickable(false);
        cb_user = (CheckBox) findViewById(R.id.cb_username);
        cb_pass = (CheckBox) findViewById(R.id.cb_password);
        cb_delete = (CheckBox) findViewById(R.id.cb_delete);
        cb_host = (CheckBox) findViewById(R.id.cb_RequestHost);
        warning = (TextView) findViewById(R.id.tv_warning);
        requestQueue = Volley.newRequestQueue(this);
        action = "";

        if(MainMenuActivity.user.getUserType().equals("host_req") || MainMenuActivity.user.getUserType().equals("admin") || MainMenuActivity.user.getUserType().equals("host")){
            cb_host.setClickable(false);
            cb_host.setVisibility(View.INVISIBLE);
        }

        cb_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_user.isChecked()){
                    update_user = true;
                    delete_acc = false;
                    host_request = false;
                    cb_delete.setChecked(false);
                    warning.setVisibility(View.INVISIBLE);
                    cb_confirm.setChecked(false);
                    cb_confirm.setClickable(false);
                    cb_host.setChecked(false);
                    user.setHint("New Username");
                } else {
                    update_user = false;
                    user.setHint("Username");
                }

            }
        });

        cb_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_pass.isChecked()){
                    update_pass = true;
                    delete_acc = false;
                    host_request = false;
                    cb_host.setChecked(false);
                    cb_delete.setChecked(false);
                    cb_confirm.setChecked(false);
                    cb_confirm.setClickable(false);
                    warning.setVisibility(View.INVISIBLE);
                    pass1.setHint("New Password");
                } else{
                    update_pass = false;
                    pass1.setHint("Password");
                }

            }
        });

        cb_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_host.isChecked()){
                    host_request = true;
                    delete_acc = false;
                    update_pass = false;
                    update_user = false;
                    cb_pass.setChecked(false);
                    cb_user.setChecked(false);
                    cb_delete.setChecked(false);
                    cb_confirm.setClickable(false);
                    user.setHint("Username");
                    pass1.setHint("Password");
                } else {
                    host_request = false;
                }
            }
        });

        cb_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_delete.isChecked()){
                    delete_acc = true;
                    update_pass = false;
                    update_user = false;
                    host_request = false;
                    cb_host.setChecked(false);
                    cb_pass.setChecked(false);
                    cb_user.setChecked(false);
                    cb_confirm.setClickable(true);
                    user.setHint("Username");
                    pass1.setHint("Password");
                } else{
                    delete_acc = false;
                    cb_confirm.setChecked(false);
                    cb_confirm.setClickable(false);
                    warning.setVisibility(View.INVISIBLE);
                }

            }
        });

        cb_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_confirm.isChecked()){
                    warning.setVisibility(View.VISIBLE);
                    confirmed_delete = true;
                } else{
                    warning.setVisibility(View.INVISIBLE);
                    confirmed_delete = false;
                }
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete_acc && confirmed_delete) {
                    action += "delete";
                    old_username = MainMenuActivity.user.getUsername();
                    old_password = MainMenuActivity.user.getPassword();
                } else if(host_request){
                    action += "host";
                    old_username = MainMenuActivity.user.getUsername();
                    old_password = MainMenuActivity.user.getPassword();
                } else {
                    if(update_user){
                        action += "update username";
                        old_username = MainMenuActivity.user.getUsername();
                        old_password = MainMenuActivity.user.getPassword();}
                    if(update_pass){
                        action += " update password";
                        old_username = MainMenuActivity.user.getUsername();
                        old_password = MainMenuActivity.user.getPassword();}
                }
                username = user.getText().toString();
                password = pass1.getText().toString();
                conf_password = pass2.getText().toString();
                if (validateUsername(username) && validatePassword(password, conf_password)) {
                    display_dialog(action);
                    RequestUpdateAccount updateRequest = new RequestUpdateAccount(username, password, old_username, old_password, action, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.equals("success")) {
                                if (action.equals("delete")) {
                                    delete_db();
                                } else if(action.equals("host")){
                                    update_and_return(username, password, action);
                                } else{
                                    update_and_return(username, password, action);
                                }
                            } else {
                                progressDialog.setMessage(response);
                                progressDialog.setCancelable(true);
                                progressDialog.show();
                            }
                        }
                    });
                    requestQueue.add(updateRequest);

                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(UpdateAccount.this);
                    progressDialog.setTitle("Error");
                    progressDialog.setMessage("Checkbox must be checked to delete account");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }
            }
        });

    }

    private void update_and_return(String new_user, String new_pass, String action){
        UserDataDBHandler db = new UserDataDBHandler(this);
        db.deleteOne(0);
        if(action.equals("host")){
            MainMenuActivity.user.setUserType("host_req");
        } else if(action.equals("update username")){
            MainMenuActivity.user.setUsername(new_user);
        } else if(action.equals(" update password")){
            MainMenuActivity.user.setPassword(new_pass);
        } else if(action.equals("update username update password")){
            MainMenuActivity.user.setUsername(new_user);
            MainMenuActivity.user.setPassword(new_pass);
        }
        db.addUser(MainMenuActivity.user);
        Intent complete = new Intent(this, SettingsDialog.class);
        complete.putExtra("action", action);
        setResult(2, complete);
        finish();
    }

    private void display_dialog(String action){
        if(action.equals("delete")){
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Deleting Account");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else{
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Updating Account");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private void delete_db(){
        MainMenuActivity.set_user(null);
        UserDataDBHandler db = new UserDataDBHandler(this);
        db.deleteOne(0);
        Intent complete = new Intent(this, SettingsDialog.class);
        complete.putExtra("action", "delete");
        setResult(2, complete);
        finish();
    }

    /**
     * Checks to make sure the username is valid
     *
     * @param name
     * @return
     */
    private boolean validateUsername(String name) {
        if(name == null){
            user.setError("Enter Username");
            return false;
        } else if(name.length() > 12){
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
    private boolean validatePassword(String one, String two){
        if(one == null || two == null){
            pass1.setError("Enter Password");
            return false;
        } else if(one.length() < 6){
            pass1.setError("Minimum 6 Characters");
            return false;
        } else if(one.length() > 24){
            pass1.setError("Max 8 Characters");
            return false;
        } else if (!(one.equals(two))){
            pass1.setError("Passwords Don't Match");
            return false;
        }
        return true;
    }
}
