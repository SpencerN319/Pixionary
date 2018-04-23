package sb_3.pixionary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.AdminSettingsDialog.AdminSettings;
import sb_3.pixionary.UserSettings.SettingsDialog;
import sb_3.pixionary.Utilities.MainMenu.RequestLogin;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.hostgame.HostGameActivity;
import sb_3.pixionary.joingame.GameBrowserActivity;
import sb_3.pixionary.SharedSettings.Images;

public class MainMenuActivity extends AppCompatActivity {

    private Context context;
    public static final int LOGIN_REQUEST_ID = 4;
    public static final int GUEST_REQUEST_ID = 6;
    public static final int CREATEACCOUNT_REQUEST_ID = 7;
    public static final int SETTINGS_ID = 8;
    public  static User user;
    TextView usernameDisplay;
    private Switch switch_admin;
    private ImageButton button_AdminSettings, button_settings;

    //Automated login
    UserDataDBHandler db;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        context = this;

        switch_admin = (Switch) findViewById(R.id.sw_admin);
        button_AdminSettings = (ImageButton) findViewById(R.id.button_admin_settings);
        switch_admin.setVisibility(View.INVISIBLE);
        button_AdminSettings.setVisibility(View.INVISIBLE);
        switch_admin.setClickable(false);
        button_AdminSettings.setClickable(false);

        Button button_joinGame = (Button) findViewById(R.id.button_joinGame);
        Button button_hostGame = (Button) findViewById(R.id.button_hostGame);
        Button button_buildGame = (Button) findViewById(R.id.button_buildGame);
        Button button_login = (Button) findViewById(R.id.button_login);
        button_settings = (ImageButton) findViewById(R.id.button_settings);
        usernameDisplay = (TextView) findViewById(R.id.textView_usernameDisplay);
        usernameDisplay.setText("Currently not logged in");

        if(user == null){
            automaticLogin();
        }


        button_joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null){
                    startLoginActivity();
                }else {
                    updateDBUserType("player");
                    Intent i = new Intent(MainMenuActivity.this, GameBrowserActivity.class);
                    startActivity(i);
                }
            }
        });

        button_hostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null){
                    startLoginActivity();
                }else {
                    updateDBUserType("host");
                    Intent intent = new Intent(MainMenuActivity.this, HostGameActivity.class);
                    intent.putExtra("username", user.getUsername());
                    startActivity(intent);
                }
            }
        });

        button_buildGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null){
                    startLoginActivity();
                }else {
                    Intent intent = new Intent(MainMenuActivity.this , Images.class);
                    startActivity(intent);
                }
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null)
                    startLoginActivity();
            }
        });

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null){
                    startSettingsActivity();
                }
            }
        });

        button_AdminSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdminSettingsActivity();
            }
        });

        switch_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(switch_admin.isChecked())){
                    button_settings.setVisibility(View.VISIBLE);
                    button_settings.setClickable(true);
                    button_AdminSettings.setVisibility(View.INVISIBLE);
                    button_AdminSettings.setClickable(false);
                } else {
                    button_settings.setVisibility(View.INVISIBLE);
                    button_settings.setClickable(false);
                    button_AdminSettings.setVisibility(View.VISIBLE);
                    button_AdminSettings.setClickable(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedData){
        if(resultCode == 0){
            return;
        }
        super.onActivityResult(requestCode, resultCode, returnedData);
        switch (requestCode){

            case LOGIN_REQUEST_ID:
                usernameDisplay.setText("Logged in as: " + this.user.getUsername());
                is_admin();
                break;
            case GUEST_REQUEST_ID:
                usernameDisplay.setText("Logged in as: " + user.getUsername());
                break;
            case SETTINGS_ID:
                String action = returnedData.getStringExtra("action");
                if(action.equals("delete")){
                    user =null;
                    is_admin();
                    usernameDisplay.setText("Currently not logged in");
                } else if(action.equals("logout")) {
                    usernameDisplay.setText("Currently not logged in");
                    user = null;
                    is_admin();
                } else if(action.equals("update")){
                    usernameDisplay.setText("Logged in as: " + this.user.getUsername());
                    is_admin();
                }
                break;
        }
    }

    private void updateDBUserType(String userType) {
        UserDataDBHandler db = new UserDataDBHandler(context);
        User user = db.getUser("0");
        user.setUserType(userType);
        db.deleteOne(0);
        db.addUser(user);
    }
    private void startLoginActivity() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login, LOGIN_REQUEST_ID);
    }

    private void startSettingsActivity() {
        Intent settingsIntent = new Intent(this, SettingsDialog.class);
        startActivityForResult(settingsIntent, SETTINGS_ID);
    }

    private void startAdminSettingsActivity() {
        Intent admin_settings = new Intent(this, AdminSettings.class);
        startActivity(admin_settings);
    }

    private void automaticLogin() {
        if (user == null) {
            startProgressDialog();
            //Creates the db helper.
            requestQueue = Volley.newRequestQueue(MainMenuActivity.this);
            db = new UserDataDBHandler(this);
            user = db.getUser("0");
            if (user != null) {
                RequestLogin request = new RequestLogin(user.getUsername(), user.getPassword(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject success = new JSONObject(response);
                            if (success.getBoolean("success")) {
                                usernameDisplay.setText("Logged in as " + user.getUsername());
                                progressDialog.dismiss();
                                is_admin();
                            } else {
                                progressDialog.setTitle("Please Login");
                                progressDialog.setMessage("No user data found");
                                startLoginActivity();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("AutoLoginAttempt", "Failed with Errors.");
                    }
                });
                requestQueue.add(request);
            } else {
                progressDialog.setTitle("Please Login");
                progressDialog.setMessage("No user data found");
                startLoginActivity();
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UserDataDBHandler db = new UserDataDBHandler(context);
        if(db.getUser("0") != null){
            set_user(db.getUser("0"));
        } else {
            startLoginActivity();
        }
    }

    private void startProgressDialog() {
        progressDialog = new ProgressDialog(MainMenuActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public static void set_user(User new_user){
        user = new_user;
    }

    private void is_admin(){
        if(user != null){
            if(user.getUserType().equals("admin")){
                switch_admin.setClickable(true);
                switch_admin.setVisibility(View.VISIBLE);
            }
        } else {
            switch_admin.setClickable(false);
            switch_admin.setVisibility(View.INVISIBLE);
        }
    }
}
