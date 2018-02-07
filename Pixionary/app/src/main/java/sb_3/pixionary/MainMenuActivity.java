package sb_3.pixionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {

    private String username = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button button_joinGame = (Button) findViewById(R.id.button_joinGame);
        button_joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username == null){
                    startLoginActivity();
                }
                else{
                    Snackbar.make(view, "Open join game activity", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent i = new Intent(MainMenuActivity.this, GameBrowserActivity.class);
                    startActivity(i);
                }
            }
        });

        Button button_hostGame = (Button) findViewById(R.id.button_hostGame);
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
            }
        });

        Button button_buildGame = (Button) findViewById(R.id.button_buildGame);
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

        if(username == null){
            //Not logged in
            startLoginActivity();
        }
        else{
            //Logged in
            TextView usernameDisplay = (TextView) findViewById(R.id.textView_usernameDisplay);
            usernameDisplay.setText("You are currently logged in as " + username);
        }
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
            case 1:
                TextView usernameDisplay = (TextView) findViewById(R.id.textView_usernameDisplay);
                username = returnedData.getStringExtra("username");
                usernameDisplay.setText("You are currently logged in as " + username);
        }
    }

    public void startLoginActivity(){
        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login, 1);
        TextView usernameDisplay = (TextView) findViewById(R.id.textView_usernameDisplay);
        usernameDisplay.setText("You are not currently logged in");
    }
}
