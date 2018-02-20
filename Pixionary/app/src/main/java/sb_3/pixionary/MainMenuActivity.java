package sb_3.pixionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.InputStream;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.Utilities.POJO.User;
import sb_3.pixionary.Utilities.RequestLogin;

public class MainMenuActivity extends AppCompatActivity {
    public final int LOGIN_REQUEST_ID = 4;
    public static final int SETTINGS_REQUEST_ID = 5;
    private String username;
    private User user;
    TextView usernameDisplay;

    //Automated login
    UserDataDBHandler db;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button button_joinGame = (Button) findViewById(R.id.button_joinGame);
        Button button_hostGame = (Button) findViewById(R.id.button_hostGame);
        Button button_buildGame = (Button) findViewById(R.id.button_buildGame);
        Button button_login = (Button) findViewById(R.id.button_login);
        ImageButton button_settings = (ImageButton) findViewById(R.id.button_settings);
        usernameDisplay = (TextView) findViewById(R.id.textView_usernameDisplay);
        usernameDisplay.setText("You are currently not logged in");

        automaticLogin();

        button_joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username == null){
                    startLoginActivity();
                }
                else{
                    Intent i = new Intent(MainMenuActivity.this, GameBrowserActivity.class);
                    startActivity(i);
                }
            }
        });

        button_hostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username == null){
                    startLoginActivity();
                }
                else{
                    Snackbar.make(view, "Open host game activity", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                Snackbar.make(view, "Open host game activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button_buildGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username == null){
                    startLoginActivity();
                }
                else{
                    Snackbar.make(view, "Open build game activity", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username == null)
                    startLoginActivity();
            }
        });

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingsActivity();
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
        if(returnedData == null){
            return;
        }
        super.onActivityResult(requestCode, resultCode, returnedData);
        switch (requestCode){
            case LOGIN_REQUEST_ID:
                username = returnedData.getStringExtra("username");
                usernameDisplay.setText("You are currently logged in as " + username);
            case SETTINGS_REQUEST_ID:
                boolean logout = returnedData.getBooleanExtra("logout", false);
                if (logout) {
                    username = null;
                    usernameDisplay.setText("Logged Out!");
                }
        }
    }


    public void startLoginActivity() {
        Intent login = new Intent(MainMenuActivity.this, LoginActivity.class);
        TextView usernameDisplay = (TextView) findViewById(R.id.textView_usernameDisplay);
        usernameDisplay.setText("You are not currently logged in");
        startActivityForResult(login, LOGIN_REQUEST_ID);
    }

    public void startSettingsActivity() {
        Intent settingsIntent = new Intent(this, SettingsDialog.class);
        startActivityForResult(settingsIntent, SETTINGS_REQUEST_ID);
    }

    private void automaticLogin() {
        if (username == null) {
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
                                username = user.getUsername();
                                usernameDisplay.setText("You are currently logged in as " + username);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.setTitle("Failed");
                                progressDialog.setMessage("No user data found, please login!");
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
                progressDialog.setTitle("Failed");
                progressDialog.setMessage("No user data found, please login!");
                progressDialog.setCancelable(true);
            }
        }
    }

    private void startProgressDialog() {
        progressDialog = new ProgressDialog(MainMenuActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

}
