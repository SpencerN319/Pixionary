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
                //Method to send new User data to the server... eventually
                createUser(Username.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void createUser(String user, String pass) {
        String newUser = "NewUser: " + user + " " + pass;
        //Pass string to the server, if successful server will return value.
        boolean success = true;
        Log.i("CreateAccountActivity", "Username: " + Username.getText().toString() + "    Password: " + Password.getText().toString());
        if (success) {
            Intent intent = new Intent(CreateAccountActivity.this, MainMenuActivity.class);
            startActivity(intent);
        }
    }

}
