package sb_3.pixionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.io.IOException;
import java.net.Socket;

import Client.LoginThread;

public class LoginActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private TextView Attempts;
    private Button Login;
    private Button CreateAccount;
    private int attemptsLeft = 10;
    private boolean success = false;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText) findViewById(R.id.editText_username);
        Password = (EditText) findViewById(R.id.editText_password);
        Attempts = (TextView) findViewById(R.id.tvAttempts);
        Login = (Button) findViewById(R.id.button_login);
        CreateAccount = (Button) findViewById(R.id.create_account);



        Attempts.setText("Attempts Left: 10");
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = Username.getText().toString();
                password = Password.getText().toString();
                validateUserLocal();
                //validateUser(username, password);

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


    private void validateUser(String username, String password) {


        try {
            Socket socket = new Socket("localhost", 3212);

            LoginThread loginRequest = new LoginThread(socket, username, password);
            loginRequest.run();
            success = loginRequest.getSuccess();

        } catch (IOException e) {
            success = true;
        }
        if (success) {
            resultLogin();
        }
    }

    private void resultLogin() {
        Intent send = new Intent();
        send.putExtra("username", username);
        setResult(RESULT_OK, send);
        finish();
    }
}