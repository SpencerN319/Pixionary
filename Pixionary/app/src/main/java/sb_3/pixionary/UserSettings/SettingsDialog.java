package sb_3.pixionary.UserSettings;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import SaveData.UserDataDBHandler;
import sb_3.pixionary.MainMenuActivity;
import sb_3.pixionary.R;
import sb_3.pixionary.SharedSettings.Friends;

/**
 * Created by fastn on 2/16/2018.
 */

public class SettingsDialog extends Activity implements View.OnClickListener {

    private static final int UPDATE_REQUEST_ID = 2;
    private TextView title;
    private Button[] buttons  = new Button[5];

    /**
     * Creates the buttons for the dialog and makes them all clickable.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_dialog);
        buttons[0] = (Button) findViewById(R.id.button_profile);
        buttons[1] = (Button) findViewById(R.id.button_leaderboard);
        buttons[2] = (Button) findViewById(R.id.bt_Friends);
        buttons[3] = (Button) findViewById(R.id.bt_update);
        buttons[4] = (Button) findViewById(R.id.button_logout);
        for (Button button: buttons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_profile:
                nextActivity(ProfileActivity.class);
                break;
            case R.id.button_leaderboard:
                nextActivity(LeaderboardActivity.class);
                break;
            case R.id.bt_Friends:
                nextActivity(Friends.class);
                break;
            case R.id.bt_update:
                Intent intent = new Intent(this, UpdateAccount.class);
                startActivityForResult(intent, UPDATE_REQUEST_ID);
                break;
            case R.id.button_logout:
                logOut();
                break;
        }
    }

    private void logOut() {
        UserDataDBHandler db = new UserDataDBHandler(this);
        db.deleteOne(0);
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("action", "logout");
        setResult(MainMenuActivity.SETTINGS_ID, intent);
        finish();
    }

    private void nextActivity(Class selected) {
        Intent intent = new Intent(this, selected);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            return;
        } else{
            switch (requestCode){
                case UPDATE_REQUEST_ID:
                    String action = data.getStringExtra("action");
                    if(action.equals("delete")){
                        Intent delete = new Intent(this, MainMenuActivity.class);
                        delete.putExtra("action", action);
                        setResult(MainMenuActivity.SETTINGS_ID, delete);
                        finish();
                    } else {
                        Intent update = new Intent(this, MainMenuActivity.class);
                        update.putExtra("action", "update");
                        setResult(MainMenuActivity.SETTINGS_ID, update);
                        finish();
                    } break;
            }
        }
    }
}
