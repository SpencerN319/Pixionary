package sb_3.pixionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameTextbox, passwordTextbox;
    private String username, password;
    private String[] upPair = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
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

    private boolean validPassword(String string){
        if(string.equals("")){
            passwordTextbox.setError("Enter a username");
            return false;
        } else if(string.length() > 8){
            passwordTextbox.setError("Max 8 Characters");
            return false;
        } else if(string.length() < 6){
            passwordTextbox.setError("Minimum 6 Characters");
            return false;
        }
        return true;
    }

    */


}
