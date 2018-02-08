package sb_3.pixionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameTextbox, passwordTextbox;
    private String username, password;
    private String[] upPair = new String[2];
    private Button login = (Button) findViewById(R.id.button_login);
    private Button crt_accnt = (Button) findViewById(R.id.button_createAccount);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        crt_accnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method to send new User data to the server... eventually
                moveToCreateAccount();
            }
        });
    }



    public void init() {
        usernameTextbox = (EditText) findViewById(R.id.editText_username);
        passwordTextbox = (EditText) findViewById(R.id.editText_password);

        username = String.valueOf(usernameTextbox.getText());
        password = String.valueOf(passwordTextbox.getText());
    }

    public void returnLoginInfo(View view) {
        upPair[0] = username;
        upPair[1] = password;
        Intent retval = new Intent();
        retval.putExtra("upPair",upPair);
        setResult(RESULT_OK, retval);
        finish();
    }

    public void moveToCreateAccount() {
        Intent move = new Intent(LoginActivity.this,CreateAccountActivity.class);
        startActivity(move);
    }


}
