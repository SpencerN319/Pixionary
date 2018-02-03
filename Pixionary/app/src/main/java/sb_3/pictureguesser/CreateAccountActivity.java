package sb_3.pictureguesser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateAccountActivity extends AppCompatActivity {

    private Button Create;
    private EditText Username;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Create = (Button) findViewById(R.id.btnCreate);
        Username = (EditText) findViewById(R.id.etCreateUser);
        Password = (EditText) findViewById(R.id.etCreatePass);

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Successfully created account
                createUser(Username.getText().toString(), Password.getText().toString());
                Log.d("CreateAccountActivity", "Username: " + Username.getText().toString() + "    Password: " + Password.getText().toString());
                //Method to send new User data to the server...
                //Going to be used to check the successful creation of the newUser.
                boolean success = true;
                if (success) {
                    Intent intent = new Intent(CreateAccountActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private String createUser(String user, String pass) {
        String newUser = "NewUser: " + user + " " + pass;
        return newUser;
    }

}
