package sb_3.pixionary;


import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import java.util.Scanner;
import Client.ServiceToActivity;

public class LoginActivity extends AppCompatActivity {
    //Local stuff
    private EditText Username;
    private EditText Password;
    private TextView Attempts;
    private Button Login;
    private Button CreateAccount;
    private int attemptsLeft = 10;
    private boolean success = false;
    private String username;
    private String password;




    //Helping out the server stuff.
    private String send = null;
    private ServiceToActivity serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Username = (EditText) findViewById(R.id.editText_username);
        Password = (EditText) findViewById(R.id.editText_password);
        Attempts = (TextView) findViewById(R.id.tvAttempts);
        Login = (Button) findViewById(R.id.button_login);
        CreateAccount = (Button) findViewById(R.id.create_account);


        //All for server run.
        //Enables the ServiceTOActivity to run.
        enableReceiver();

        Attempts.setText("Attempts Left: 10");
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = Username.getText().toString();
                password = Password.getText().toString();
                validateUserLocal();
                validateUser(username, password);

            }
        });
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(serviceReceiver);
        super.onDestroy();
    }

    //Just testing Authentication on frontend without the server.
    private void validateUserLocal() {
        Log.i("LoginActivity", "User:" + username + "Pass: " + password);
        if ((username.equals("admin")) && (password.equals("password"))) {
            success = true;  //Set to true until backend is ready.
        } else {
            attemptsLeft--;
            Attempts.setText("Attempts Left: " + String.valueOf(attemptsLeft));
            if (attemptsLeft == 0) {
                Login.setEnabled(false);
            }
        }
        if (success) {
            resultLogin();
        }
    }



    private void sendDataToService() {
        Intent broadcaster = new Intent();
        broadcaster.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES); //Don't know what this is for.
        broadcaster.setAction("Server");
        if (send != null) {
            broadcaster.putExtra("toServer", send);
            sendBroadcast(broadcaster);
        }
    }

    private void validateUser(String username, String password) {
        setUserData(username, password);
        sendDataToService();
        //May need to add a delay here.
        receiveCredentialResult();
        resultLogin();
    }

    //Enable Activity to receive data.
    private void enableReceiver() {
        serviceReceiver = new ServiceToActivity("LoginActivity");
        IntentFilter filter = new IntentFilter("LoginActivity");
        registerReceiver(serviceReceiver, filter);
    }

    private void setUserData(String username, String password) {
        //Sending LoginRequest
        int lenUser = username.length();
        int lenPass = password.length();
        if (lenUser >= 6 && lenUser <= 20 && lenPass >= 8) {
            send = "LoginRequest " + username + " " + password;
        }
    }

    private void receiveCredentialResult() {
        String data = serviceReceiver.getData();
        Scanner scanInput = new Scanner(data);
        String id = scanInput.next();
        if (id.equals("LoginResult") && scanInput.hasNextBoolean()) {
            success = scanInput.nextBoolean();
        }

    }


    private void resultLogin() {
        Intent send = new Intent();
        send.putExtra("username", username);
        setResult(RESULT_OK, send);
        finish();
    }

}

    // To be used in Create Account Activity
    /*

    private boolean validUsername(String string) {
        //validate username;
        if(string.equals("")){
            usernameTextbox.setError("Enter a username");
            return false;
        } else if(string.length() > 20){
            usernameTextbox.setError("Max 20 characters");
            return false;
        } else if(string.length() < 5){
            usernameTextbox.setError("Minimum 5 characters");
            return false;
        }
        return true;
    }