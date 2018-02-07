package sb_3.pictureguesser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void returnLoginInfo(View view) {
        EditText usernameTextbox = (EditText) findViewById(R.id.editText_username);
        EditText passwordTextbox = (EditText) findViewById(R.id.editText_password);
        String username = String.valueOf(usernameTextbox.getText());
        String password = String.valueOf(passwordTextbox.getText());
        String[] upPair = new String[2];
        upPair[0] = username;
        upPair[1] = password;
        Intent retval = new Intent();
        retval.putExtra("UPPair", upPair);
        setResult(RESULT_OK, retval);
        finish();
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        //editText editText = (editText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        startActivity(intent);


    }
}
