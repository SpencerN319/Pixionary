package sb_3.pixionary;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.net.Socket;

import sb_3.pixionary.Client.LoginThread;


public class LoginActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private TextView Attempts;
    private Button Login;
    private Button CreateAccount;
    private Button Guest;
    private int attemptsLeft = 10;

    Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText) findViewById(R.id.editText_username);
        Password = (EditText) findViewById(R.id.editText_password);
        Attempts = (TextView) findViewById(R.id.tvAttempts);
        Login = (Button) findViewById(R.id.btnLogin);
        CreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        Guest = (Button) findViewById(R.id.btnGuest);


        Attempts.setText("Attempts Left: 10");
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Commented out because there's no server to contact yet.
                //validateUser(Username.getText().toString(), Password.getText().toString());
                validateUserLocal(Username.getText().toString(), Password.getText().toString());
            }
        });

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createAccount = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivityForResult(createAccount, 1);
            }
        });


    }

    //Just testing Authentication on frontend without the server.
    private void validateUserLocal(String username, String password) {
        String userInfo = "UserInfo: " + username + " " + password;
        Log.i("LoginActivity", userInfo);

        //Local test only.
        //TODO Remove admin check when not required
        if ((username.equals("admin")) && (password.equals("password"))) {
            returnUsernameAndFinish(username);
        } else {
            validateUser(username, password);
        }
    }

    private void validateUser(String username, String password) {
        boolean success = false;
        try {
            socket = new Socket("localhost", 9090);
            LoginThread loginRequest = new LoginThread(socket,
                    username, password);
            loginRequest.run();
            success = loginRequest.getSuccess();
        }
        catch (Exception e) {
            //TODO change once server is running
            success = true;
        }
        if (success) {
            System.out.println("Hit");
            returnUsernameAndFinish(username);
        } else  {
            attemptsLeft--;
            Attempts.setText("Attempts Left: " + String.valueOf(attemptsLeft));
            if (attemptsLeft == 0) {
                Login.setEnabled(false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedData){
        if(returnedData == null){
            return;
        }
        super.onActivityResult(requestCode, resultCode, returnedData);
        switch (requestCode){
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    returnUsernameAndFinish(returnedData.getStringExtra("username"));
                }
        }
    }

    public void returnUsernameAndFinish(String username){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("username", username);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}
